using TMPro;
using UnityEngine;
using UnityEngine.UI;
using Unity.Services.Lobbies.Models;
using Unity.Services.Lobbies;
using System.Collections.Generic;
using Unity.Netcode;

public class ManagerUI : MonoBehaviour
{
    public static ManagerUI Instance { get; private set; }

    [SerializeField] private GameObject startMenu;
    [SerializeField] private GameObject createLobby;
    [SerializeField] private GameObject joinLobby;
    [SerializeField] private GameObject lobby;
    [SerializeField] private GameObject game;
    [SerializeField] private GameObject hostLeft;
    [SerializeField] private GameObject loadSessionsMultiplayer;

    private string playerName;
    private string lobbyName;
    private string lobbyCode;

    private bool isLobbyPrivate;

    [SerializeField] private Button targetButton;
    [SerializeField] private TMP_Text buttonText;

    [SerializeField] private Transform lobbyListContainer; // Container dos itens (Content de um ScrollView)
    [SerializeField] private GameObject lobbyItemPrefab;   // Prefab do item de lobby

    [SerializeField] private Transform playerListContainer; 
    [SerializeField] private GameObject playerItemPrefab;

    [SerializeField] private Transform sessionListContainer;
    [SerializeField] private GameObject sessionItemPrefab;

    [SerializeField] private TMP_Text lName;
    [SerializeField] private TMP_Text code;

    [SerializeField] private LobbyManager lobbyManager;

    private Lobby currentlobby;
    private float playerListRefreshTimer = 0f;
    private const float PLAYER_LIST_REFRESH_INTERVAL = 1.5f;


    [SerializeField] private GameObject startGame;
    [SerializeField] private List<GameObject> models;
    [SerializeField] private List<string> classes;
    [SerializeField] private List<GameObject> playersPrefabs;

    [SerializeField] private Button warriorButton;
    [SerializeField] private Button mageButton;
    [SerializeField] private Button clericButton;

    [SerializeField] private Button readyButton;
    [SerializeField] private TMP_Text readyButtonText;
    [SerializeField] private TMP_Text className;

    [SerializeField] private GameUI gameUI;

    private bool LoadSavedSession = false;

    [SerializeField]
    private bool isSingleplayer = false;

    private bool inGame = false;

    private bool startSinglePlayer = false;

    void Awake()
    {
        // Singleton
        if (Instance == null)
        {
            Instance = this;
            DontDestroyOnLoad(gameObject);
        }
        else
        {
            Destroy(gameObject);
            return;
        }
    }

    void Start()
    {
        StartMenu();
        isLobbyPrivate = false;

        if(!isSingleplayer)
            targetButton.onClick.AddListener(ToggleText);

        lobbyName = null;
        UserData userData = SaveSystem.LoadUser();
        playerName = userData.UserName;

        foreach (GameObject gameObject in models)
        {
            gameObject.SetActive(false);
        }

        readyButton.interactable = false;
        className.text = "Choose Skin";

        warriorButton.onClick.AddListener(() => SelectCharacter(classes[0]));
        mageButton.onClick.AddListener(() => SelectCharacter(classes[1]));
        clericButton.onClick.AddListener(() => SelectCharacter(classes[2]));
    }

    void Update()
    {
        if(lobby.activeSelf && currentlobby != null)
        {
            playerListRefreshTimer -= Time.deltaTime;
            if (playerListRefreshTimer <= 0f)
            {
                playerListRefreshTimer = PLAYER_LIST_REFRESH_INTERVAL;

                if(!isSingleplayer )
                    UpdatePlayerListUI(currentlobby);

                if (lobbyManager.IsLobbyHost() && lobbyManager.AreAllPlayersReady())
                {
                    lobbyManager.StarGame();
                }
            }
        }

        if (lobbyManager.IsGameStarted() && !inGame)
        {
            Game();
            NetworkManager.Singleton.OnClientDisconnectCallback += HandleClientDisconnected;
            inGame = true;  
        }
    }

    private void HandleClientDisconnected(ulong clientId)
    {
        // Se for o proprio cliente (nao o host)
        if (clientId == NetworkManager.Singleton.LocalClientId && !NetworkManager.Singleton.IsHost)
        {
            ShowHostLeftScreen();
        }
    }

    private void ShowHostLeftScreen()
    {
        game.SetActive(false);
        hostLeft.SetActive(true);
    }

    public void ChangeLobbyName(string input)
    {
        lobbyName = input;
    }

    //Botao de Lobbies Publicos e Privados
    private void ToggleText()
    {
        isLobbyPrivate = !isLobbyPrivate;
        UpdateButtonText();
    }

