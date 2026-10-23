using System.Collections;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Runtime.CompilerServices;
using UnityEngine;
using System;

/// <summary>
/// Classe principal que gera o mundo.
/// </summary>
public class World : MonoBehaviour
{
    public GameObject player;
    public Material material;

    public static int chunkSize = 16;
    public static int radius = 3;
    public static ConcurrentDictionary<string, Chunk> chunkDict;
    public static List<string> toRemove = new List<string>();

    Vector3 lastBuildPos;
    bool drawing;

    /// <summary>
    /// Cria o nome do chunk com base na posição.
    /// </summary>
    /// <param name="v">Posição do chunk.</param>
    /// <returns>Nome do chunk.</returns>
    public static string CreateChunkName(Vector3 v)
    {
        return (int)v.x + " " + (int)v.y + " " + (int)v.z;
    }

    /// <summary>
    /// Constrói o mundo.
    /// </summary>
    /// <param name="chunkPos">Posição do chunk.</param>
    /// <param name="rad">Raio da construção.</param>
    IEnumerator BuildRecursiveWorld(Vector3 chunkPos, int rad)
    {
        int x = (int) chunkPos.x;
        int y = (int) chunkPos.y;
        int z = (int) chunkPos.z;

        BuildChunkAt(chunkPos);
        yield return null;

        if (--rad < 0) yield break;

        Building(new Vector3(x, y, z + chunkSize), rad);
        yield return null;

        Building(new Vector3(x, y, z - chunkSize), rad);
        yield return null;

        Building(new Vector3(x + chunkSize, y, z), rad);
        yield return null;

        Building(new Vector3(x - chunkSize, y, z), rad); 
        yield return null;

        Building(new Vector3(x, y + chunkSize, z), rad); 
        yield return null;

        Building(new Vector3(x, y - chunkSize, z), rad);
        yield return null;
    }

    /// <summary>
    /// Constrói um chunk em uma posição específica.
    /// </summary>
    /// <param name="chunkPos">Posição do chunk.</param>
    void BuildChunkAt(Vector3 chunkPos)
    {
        string name = CreateChunkName(chunkPos);
        Chunk c;
        if (!chunkDict.TryGetValue(name, out c))
        {
            c = new Chunk(chunkPos, material);
            c.goChunk.transform.parent = this.transform;
            chunkDict.TryAdd(c.goChunk.name, c);
        }
    }

    /// <summary>
    /// Remove chunks que estão fora do raio do player.
    /// </summary>
    IEnumerator RemoveChunks()
    {
        for(int i = 0; i < toRemove.Count; i++)
        {
            string name = toRemove[i];
            Chunk c;
            if(chunkDict.TryGetValue(name, out c))
            {
                Destroy(c.goChunk);
                chunkDict.TryRemove(name, out c);
                yield return null;
            }
        }
    }

    /// <summary>
    /// Desenha os chunks no mundo.
    /// </summary>
    IEnumerator DrawChunks()
    {
        drawing = true;
        toRemove.Clear();
        foreach (KeyValuePair<string, Chunk> c in chunkDict)
        {
            if(c.Value.status == Chunk.ChunkStatus.DRAW)
            {
                c.Value.DrawChunk();
                yield return null;
            } 
            if(c.Value.goChunk && 
                Vector3.Distance(player.transform.position, c.Value.goChunk.transform.position) > chunkSize * radius)
            {
                toRemove.Add(c.Key);
            }
        }
        StartCoroutine(RemoveChunks());
        drawing = false;
    }

    /// <summary>
    /// Inicia a coroutine que desenha os chunks.
    /// </summary>
    void Drawing()
    {
        StartCoroutine(DrawChunks());
    }

    /// <summary>
    /// Inicia a coroutine que constrói o mundo.
    /// </summary>
    /// <param name="chunkPos">Posição do chunk.</param>
    /// <param name="rad">Raio de construção.</param>
    void Building(Vector3 chunkPos, int rad)
    {
        StartCoroutine(BuildRecursiveWorld(chunkPos, rad));
    }

    /// <summary>
    /// Determina em qual chunk umas certas coordenadas estão.
    /// </summary>
    /// <param name="position">Posição no mundo.</param>
    /// <returns>Posição do chunk.</returns>
    Vector3 WhichChunk(Vector3 position)
    {
        Vector3 chunkPos = new Vector3();
        chunkPos.x = Mathf.Floor(position.x / chunkSize) * chunkSize;
        chunkPos.y = Mathf.Floor(position.y / chunkSize) * chunkSize;
        chunkPos.z = Mathf.Floor(position.z / chunkSize) * chunkSize;
        return chunkPos;
    }

    // Start is called before the first frame update
    void Start()
    {
        chunkDict = new ConcurrentDictionary<string, Chunk>();
        this.transform.position = Vector3.zero;
        this.transform.rotation = Quaternion.identity;

        Vector3 ppos = player.transform.position;
        player.transform.position = new Vector3(ppos.x, Utils.GenerateHeight(ppos.x, ppos.z) + 1, ppos.z);

        lastBuildPos = player.transform.position;
        Building(WhichChunk(lastBuildPos), radius);
        Drawing();
    }

    private void Update()
    {
        Vector3 movement = player.transform.position - lastBuildPos;
        if (movement.magnitude > chunkSize)
        {
            lastBuildPos = player.transform.position;
            Building(WhichChunk(lastBuildPos), radius);
            Drawing();
        }
        if (!drawing) Drawing();
    }
}
