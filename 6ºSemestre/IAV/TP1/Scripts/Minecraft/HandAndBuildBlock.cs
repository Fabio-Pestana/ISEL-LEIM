using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Classe responsável por criar blocos na mão do jogador (para assim o player
/// saber o bloco que tem selecionado).
/// </summary>
public class HandAndBuildBlock : MonoBehaviour
{
    enum Cubeside { BOTTOM, TOP, LEFT, RIGHT, FRONT, BACK };

    public Material material;
    public Block.BlockType bType;
    public Transform buildLocation;
    public Block b;

    /// <summary>
    /// Cria um quad para um lado específico do cubo.
    /// </summary>
    /// <param name="side">Lado do cubo a ser criado.</param>
    void Quad(Cubeside side)
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

        if (bType == Block.BlockType.GRASS && side == Cubeside.TOP)
        {
            uv00 = b.blockUVs[0, 0];
            uv10 = b.blockUVs[0, 1];
            uv01 = b.blockUVs[0, 2];
            uv11 = b.blockUVs[0, 3];
        }
        else if (bType == Block.BlockType.GRASS && side == Cubeside.BOTTOM)
        {
            uv00 = b.blockUVs[2, 0];
            uv10 = b.blockUVs[2, 1];
            uv01 = b.blockUVs[2, 2];
            uv11 = b.blockUVs[2, 3];
        }
        else
        {
            uv00 = b.blockUVs[(int) (bType + 1), 0];
            uv10 = b.blockUVs[(int)(bType + 1), 1];
            uv01 = b.blockUVs[(int)(bType + 1), 2];
            uv11 = b.blockUVs[(int)(bType + 1), 3];
        }




        Vector3[] vertices = new Vector3[4];
        Vector3[] normals = new Vector3[4];

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

        GameObject quad = new GameObject("quad");
        quad.transform.parent = this.gameObject.transform;

        MeshFilter mf = quad.AddComponent<MeshFilter>();
        mf.mesh = mesh;
    }


    /// <summary>
    /// Combina os quads do cubo em uma única mesh.
    /// </summary>
    void CombineQuads()
    {
        MeshFilter[] meshFilters = GetComponentsInChildren<MeshFilter>();
        CombineInstance[] combine = new CombineInstance[meshFilters.Length];

        int i = 0;
        while (i < meshFilters.Length)
        {
            if (meshFilters[i] != null && meshFilters[i].sharedMesh != null)
            {
                combine[i].mesh = meshFilters[i].sharedMesh;
                combine[i].transform = meshFilters[i].transform.localToWorldMatrix;
            }
            i++;
        }

        MeshFilter mf = this.gameObject.GetComponent<MeshFilter>();
        if (mf == null)
        {
            mf = this.gameObject.AddComponent<MeshFilter>();
        }

        mf.mesh = new Mesh();

        mf.mesh.CombineMeshes(combine);

        MeshRenderer renderer = this.gameObject.GetComponent<MeshRenderer>();
        if (renderer == null)
        {
            renderer = this.gameObject.AddComponent<MeshRenderer>();
        }

        renderer.material = material;

        foreach(Transform quad in this.transform)
        {
            if (quad != null)
            {
                Destroy(quad.gameObject);
            }
        }
    }

    /// <summary>
    /// Cria um cubo completo.
    /// </summary>
    void CreateCube()
    {
        Quad(Cubeside.TOP);
        Quad(Cubeside.FRONT);
        Quad(Cubeside.BOTTOM);
        Quad(Cubeside.BACK);
        Quad(Cubeside.LEFT);
        Quad(Cubeside.RIGHT);

        CombineQuads();

        // Define a posição do cubo com base no Transform fornecido (coloca na mão do player).
        if (buildLocation != null)
        {
            this.transform.position = buildLocation.position;
            this.transform.rotation = buildLocation.rotation;
            this.transform.localScale = buildLocation.localScale;
        }
    }

    /// <summary>
    /// Altera o tipo do bloco.
    /// </summary>
    /// <param name="newType">Novo tipo do bloco.</param>
    void ChangeBlockType(Block.BlockType newType)
    {
        bType = newType;
        CreateCube();
    }

    void Start()
    {
        b = new Block();
        CreateCube();
    }

    void Update()
    {
        // Verifica as teclas pressionadas
        if (Input.GetKeyDown(KeyCode.Alpha1)) // Tecla 1
        {
            ChangeBlockType(Block.BlockType.GRASS);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha2)) // Tecla 2
        {
            ChangeBlockType(Block.BlockType.DIRT);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha3)) // Tecla 3
        {
            ChangeBlockType(Block.BlockType.STONE);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha4)) // Tecla 4
        {
            ChangeBlockType(Block.BlockType.DIAMOND);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha5)) // Tecla 5
        {
            ChangeBlockType(Block.BlockType.COAL);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha6)) // Tecla 6
        {
            ChangeBlockType(Block.BlockType.IRON);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha7)) // Tecla 7
        {
            ChangeBlockType(Block.BlockType.COBBLESTONE);
        }
        else if (Input.GetKeyDown(KeyCode.Alpha8)) // Tecla 8
        {
            ChangeBlockType(Block.BlockType.TNT);
        }
    }
}