    private void UpdateButtonText()
    {
        buttonText.text = isLobbyPrivate ? "Private" : "Public";
    }
    

    public async void ListLobbies()
    {
        try
        {
            QueryResponse query = await LobbyService.Instance.QueryLobbiesAsync();
            UpdateLobbyListUI(query.Results);
        }
        catch (LobbyServiceException e)
        {
            Debug.LogError("Erro ao obter lobbies. Erro: " + e);
        }
    }

    private void UpdateLobbyListUI(List<Lobby> lobbies)
    {
        // Limpa a lista atual
        foreach (Transform child in lobbyListContainer)
        {
            Destroy(child.gameObject);
        }

        // Cria um item para cada lobby
        foreach (Lobby lobby in lobbies)
        {
            GameObject lobbyItem = Instantiate(lobbyItemPrefab, lobbyListContainer);

            TextMeshProUGUI nameText = lobbyItem.transform.Find("Name").GetComponent<TextMeshProUGUI>();
            TextMeshProUGUI playersText = lobbyItem.transform.Find("Players").GetComponent<TextMeshProUGUI>();

            nameText.text = lobby.Name;
            playersText.text = $"{lobby.Players.Count}/{lobby.MaxPlayers}";

            // Botao para "entrar" no lobby
            Button joinButton = lobbyItem.GetComponent<Button>();

            joinButton.onClick.AddListener(() => {
                Debug.Log($"Entrou no lobby: {lobby.Name}"); 

                Lobby(lobby.Id);
            });
        }
    }

    private void UpdatePlayerListUI(Lobby lobby)
    {
        // Limpa a lista atual
        foreach (Transform child in playerListContainer)
        {
            Destroy(child.gameObject);
        }

        lName.text = lobby.Name;
        code.text = lobby.LobbyCode;

        // Cria um item para cada lobby
        foreach (Player player in lobby.Players)
        {
            GameObject lobbyItem = Instantiate(playerItemPrefab, playerListContainer);

            TextMeshProUGUI nameText = lobbyItem.transform.Find("Name").GetComponent<TextMeshProUGUI>();
            TextMeshProUGUI classText = lobbyItem.transform.Find("Class").GetComponent<TextMeshProUGUI>();

            nameText.text = player.Data["PlayerName"].Value ?? "Jogador Desconhecido";
            classText.text = player.Data["CharacterType"].Value ?? "Sem Classe";

        }
    }

    public void ShowSavedSessions()
    {
        // Limpar sessoes anteriores
        foreach (Transform child in sessionListContainer)
        {
            Destroy(child.gameObject);
        }

        // Carregar todas as sessoes
        List<GameSessionData> allSessions = null;
        if (isSingleplayer)
        {
            allSessions = SaveSystem.GetSingleplayerSessions();
        }
        else
        {
            allSessions = SaveSystem.GetMultiplayerSessions();
        }

        foreach (GameSessionData session in allSessions)
        {
            GameObject item = Instantiate(sessionItemPrefab, sessionListContainer);

            TextMeshProUGUI nameText = item.transform.Find("Name").GetComponent<TextMeshProUGUI>();
            TextMeshProUGUI playersText = item.transform.Find("Players").GetComponent<TextMeshProUGUI>();

            nameText.text = session.SessionName;
            playersText.text = session.Players.Count + " players";

            Button button = item.GetComponent<Button>();
            string sessionName = session.SessionName;

            button.onClick.AddListener(() =>
            {
                CreateLobbyFromSession(sessionName);
            });
        }
    }


    // Trocas de Menus
    public void StartMenu()
    {
        startMenu.SetActive(true);
        createLobby.SetActive(false);

        if(joinLobby != null)
            joinLobby.SetActive(false);

        lobby.SetActive(false);
        game.SetActive(false);
        hostLeft.SetActive(false);
        loadSessionsMultiplayer.SetActive(false);
        LoadSavedSession = false;
    }

    public void CreateLobby()
    {
        startMenu.SetActive(false);
        createLobby.SetActive(true);
    }

    public void JoinLobby()
    {
        startMenu.SetActive(false);
        lobby.SetActive(false);
        joinLobby.SetActive(true);
        loadSessionsMultiplayer.SetActive(false);
        ListLobbies();
    }

    public void MultiplayerSessions()
    {
        startMenu.SetActive(false);
        loadSessionsMultiplayer.SetActive(true);
        ShowSavedSessions();
    }

    public void JoinLobbyFromCode(string code)
    {
        Lobby(null, code);
    }

    public void LobbyFromCreate()
    {
        Lobby(null, null);
    }

