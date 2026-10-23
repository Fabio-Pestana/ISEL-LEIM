using UnityEngine;
using System.Collections.Generic;
using Unity.Services.Authentication;
using Unity.Services.Core;
using Unity.Services.Lobbies.Models;
using Unity.Services.Lobbies;
using System.Threading.Tasks;
using Unity.Netcode;
using UnityEngine.SceneManagement;


public class LobbyManager : MonoBehaviour
{

    private Lobby hostlobby;
    private Lobby joinedlobby;
    private float pingLobby;
    private float maxPingLobby = 15; // 30 segundos sem atividade o lobby desaparece
    private float updateLobby;
    private float maxUpdate = 3f;

    private const string KEY_START_GAME = "StartGame_Code";
    private const string KEY_CHARACTER_TYPE = "CharacterType";
    private const string KEY_READY_STATUS = "IsReady";

    [SerializeField]
    private RelayManager relay;

    private bool gameStarted;

    void Awake()
    {
        DontDestroyOnLoad(gameObject);
    }

    async void Start()
    {
        gameStarted = false;
        hostlobby = null;
        joinedlobby = null;

        await UnityServices.InitializeAsync();

        AuthenticationService.Instance.SignedIn += () =>
        {
            Debug.Log("Signed in " + AuthenticationService.Instance.PlayerId);
        };

        await AuthenticationService.Instance.SignInAnonymouslyAsync();
    }

    void Update()
    {
        if (!gameStarted)
        {
            KeepLobbyAlive();
            HandleLobbyUpdates();
        }
    }

    public async Task<Lobby> CreateLobby(string name, string lobbyName, bool isPrivate)
    {
        try
        {
            int maxPlayers = 3;

            CreateLobbyOptions options = new CreateLobbyOptions();
            options.IsPrivate = isPrivate;
            options.Player = GetPlayer(name);
            options.Data = new Dictionary<string, DataObject>
            {
                { KEY_START_GAME, new DataObject(DataObject.VisibilityOptions.Member, "0") }
            };

            Lobby lobby = await LobbyService.Instance.CreateLobbyAsync(lobbyName, maxPlayers, options);

            hostlobby = lobby;
            joinedlobby = lobby;

            Debug.Log("Created Lobby " + lobby.Name + " " + lobby.MaxPlayers + " " + lobby.LobbyCode);
            return lobby;
        }catch(LobbyServiceException e)
        {
            Debug.Log(e);
            return null;
        }
    }

    private async void KeepLobbyAlive()
    {
        if(hostlobby != null)
        {
            pingLobby = pingLobby - Time.deltaTime;
            if(pingLobby < 0f)
            {
                pingLobby = maxPingLobby;
                await LobbyService.Instance.SendHeartbeatPingAsync(hostlobby.Id);
            }
            
        }
    }

    private async void HandleLobbyUpdates()
    {
        if (joinedlobby != null && !gameStarted)
        {
            updateLobby = updateLobby - Time.deltaTime;
            if (updateLobby < 0f)
            {
                updateLobby = maxUpdate;
                joinedlobby = await LobbyService.Instance.GetLobbyAsync(joinedlobby.Id);

                if (joinedlobby.Data[KEY_START_GAME].Value != "0")
                {
                    if (!IsLobbyHost())
                    {
                        relay.JoinRelay(joinedlobby.Data[KEY_START_GAME].Value);
                        gameStarted = true;
                    }

                    joinedlobby = null;
                }
            }
        }
    }

    public async Task<Lobby> JoinLobby(string code, string name)
    {
        try
        {
            JoinLobbyByCodeOptions options = new JoinLobbyByCodeOptions();
            options.Player = GetPlayer(name);

            Lobby l = await LobbyService.Instance.JoinLobbyByCodeAsync(code, options);
            joinedlobby = l;
            return l;
        }
        catch (LobbyServiceException e)
        {
            Debug.Log(e);
            return null;
        }
    }

    public async Task<Lobby> JoinLobbyById(string id, string name)
    {
        try
        {
            JoinLobbyByIdOptions options = new JoinLobbyByIdOptions();
            options.Player = GetPlayer(name);

            Lobby l = await LobbyService.Instance.JoinLobbyByIdAsync(id, options);
            joinedlobby = l;
            return l;
        }
        catch (LobbyServiceException e)
        {
            Debug.Log(e);
            return null;
        }
    }

    private Player GetPlayer(string name)
    {
        return new Player
        {
            Data = new Dictionary<string, PlayerDataObject>
            {
                    {"PlayerName", new PlayerDataObject(PlayerDataObject.VisibilityOptions.Member, name) },
                    {KEY_CHARACTER_TYPE, new PlayerDataObject(PlayerDataObject.VisibilityOptions.Member, "Choosing") },
                    {KEY_READY_STATUS, new PlayerDataObject(PlayerDataObject.VisibilityOptions.Member, "false") }
            }
        };
    }

    public async void LeaveLobby()
    {
        try
        {
            await LobbyService.Instance.RemovePlayerAsync(joinedlobby.Id, AuthenticationService.Instance.PlayerId);
            joinedlobby = null;
            hostlobby = null;
        }
        catch (LobbyServiceException e)
        {
            Debug.Log(e);
        }
    }

    public bool IsLobbyHost()
    {
        return hostlobby != null;
    }

    public bool IsGameStarted()
    {
        return gameStarted;
    }

    public async void StarGame()
    {
        if(IsLobbyHost() && !gameStarted)
        {
            try
            {
                Debug.Log("Start Game!");

                string relayCode = await relay.CreateRelay();

                Lobby lobby = await LobbyService.Instance.UpdateLobbyAsync(joinedlobby.Id, new UpdateLobbyOptions
                {
                    Data = new Dictionary<string, DataObject> 
                    {
                        {KEY_START_GAME, new DataObject(DataObject.VisibilityOptions.Member, relayCode)}
                    }
                });

                joinedlobby = lobby;
                gameStarted = true;
            }
            catch (LobbyServiceException e)
            {
                Debug.Log(e);
            }
        }
    }

    public async void UpdatePlayerCharacter(string nameOfClass)
    {
        try
        {
            await LobbyService.Instance.UpdatePlayerAsync(joinedlobby.Id,
                AuthenticationService.Instance.PlayerId,
                new UpdatePlayerOptions
                {
                    Data = new Dictionary<string, PlayerDataObject>
                    {
                        {KEY_CHARACTER_TYPE, new PlayerDataObject(PlayerDataObject.VisibilityOptions.Member, nameOfClass) }
                    }
                });
        }
        catch (LobbyServiceException e)
        {
            Debug.Log(e);
        }
    }

    public async void ToggleReadyStatus(bool ready)
    {
        try
        {
            await LobbyService.Instance.UpdatePlayerAsync(joinedlobby.Id,
                AuthenticationService.Instance.PlayerId,
                new UpdatePlayerOptions
                {
                    Data = new Dictionary<string, PlayerDataObject>
                    {
                        {KEY_READY_STATUS, new PlayerDataObject(PlayerDataObject.VisibilityOptions.Member, ready.ToString()) }
                    }
                });
        }
        catch (LobbyServiceException e)
        {
            Debug.Log(e);
        }
    }

    public bool AreAllPlayersReady()
    {
        if (joinedlobby == null) return false;

        foreach (Player player in joinedlobby.Players)
        {
            if (player.Data[KEY_READY_STATUS].Value != "True")
            {
                return false;
            }
        }
        return true;
    }
}
