using System.Collections.Generic;
using Unity.Netcode;
using UnityEngine;
using UnityEngine.AI;

public class Spawner : NetworkBehaviour
{
    [SerializeField]
    private float spawnXmin;

    [SerializeField]
    private float spawnXmax;

    [SerializeField]
    private float spawnZmin;

    [SerializeField]
    private float spawnZmax;

    [SerializeField]
    private float xmin;

    [SerializeField]
    private float xmax;

    [SerializeField]
    private float zmin;

    [SerializeField]
    private float zmax;

    [SerializeField]
    private List<GameObject> enemiesPrefabs;

    [SerializeField]
    private GameObject plane;

    [SerializeField]
    private bool boss = false;

    [SerializeField]
    private GameObject bossPrefab;

    private int numberOfEnemies;

    private int level;

    private bool alreadySpawned = false;

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (!IsServer)
        {
            enabled = false;
            return;
        }

        int playerCount = GameManager.Instance.players.Count;
        level = GameManager.Instance.GetLevel();

        switch (playerCount)
        {
            case 1:
                numberOfEnemies = 4;
                break;
            case 2:
                numberOfEnemies = 6;
                break;
            case 3:
                numberOfEnemies = 8;
                break;
            default:
                numberOfEnemies = 4;
                break;
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void RequestSpawnEnemiesServerRpc()
    {
        SpawnEnemies();
    }

    private void SpawnEnemies()
    {
        if(alreadySpawned) return;

        alreadySpawned = true;

        if(plane != null)
        {
            Destroy(plane);
            plane.GetComponent<NetworkObject>().Despawn(true);
        }

        if (boss)
        {
            float x = Random.Range(spawnXmin, spawnXmax);
            float z = Random.Range(spawnZmin, spawnZmax);
            Vector3 spawnPosition = new Vector3(x, 0.1f, z);
            GameObject enemy = Instantiate(bossPrefab, spawnPosition, Quaternion.identity);
            enemy.GetComponent<NetworkObject>().Spawn();

            Boss boss = enemy.GetComponent<Boss>();

            if (boss != null)
            {
                int numberPlayers = GameManager.Instance.players.Count;

                boss.xmin = xmin;
                boss.xmax = xmax;
                boss.zmin = zmin;
                boss.zmax = zmax;

                int hp = 750 * numberPlayers; ;
                int damage = 30;
                int monney = 200 * numberPlayers;

                if (level == 5)
                {
                    hp = 500 * numberPlayers;
                    monney = 100 * numberPlayers;
                }
                
                boss.SetHp(hp);
                boss.SetDamage(damage);
                boss.SetMonney(monney);
            }
        }
        else
        {
            for (int i = 0; i < numberOfEnemies; i++)
            {
                int prefabIndex = i % enemiesPrefabs.Count;
                GameObject prefab = enemiesPrefabs[prefabIndex];

                float x = Random.Range(spawnXmin, spawnXmax);
                float z = Random.Range(spawnZmin, spawnZmax);

                Vector3 spawnPosition = new Vector3(x, 0.1f, z);

                GameObject enemy = Instantiate(prefab, spawnPosition, Quaternion.identity);
                enemy.GetComponent<NetworkObject>().Spawn();

                Skeleton skeleton = enemy.GetComponent<Skeleton>();

                if (skeleton != null)
                {
                    skeleton.xmin = xmin;
                    skeleton.xmax = xmax;
                    skeleton.zmin = zmin;
                    skeleton.zmax = zmax;

                    int hp = 100;
                    int damage = 5;
                    int monney = 15;

                    if (prefabIndex == 0)
                    {
                        hp = 100 + (20 * level);
                        damage = Mathf.Min(5 * level, 25);

                        int aux = Random.Range(15 * level, 25 * level);
                        monney = Mathf.Min(aux, 70);
                    }
                    else if (prefabIndex == 1)
                    {
                        hp = 130 + (20 * level);
                        damage = Mathf.Min(5 * level, 30);

                        int aux = Random.Range(20 * level, 30 * level);
                        monney = Mathf.Min(aux, 80);
                    }

                    skeleton.SetHp(hp);
                    skeleton.SetDamage(damage);
                    skeleton.SetMonney(monney);
                }
            }
        }
    }
}