    public async void Lobby(string lobbyId = null, string codeLobby = null, bool singlePlayer = false)
    {
        createLobby.SetActive(false);

        if(joinLobby != null)
            joinLobby.SetActive(false);

        lobby.SetActive(false);

        try
        {
            if (singlePlayer)
            {
                currentlobby = await lobbyManager.CreateLobby(playerName, lobbyName, true);
            }
            else if (lobbyName != null)
            {
                currentlobby = await lobbyManager.CreateLobby(playerName, lobbyName, isLobbyPrivate);
            }
            else if(codeLobby == null)
            {
                currentlobby = await lobbyManager.JoinLobbyById(lobbyId, playerName);
            }
            else
            {
                currentlobby = await lobbyManager.JoinLobby(codeLobby, playerName);
            }

            if (currentlobby != null)
            {
                if(!isSingleplayer)
                    UpdatePlayerListUI(currentlobby);
                lobby.SetActive(true);
                Debug.Log($"Lobby criado com c�digo: {currentlobby.LobbyCode}");
            }
        }
        catch (System.Exception e)
        {
            Debug.LogError($"Erro: {e.Message}");
            StartMenu();
        }
    }

    public void LobbyFromCreateSinglePlayer()
    {
        Lobby(null, null, true);
    }

    public async void CreateLobbyFromSession(string nome)
    {
        try
        {
            loadSessionsMultiplayer.SetActive(false);
            lobby.SetActive(false);

            lobbyName = nome;
            currentlobby = await lobbyManager.CreateLobby(playerName, nome, true);

            if (currentlobby != null)
            {
                if (!isSingleplayer)
                    UpdatePlayerListUI(currentlobby);
                LoadSavedSession = true;
                lobby.SetActive(true);
            }
        }
        catch (System.Exception e)
        {
            Debug.LogError($"Erro: {e.Message}");
        }
    }

    public void Game()
    {
        lobby.SetActive(false);
        game.SetActive(true);
        RemoveModels();
        gameUI.SetName(playerName);
        StartCoroutine(gameUI.ShowReadyButtonAfterDelay());
    }

    public void LeaveLobby()
    {
        lobbyManager.LeaveLobby();
        currentlobby = null;
        lobbyName = null;

        if (isSingleplayer)
        {
            StartMenu();
        }
        else
        {
            JoinLobby();
        }
    }


    public void refreshLobbies()
    {
        ListLobbies();
        currentlobby = null;
    }


    public void SelectCharacter(string characterName)
    {
        // Desativa todos os modelos
        foreach (GameObject model in models)
        {
            model.SetActive(false);
        }

        // Ativa o modelo correspondente e atualiza no lobby
        int index = classes.IndexOf(characterName);
        if (index >= 0 && index < models.Count)
        {
            models[index].SetActive(true);

            if(!isSingleplayer)
                lobbyManager.UpdatePlayerCharacter(characterName);

            UpdateCharacterButtons(characterName);

            readyButton.interactable = true;
        }
    }

    private void UpdateCharacterButtons(string selectedCharacter)
    {
        warriorButton.interactable = true;
        mageButton.interactable = true;
        clericButton.interactable = true;

        switch (selectedCharacter)
        {
            case "Knight":
                warriorButton.interactable = false;
                className.text = "Knight";
                break;
            case "Mage":
                mageButton.interactable = false;
                className.text = "Mage";
                break;
            case "Cleric":
                clericButton.interactable = false;
                className.text = "Cleric";
                break;
        }
    }


    public void Ready()
    {
        if(readyButtonText.text == "Ready")
        {
            readyButtonText.text = "Cancel";
            warriorButton.interactable = false;
            mageButton.interactable = false;
            clericButton.interactable = false;

            lobbyManager.ToggleReadyStatus(true);
        }
        else
        {
            warriorButton.interactable = true;
            mageButton.interactable = true;
            clericButton.interactable = true;
            readyButtonText.text = "Ready";
            lobbyManager.ToggleReadyStatus(false);
        }
    }

    public string GetClassName()
    {
        return className.text;
    }

    public void RemoveModels()
    {
        foreach (GameObject model in models)
        {
            Destroy(model);
        }
    }

    public GameUI GetGameUI()
    {
        return gameUI;
    }

    public void GoToMainMenu()
    {
        SoundManager.Instance.PlayMenuMusic();
        Destroy(Instance.gameObject);
        Destroy(GameManager.Instance.gameObject);
        Destroy(NetworkManager.Singleton.gameObject);
        UnityEngine.SceneManagement.SceneManager.LoadScene("Start");
    }

    public string GetLobbyName()
    {
        return lobbyName;
    }

    public bool GetLoadSession()
    {
        return LoadSavedSession;
    }

    public void ExitGame()
    {
        Application.Quit();
    }
}
