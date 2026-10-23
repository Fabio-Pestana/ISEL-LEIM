using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Representa um bloco no mundo do jogo.
/// </summary>
public class Block
{
    /// <summary>
    /// Define os lados do cubo.
    /// </summary>
    enum Cubeside { BOTTOM, TOP, LEFT, RIGHT, FRONT, BACK };


    /// <summary>
    /// Tipos de bloco disponíveis.
    /// </summary>
    public enum BlockType { GRASS, DIRT, STONE, DIAMOND, COAL, IRON, COBBLESTONE, TNT, AIR};

    Material material;
    public BlockType bType;
    Chunk owner;
    Vector3 pos;
    bool isSolid;


    // Coordenadas do canto inferior esquerdo dos blocos do atlas
    static Vector2 GrassSide_LBC = new Vector2(3f, 15f) / 16;
    static Vector2 GrassTOP_LBC = new Vector2(1f, 6f) / 16;
    static Vector2 Dirt_LBC = new Vector2(2f, 15f) / 16;
    static Vector2 Stone_LBC = new Vector2(1f, 15f) / 16;
    static Vector2 Diamond_LBC = new Vector2(2f, 12f) / 16;
    static Vector2 Coal_LBC = new Vector2(2f, 13f) / 16;
    static Vector2 Iron_LBC = new Vector2(1f, 13f) / 16;
    static Vector2 Cobblestone_LBC = new Vector2(0f, 14f) / 16;
    static Vector2 TntTOP_LBC = new Vector2(9f, 15f) / 16;
    static Vector2 TntSide_LBC = new Vector2(8f, 15f) / 16;
    static Vector2 TntBottom_LBC = new Vector2(10f, 15f) / 16;


    /// <summary>
    /// Matriz que contém as coordenadas UV para cada tipo de bloco.
    /// </summary>
    public Vector2[,] blockUVs = {
        /* Grass Top */ { GrassTOP_LBC, GrassTOP_LBC + new Vector2(1f, 0f)/16,
                            GrassTOP_LBC + new Vector2(0f, 1f)/16, GrassTOP_LBC + new Vector2(1f, 1f)/16 },

        /* Grass Side */ { GrassSide_LBC, GrassSide_LBC + new Vector2(1f, 0f)/16,
                            GrassSide_LBC + new Vector2(0f, 1f)/16, GrassSide_LBC + new Vector2(1f, 1f)/16 },

        /* Dirt */ { Dirt_LBC, Dirt_LBC + new Vector2(1f, 0f)/16,
                            Dirt_LBC + new Vector2(0f, 1f)/16, Dirt_LBC + new Vector2(1f, 1f)/16 },

        /* Stone */ { Stone_LBC, Stone_LBC + new Vector2(1f, 0f)/16,
                            Stone_LBC + new Vector2(0f, 1f)/16, Stone_LBC + new Vector2(1f, 1f)/16 },

        /* Diamond */ { Diamond_LBC, Diamond_LBC + new Vector2(1f, 0f)/16,
                            Diamond_LBC + new Vector2(0f, 1f)/16, Diamond_LBC + new Vector2(1f, 1f)/16 },
        
        /* Coal */ { Coal_LBC, Coal_LBC + new Vector2(1f, 0f)/16,
                            Coal_LBC + new Vector2(0f, 1f)/16, Coal_LBC + new Vector2(1f, 1f)/16 },
        
        /* Iron */ { Iron_LBC, Iron_LBC + new Vector2(1f, 0f)/16,
                            Iron_LBC + new Vector2(0f, 1f)/16, Iron_LBC + new Vector2(1f, 1f)/16 },
        
        /* Cobblestone */ { Cobblestone_LBC, Cobblestone_LBC + new Vector2(1f, 0f)/16,
                            Cobblestone_LBC + new Vector2(0f, 1f)/16, Cobblestone_LBC + new Vector2(1f, 1f)/16 },

        /* TNT Side */ { TntSide_LBC, TntSide_LBC + new Vector2(1f, 0f)/16,
                            TntSide_LBC + new Vector2(0f, 1f)/16, TntSide_LBC + new Vector2(1f, 1f)/16 },

        /* TNT Top */ { TntTOP_LBC, TntTOP_LBC + new Vector2(1f, 0f)/16,
                            TntTOP_LBC + new Vector2(0f, 1f)/16, TntTOP_LBC + new Vector2(1f, 1f)/16 },

        /* TNT Bottom */ { TntBottom_LBC, TntBottom_LBC + new Vector2(1f, 0f)/16,
                            TntBottom_LBC + new Vector2(0f, 1f)/16, TntBottom_LBC + new Vector2(1f, 1f)/16 }
    };

