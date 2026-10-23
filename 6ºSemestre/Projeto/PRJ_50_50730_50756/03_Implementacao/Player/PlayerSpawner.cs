using System.Collections;
using Unity.Netcode;
using UnityEngine;

public class PlayerSpawner : MonoBehaviour
{
    [Header("Child Character Models")]
    [SerializeField]
    private GameObject knightModel;

    [SerializeField]
    private GameObject mageModel;

    [SerializeField]
    private GameObject clericModel;

    void Start()
    {
        if (NetworkManager.Singleton != null)
        {
            NetworkManager.Singleton.OnClientConnectedCallback += OnClientConnected;
        }
    }

    private void OnClientConnected(ulong clientId)
    {
        if (NetworkManager.Singleton.IsServer)
        {
            StartCoroutine(WaitForPlayerObject(clientId));
        }
    }


    private IEnumerator WaitForPlayerObject(ulong clientId)
    {
        yield return null;

        // Obtem o NetworkObject do jogador que acabou de se conectar
        NetworkObject playerNetworkObject = NetworkManager.Singleton.ConnectedClients[clientId].PlayerObject;

        if (playerNetworkObject != null)
        {
            // Obtem o componente que contem a variavel de rede
            PlayerData playerData = playerNetworkObject.GetComponent<PlayerData>();

            if (playerData != null)
            {
                SpawnPlayerPrefab(clientId, playerData.selectedCharacter.Value);
            }
        }
    }

    private void SpawnPlayerPrefab(ulong clientId, int characterType)
    {
        // Encontra o prefab correto baseado no characterType
        GameObject prefabToSpawn = SetCharacter(characterType);

        if (prefabToSpawn != null)
        {
            GameObject[] spawnPoints = GameObject.FindGameObjectsWithTag("SpawnPoint");

            if (spawnPoints == null || spawnPoints.Length == 0)
            {
                Debug.LogError("No spawn points found!");
                return;
            }

            Transform spawnPoint = spawnPoints[Random.Range(0, spawnPoints.Length)].transform;

            GameObject playerInstance = Instantiate(
                prefabToSpawn,
                spawnPoint.position,
                spawnPoint.rotation
            );

            NetworkObject networkObject = playerInstance.GetComponent<NetworkObject>();

            // Spawna o objeto e atribui a ownership ao cliente
            networkObject.SpawnWithOwnership(clientId);
        }
    }

    public GameObject SetCharacter(int characterIdx)
    {
        if (characterIdx == 0) return knightModel;
        if (characterIdx == 1) return mageModel;
        
        return clericModel;
    }

    private void OnDestroy()
    {
        if (NetworkManager.Singleton != null)
        {
            NetworkManager.Singleton.OnClientConnectedCallback -= OnClientConnected;
        }
    }
}
