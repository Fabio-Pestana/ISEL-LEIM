using UnityEngine;

/// <summary>
/// Classe que representa um chunk.
/// </summary>
public class Chunk
{
    public Block[,,] chunkdata;
    public GameObject goChunk;
    public enum ChunkStatus { DRAW, DONE};
    public ChunkStatus status;

    Material material;

    // probabilidades dos minérios
    public float probDiamond = 0.01f;
    public float probIron = 0.015f;
    public float probCoal = 0.03f;

    /// <summary>
    /// Construtor da classe Chunk.
    /// </summary>
    /// <param name="pos">Posição do chunk no mundo.</param>
    /// <param name="material">Material do chunk.</param>
    public Chunk(Vector3 pos, Material material)
    {
        goChunk = new GameObject(World.CreateChunkName(pos));
        goChunk.transform.position = pos;
        this.material = material;
        BuildChunk();
    }

    /// <summary>
    /// Constrói o chunk, preenchendo-o com blocos.
    /// </summary>
    void BuildChunk()
    {
        chunkdata = new Block[World.chunkSize, World.chunkSize, World.chunkSize];

        for (int z = 0; z < World.chunkSize; z++)
        {
            for (int y = 0; y < World.chunkSize; y++)
            {
                for (int x = 0; x < World.chunkSize; x++)
                {
                    Vector3 pos = new Vector3(x, y, z);
                    int worldX = (int) goChunk.transform.position.x + x;
                    int worldY = (int) goChunk.transform.position.y + y;
                    int worldZ = (int) goChunk.transform.position.z + z;

                    int h = Utils.GenerateHeight(worldX, worldZ);
                    int hs = Utils.GenerateStoneHeight(worldX, worldZ);

                    if(worldY <= hs)
                    {
                        // valor entre 0 e 1 que indica a probabilidade de ser 
                        // Stone ou Air
                        if(Utils.fBM3D(worldX, worldY , worldZ, 1, 0.5f) < 0.55f)
                        {
                            if(worldY <= hs - 20 && Random.Range(0f, 1f) <= probDiamond)
                            {
                                // Diamante
                                chunkdata[x, y, z] = new Block(Block.BlockType.DIAMOND, pos, this, material);
                            }else if(worldY <= hs - 10 && Random.Range(0f, 1f) <= probIron)
                            {
                                // Ferro
                                chunkdata[x, y, z] = new Block(Block.BlockType.IRON, pos, this, material);
                            }
                            else if (worldY <= hs - 5 && Random.Range(0f, 1f) <= probCoal)
                            {
                                // Carvão
                                chunkdata[x, y, z] = new Block(Block.BlockType.COAL, pos, this, material);
                            }
                            else
                            {
                                // Pedra
                                chunkdata[x, y, z] = new Block(Block.BlockType.STONE, pos, this, material);
                            }
                        }
                        else
                        {
                            // Para grutas
                            chunkdata[x, y, z] = new Block(Block.BlockType.AIR, pos, this, material);
                        }

                    }
                    else if (worldY == h)
                    {
                        // Relva à superficie
                        chunkdata[x, y, z] = new Block(Block.BlockType.GRASS, pos, this, material);
                    }
                    else if (worldY <= h)
                    {
                        // terra debaixo da relva
                        chunkdata[x, y, z] = new Block(Block.BlockType.DIRT, pos, this, material);
                    }else
                    {
                        chunkdata[x, y, z] = new Block(Block.BlockType.AIR, pos, this, material);
                    }
                }
            }

        }
        status = ChunkStatus.DRAW;
    }

    /// <summary>
    /// Desenha o chunk no mundo.
    /// </summary>
    public void DrawChunk()
    {

        for (int z = 0; z < World.chunkSize; z++)
            for (int y = 0; y < World.chunkSize; y++)
                for (int x = 0; x < World.chunkSize; x++)
                    chunkdata[x, y, z].Draw();
        CombineQuads();

        MeshCollider collider = goChunk.AddComponent<MeshCollider>();
        collider.sharedMesh = goChunk.GetComponent<MeshFilter>().mesh;
        status = ChunkStatus.DONE;
    }

    /// <summary>
    /// Combina os quads do chunk em uma única mesh.
    /// </summary>
    void CombineQuads()
    {
        //Combina-se as meshes
        //Vai a todos os filhos e aos MeshFilters e arruma-os num array
        MeshFilter[] meshFilters = goChunk.GetComponentsInChildren<MeshFilter>();
        CombineInstance[] combine = new CombineInstance[meshFilters.Length];

        int i = 0;
        while (i < meshFilters.Length)
        {
            combine[i].mesh = meshFilters[i].sharedMesh;
            // Colocá-los com as mesmas coordenadas
            combine[i].transform = meshFilters[i].transform.localToWorldMatrix;
            i++;
        }

        //Cria-se um meshFilter
        MeshFilter mf = goChunk.AddComponent<MeshFilter>();
        mf.mesh = new Mesh();

        //Mesh combinada
        mf.mesh.CombineMeshes(combine);

        //Render para o objeto
        MeshRenderer renderer = goChunk.AddComponent<MeshRenderer>();
        renderer.material = material;

        //Apaga todos os filhos que não foram combinados
        foreach (Transform quad in goChunk.transform)
        {
            GameObject.Destroy(quad.gameObject);
        }
    }
}
