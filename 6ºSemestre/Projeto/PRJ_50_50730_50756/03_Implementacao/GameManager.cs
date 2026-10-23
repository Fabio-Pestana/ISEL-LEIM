using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Unity.Netcode;
using UnityEngine;
using UnityEngine.SceneManagement;
using static PlayerSessionData;

public class GameManager : NetworkBehaviour
{
    public List<GameObject> players = new List<GameObject>(); // Lista de jogadores

    public static GameManager Instance { get; private set; }

    [SerializeField] private GameObject startGameButton;

    private bool needUpdatePlayerList = false;

    private HashSet<ulong> playersLoaded = new HashSet<ulong>();

    private int level = 0;

    [SerializeField]
    private bool multiplayer = true;

    private int numberOfDeadEnemies = 0;

    private GameSessionData gameSessionData;

    private bool _onSceneEventSubscribed = false;

    private void Awake()
    {
        if (Instance != null && Instance != this)
        {
            Destroy(gameObject);
            return;
        }

        Instance = this;
        DontDestroyOnLoad(gameObject);
    }

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (IsServer)
        {
            // Registrar para novos jogadores
            NetworkManager.Singleton.OnClientConnectedCallback += OnClientConnected;
            NetworkManager.Singleton.OnClientDisconnectCallback += OnClientDisconnected;
            NetworkManager.SceneManager.OnSceneEvent += OnSceneEvent;
            _onSceneEventSubscribed = true;

            string sessionName = ManagerUI.Instance.GetLobbyName();
            if (ManagerUI.Instance.GetLoadSession())
            {
                gameSessionData = SaveSystem.GetSessionFromName(sessionName);
            }
            else
            {
                gameSessionData = null;
            }
        }
    }

    private void OnClientConnected(ulong clientId)
    {
        needUpdatePlayerList = true;
    }

    private async void OnClientDisconnected(ulong clientId)
    {
        Debug.Log(clientId);
        await Task.Yield();
        needUpdatePlayerList = true;
    }

    IEnumerator FindPlayers()
    {
        yield return new WaitForSeconds(2f);
        Debug.Log("====================== Procurar players....");
        players = new List<GameObject>(GameObject.FindGameObjectsWithTag("Player"));
        if(players == null || players.Count == 0)
            needUpdatePlayerList = true;
    }

    private void Update()
    {
        if (!IsServer) return;

        if (needUpdatePlayerList)
        {
            needUpdatePlayerList = false;
            StartCoroutine(FindPlayers());
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void DistributeCoinsServerRPC(int totalCoins)
    {
        if (!IsServer) return;

        if (players == null || players.Count == 0) return;

        int coinsPerPlayer = Mathf.FloorToInt((float) totalCoins / players.Count);

        foreach (GameObject playerObj in players)
        {
            if (playerObj == null) continue;

            CharacterBase character = playerObj.GetComponent<CharacterBase>();
            if (character != null)
            {
                character.AddCoinsServerRpc(coinsPerPlayer);
            }
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void CheckIfAllPlayersAreReadyServerRpc()
    {
        if (players == null || players.Count == 0) return;

        var alivePlayers = players.Where(player =>
        {
            if (player == null) return false;
            CharacterBase character = player.GetComponent<CharacterBase>();
            return character != null && character.isAlive.Value;
        }).ToList();


        if (alivePlayers.Count == 0) return;

        bool allReady = alivePlayers.All(player =>
        {
            CharacterBase character = player.GetComponent<CharacterBase>();
            return character != null && character.IsReady();
        });

        if (allReady)
        {
            level++;

            foreach (GameObject player in players)
            {
                CharacterBase character = player.GetComponent<CharacterBase>();
                if (character != null)
                {
                    if(level <= 10)
                    {
                        character.ChangeSceneServerRpc(1);
                    }
                    else
                    {
                        character.ChangeSceneServerRpc(5);
                    }
                    character.SetChangeFloorServerRpc(false);
                }
            }
        

            if (level >= 2) DestroyAllEnemies();

            // Determinar qual cena carregar com base no level
            string sceneName = "";
            if ((level >= 1 && level <= 4) || (level >= 6 && level <= 9))
            {
                sceneName = Random.Range(0, 2) == 0 ? "Dungeon1" : "Dungeon2";
            }
            else if (level == 5 || level == 10)
            {
                sceneName = Random.Range(0, 2) == 0 ? "Dungeon3" : "Dungeon4";
            }
            else
            {
                sceneName = "NexusContinue";
            }

            Debug.Log($"Todos os jogadores est�o prontos! Carregando cena {sceneName}...");
            NetworkManager.SceneManager.LoadScene(sceneName, LoadSceneMode.Single);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void CheckAllPlayersDeadServerRpc()
    {
        if (!IsServer || players == null || players.Count == 0) return;

        bool allDead = players.All(player =>
        {
            if (player == null) return false;
            CharacterBase character = player.GetComponent<CharacterBase>();
            return character != null && !character.isAlive.Value;
        });

        if (allDead)
        {
            foreach (GameObject player in players)
            {
                CharacterBase character = player.GetComponent<CharacterBase>();
                if (character != null)
                {
                    character.ChangeSceneServerRpc(3);
                    character.SetChangeFloorServerRpc(false);
                }
            }

            DestroyAllEnemies();

            Debug.Log("Todos os jogadores morreram! Enviando para a cena 'Nexus'...");
            NetworkManager.SceneManager.LoadScene("NexusContinue", LoadSceneMode.Single);
        }
    }

    private void OnSceneEvent(SceneEvent sceneEvent)
    {
        if (sceneEvent.SceneEventType == SceneEventType.LoadComplete)
        {
            playersLoaded.Add(sceneEvent.ClientId);

            Debug.Log($"Player {sceneEvent.ClientId} carregou Dungeon1. Total prontos: {playersLoaded.Count}/{NetworkManager.ConnectedClientsIds.Count}");

            // Se todos os conectados carregaram
            if (playersLoaded.Count == NetworkManager.ConnectedClientsIds.Count)
            {
                if (sceneEvent.SceneName == "NexusContinue")
                {
                    level = 0;
                    StartCoroutine(ResetReviveForAllPlayers());
                }
                else
                {
                    StartCoroutine(DelayedPositionPlayers());
                }
                playersLoaded.Clear();
            }
        }
    }

    private IEnumerator ResetReviveForAllPlayers()
    {
        yield return new WaitForSeconds(1f);

        players = new List<GameObject>(GameObject.FindGameObjectsWithTag("Player"));

        foreach (GameObject player in players)
        {
            if (player == null) continue;

            CharacterBase character = player.GetComponent<CharacterBase>();
            if (character != null)
            {
                character.ResetReviveServerRpc(true);
                character.ChangeSceneServerRpc(4);
                character.ChangeFloorValueServerRpc("Nexus");
                character.SetInGameStateServerRpc(false);
            }
        }
    }

    private IEnumerator DelayedPositionPlayers()
    {
        yield return new WaitForSeconds(1f);
        PositionPlayersInDungeonServerRpc();
    }

    [ServerRpc(RequireOwnership = false)]
    private void PositionPlayersInDungeonServerRpc()
    {
        players = new List<GameObject>(GameObject.FindGameObjectsWithTag("Player"));

        if (players == null || players.Count == 0)
        {
            Debug.LogWarning("Nenhum jogador encontrado ao tentar posicionar.");
            return;
        }

        foreach (GameObject player in players)
        {
            if (player == null) continue;

            CharacterBase character = player.GetComponent<CharacterBase>();

            if (character != null)
            {
                character.ChangeSceneServerRpc(2);
                character.SetInGameStateServerRpc(true);
                character.ChangeFloorValueServerRpc($"Floor {level}");
            }
        }

        Debug.Log("Todos os jogadores foram posicionados!");
    }

    public void DestroyAllEnemies()
    {
        GameObject[] enemyObjects = GameObject.FindGameObjectsWithTag("Enemy");

        foreach (GameObject enemyObj in enemyObjects)
        {
            Skeleton s = enemyObj.GetComponent<Skeleton>();
            if (s != null)
            {
                s.DestroyFromScene();
            }
        }
    }

    public int GetLevel()
    {
        return level;
    }

    public void ResetNumberOfDeadEnemies()
    {
        numberOfDeadEnemies = 0;
    }

    public void KilledBoss()
    {
        foreach (GameObject player in players)
        {
            CharacterBase character = player.GetComponent<CharacterBase>();
            if (character != null)
            {
                character.SetChangeFloorServerRpc(true);
            }
        }
    }

    public void EnemyKilled()
    {
        if (level == 5 || level == 10) return;

        numberOfDeadEnemies++;

        if (numberOfDeadEnemies > 7)
        {
            foreach (GameObject player in players)
            {
                CharacterBase character = player.GetComponent<CharacterBase>();
                if (character != null)
                {
                    character.SetChangeFloorServerRpc(true);
                }
            }

            numberOfDeadEnemies = 0;
        }
    }

    public void SaveGameSession()
    {
        List<PlayerSessionData> playerData = new List<PlayerSessionData>();

        foreach (GameObject player in players)
        {
            if (player == null) continue;

            CharacterBase character = player.GetComponent<CharacterBase>();
            if (character != null)
            {
                string id = character.id.Value.ToString();
                int coins = character.GetCoins();
                int level = character.GetPlayerLevel();
                PlayerClass type = PlayerClass.Cleric;
                if(character is Mage) type = PlayerClass.Mage;
                else if (character is Knight) type = PlayerClass.Knight;

                playerData.Add(new PlayerSessionData(id, level, coins, type));
            }
        }

        string sessionName = ManagerUI.Instance.GetLobbyName();
        GameSessionData session = new GameSessionData(
            sessionName,
            multiplayer,
            playerData
        );

        SaveSystem.UpdateGameSession(session);
    }

    public GameSessionData GetData()
    {
        return gameSessionData;
    }


    private void OnDestroy()
    {
        if (NetworkManager.Singleton != null)
        {
            NetworkManager.Singleton.OnClientConnectedCallback -= OnClientConnected;
            NetworkManager.Singleton.OnClientDisconnectCallback -= OnClientDisconnected;

            if(_onSceneEventSubscribed)
                NetworkManager.SceneManager.OnSceneEvent -= OnSceneEvent;
        }
    }

}