    /// <summary>
    /// Construtor da classe Block.
    /// </summary>
    /// <param name="bType">Tipo do bloco.</param>
    /// <param name="pos">Posição do bloco.</param>
    /// <param name="owner">Chunk ao qual o bloco pertence.</param>
    /// <param name="material">Material do bloco.</param>
    public Block(BlockType bType, Vector3 pos, Chunk owner, Material material)
    {
        this.owner = owner;
        this.pos = pos;
        this.material = material;
        SetType(bType);
    }

    /// <summary>
    /// Construtor da classe Block (usado na classe HandAndBuildBlock
    /// para criar um cubo na mão do player).
    /// </summary>
    public Block()
    {
        this.owner = null;
    }

    /// <summary>
    /// Define o tipo do bloco.
    /// </summary>
    /// <param name="blockType">Tipo do bloco.</param>
    public void SetType(BlockType blockType)
    {
        this.bType = blockType;

        if (bType == BlockType.AIR)
            isSolid = false;
        else
            isSolid = true;
    }

    /// <summary>
    /// Cria um quad para um lado específico do cubo.
    /// </summary>
    /// <param name="side">Lado do cubo a ser criado.</param>
    void CreateQuad(Cubeside side)
    {
        Mesh mesh = new Mesh();

        Vector3 v0 = new Vector3(-0.5f, -0.5f, 0.5f);
        Vector3 v1 = new Vector3(0.5f, -0.5f, 0.5f);
        Vector3 v2 = new Vector3(0.5f, -0.5f, -0.5f);
        Vector3 v3 = new Vector3(-0.5f, -0.5f, -0.5f);
        Vector3 v4 = new Vector3(-0.5f, 0.5f, 0.5f);
        Vector3 v5 = new Vector3(0.5f, 0.5f, 0.5f);
        Vector3 v6 = new Vector3(0.5f, 0.5f, -0.5f);
        Vector3 v7 = new Vector3(-0.5f, 0.5f, -0.5f);

        Vector2 uv00 = new Vector2(0, 0);
        Vector2 uv01 = new Vector2(0, 1);
        Vector2 uv10 = new Vector2(1, 0);
        Vector2 uv11 = new Vector2(1, 1);

        if (bType == BlockType.GRASS && side == Cubeside.TOP)
        {
            uv00 = blockUVs[0, 0];
            uv10 = blockUVs[0, 1];
            uv01 = blockUVs[0, 2];
            uv11 = blockUVs[0, 3];
        }
        else if (bType == BlockType.GRASS && side == Cubeside.BOTTOM)
        {
            uv00 = blockUVs[2, 0];
            uv10 = blockUVs[2, 1];
            uv01 = blockUVs[2, 2];
            uv11 = blockUVs[2, 3];
        }
        else if (bType == BlockType.TNT && side == Cubeside.TOP)
        {
            uv00 = blockUVs[9, 0];
            uv10 = blockUVs[9, 1];
            uv01 = blockUVs[9, 2];
            uv11 = blockUVs[9, 3];
        }
        else if (bType == BlockType.TNT && side == Cubeside.BOTTOM)
        {
            uv00 = blockUVs[10, 0];
            uv10 = blockUVs[10, 1];
            uv01 = blockUVs[10, 2];
            uv11 = blockUVs[10, 3];
        }
        else
        {
            uv00 = blockUVs[(int) (bType + 1), 0];
            uv10 = blockUVs[(int)(bType + 1), 1];
            uv01 = blockUVs[(int)(bType + 1), 2];
            uv11 = blockUVs[(int)(bType + 1), 3];
        }




        Vector3[] vertices = new Vector3[4];
        Vector3[] normals = new Vector3[4];
        //int[] triangles = new int[6];
        //Vector2[] uv = new Vector2[4];

        switch(side)
        {
            case Cubeside.FRONT:
                vertices = new Vector3[] { v4, v5, v1, v0 };
                normals = new Vector3[] { Vector3.forward, Vector3.forward, Vector3.forward, Vector3.forward };
                break;

            case Cubeside.BACK:
                vertices = new Vector3[] { v6, v7, v3, v2 };
                normals = new Vector3[] { Vector3.back, Vector3.back, Vector3.back, Vector3.back };
                break;

            case Cubeside.LEFT:
                vertices = new Vector3[] { v7, v4, v0, v3 };
                normals = new Vector3[] { Vector3.left, Vector3.left, Vector3.left, Vector3.left };
                break;

            case Cubeside.RIGHT:
                vertices = new Vector3[] { v5, v6, v2, v1 };
                normals = new Vector3[] { Vector3.right, Vector3.right, Vector3.right, Vector3.right };
                break;

            case Cubeside.TOP:
                vertices = new Vector3[] { v7, v6, v5, v4 };
                normals = new Vector3[] { Vector3.up, Vector3.up, Vector3.up, Vector3.up };
                break;

            case Cubeside.BOTTOM:
                vertices = new Vector3[] { v0, v1, v2, v3 };
                normals = new Vector3[] { Vector3.down, Vector3.down, Vector3.down, Vector3.down };
                break;
        }

        int[] triangles = new int[] { 3, 1, 0, 3, 2, 1 };
        Vector2[] uv = new Vector2[] { uv11, uv01, uv00, uv10 };

        mesh.vertices = vertices;  
        mesh.normals = normals;
        mesh.triangles = triangles;
        mesh.uv = uv;

        mesh.RecalculateBounds();

        GameObject quad = new GameObject("quad");
        quad.transform.position = this.pos;
        quad.transform.parent = owner.goChunk.transform;

        MeshFilter mf = quad.AddComponent<MeshFilter>();
        mf.mesh = mesh;
    }

