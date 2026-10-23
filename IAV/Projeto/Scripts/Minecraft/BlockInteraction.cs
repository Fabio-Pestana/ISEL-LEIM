using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Classe responsável por lidar com as interações do player com os blocos
/// (criar e destruir blocos).
/// </summary>
public class BlockInteraction : MonoBehaviour
{
    public Camera cam;
    enum InteractionType { DESTROY, BUILD};
    InteractionType interactionType;

    public HandAndBuildBlock quad;

    private TouchOSCReceiver receiver;

    [SerializeField]
    private Animator animator;

    void Start()
    {
        receiver = quad.receiver;
    }

    // Update is called once per frame
    void Update()
    {
        bool interaction = receiver.Interaction();

        if (interaction)
        {
            animator.SetTrigger("Hit");

            interactionType = receiver.DestroyBlock() ? InteractionType.DESTROY : InteractionType.BUILD;

            RaycastHit hit;
            if(Physics.Raycast(cam.transform.position, cam.transform.forward, out hit, 8))
            {
                string chunkName = hit.collider.gameObject.name;
                float chunkX = hit.collider.gameObject.transform.position.x;
                float chunkY = hit.collider.gameObject.transform.position.y;
                float chunkZ = hit.collider.gameObject.transform.position.z;

                Vector3 hitBlock;
                if(interactionType == InteractionType.DESTROY)
                {
                    hitBlock = hit.point - hit.normal / 2f;
                }
                else 
                {
                    hitBlock = hit.point + hit.normal / 2f;
                }

                int blockX = (int) (Mathf.Round(hitBlock.x) - chunkX);
                int blockY = (int) (Mathf.Round(hitBlock.y) - chunkY);
                int blockZ = (int) (Mathf.Round(hitBlock.z) - chunkZ);

                if (blockX < 0 || blockX >= World.chunkSize || blockY < 0 || blockY >= World.chunkSize ||
                blockZ < 0 || blockZ >= World.chunkSize)
                {
                    if (blockX < 0)
                    {
                        chunkX -= World.chunkSize;
                        blockX = World.chunkSize - 1;
                    }else if (blockX >= World.chunkSize)
                    {
                        chunkX += World.chunkSize;
                        blockX = 0;
                    }

                    if (blockY < 0)
                    {
                        chunkY -= World.chunkSize;
                        blockY = World.chunkSize - 1;
                    }else if (blockY >= World.chunkSize)
                    {
                        chunkY += World.chunkSize;
                        blockY = 0;
                    }

                    if (blockZ < 0)
                    {
                        chunkZ -= World.chunkSize;
                        blockZ = World.chunkSize - 1;
                    }else if (blockZ >= World.chunkSize)
                    {
                        chunkZ += World.chunkSize;
                        blockZ = 0;
                    }

                    chunkName = World.CreateChunkName(new Vector3(chunkX, chunkY, chunkZ));
                }

                Chunk c;
                if(World.chunkDict.TryGetValue(chunkName, out c))
                {
                    //Debug.Log(c.chunkdata[blockX, blockY, blockZ].bType);

                    if(interactionType == InteractionType.DESTROY)
                    {
                        c.chunkdata[blockX, blockY, blockZ].SetType(Block.BlockType.AIR);
                    }
                    else if(interactionType == InteractionType.BUILD)
                    {
                        c.chunkdata[blockX, blockY, blockZ].SetType(quad.bType);
                    }
                }

                List<string> updates = new List<string>();
                updates.Add(chunkName);

                if (blockX == 0)
                    updates.Add(World.CreateChunkName(new Vector3(chunkX - World.chunkSize, chunkY, chunkZ)));
                if (blockX == World.chunkSize-1)
                    updates.Add(World.CreateChunkName(new Vector3(chunkX + World.chunkSize, chunkY, chunkZ)));

                if (blockY == 0)
                    updates.Add(World.CreateChunkName(new Vector3(chunkX, chunkY - World.chunkSize, chunkZ)));
                if (blockY == World.chunkSize - 1)
                    updates.Add(World.CreateChunkName(new Vector3(chunkX, chunkY + World.chunkSize, chunkZ)));

                if (blockZ == 0)
                    updates.Add(World.CreateChunkName(new Vector3(chunkX, chunkY, chunkZ - World.chunkSize)));
                if (blockZ == World.chunkSize - 1)
                    updates.Add(World.CreateChunkName(new Vector3(chunkX, chunkY, chunkZ + World.chunkSize)));

                foreach(string update in updates)
                {
                    if (World.chunkDict.TryGetValue(update, out c))
                    {
                        DestroyImmediate(c.goChunk.GetComponent<MeshFilter>());
                        DestroyImmediate(c.goChunk.GetComponent<MeshRenderer>());
                        DestroyImmediate(c.goChunk.GetComponent<MeshCollider>());
                        c.DrawChunk();
                    }
                }
            }
        }
    }
}