    /// <summary>
    /// Converte um índice global para um índice local dentro do chunk.
    /// </summary>
    /// <param name="i">Índice global.</param>
    /// <returns>Índice local.</returns>
    int ConvertToLocalIndex(int i)
    {
        if (i == -1)
            return World.chunkSize-1;
        if (i == World.chunkSize) 
            return 0;
        return i;
    }

    /// <summary>
    /// Verifica se um bloco vizinho é sólido.
    /// </summary>
    /// <param name="x">Coordenada x do bloco vizinho.</param>
    /// <param name="y">Coordenada y do bloco vizinho.</param>
    /// <param name="z">Coordenada z do bloco vizinho.</param>
    /// <returns>True se o bloco vizinho for sólido, False caso contrário.</returns>
    bool HasSolidNeighbour(int x, int y, int z)
    {
        Block[,,] chunkdata;

        if (x < 0 || x >= World.chunkSize || y < 0 || y >= World.chunkSize || z < 0 || z >= World.chunkSize)
        {
            Vector3 neighChunkPos = owner.goChunk.transform.position + new Vector3(
                    (x - (int)pos.x) * World.chunkSize,
                    (y - (int)pos.y) * World.chunkSize,
                    (z - (int)pos.z) * World.chunkSize);

            string chunkName = World.CreateChunkName(neighChunkPos);

            x = ConvertToLocalIndex(x);
            y = ConvertToLocalIndex(y);
            z = ConvertToLocalIndex(z);


            Chunk neighChunk;
            if(World.chunkDict.TryGetValue(chunkName, out neighChunk))
            {
                chunkdata = neighChunk.chunkdata;
            }
            else
            {
                return false;
            }
        }
        else
        {
            chunkdata = owner.chunkdata;
        }

        try
        {
            return chunkdata[x, y, z].isSolid;
        }
        catch (System.IndexOutOfRangeException ex) { }

        return false;

    }

    /// <summary>
    /// Desenha o bloco no mundo.
    /// </summary>
    public void Draw()
    {
        if(bType == BlockType.AIR) 
            return;

        if(!HasSolidNeighbour( (int) pos.x - 1, (int) pos.y, (int) pos.z))
            CreateQuad(Cubeside.LEFT);

        if (!HasSolidNeighbour((int) pos.x + 1, (int) pos.y, (int) pos.z))
            CreateQuad(Cubeside.RIGHT);

        if (!HasSolidNeighbour((int) pos.x, (int) pos.y - 1, (int) pos.z))
            CreateQuad(Cubeside.BOTTOM);

        if (!HasSolidNeighbour((int) pos.x, (int) pos.y + 1, (int) pos.z))
            CreateQuad(Cubeside.TOP);

        if (!HasSolidNeighbour((int) pos.x, (int) pos.y, (int) pos.z - 1))
            CreateQuad(Cubeside.BACK);

        if (!HasSolidNeighbour((int) pos.x, (int) pos.y, (int) pos.z + 1))
            CreateQuad(Cubeside.FRONT);
    }
}
