using System.Collections;
using Unity.Netcode;
using UnityEngine;

public class DestroyParticles : NetworkBehaviour
{
    [SerializeField]
    private float _destroyDelay = 2f; 

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (IsServer)
        {
            // Contagem pra destruir so no servidor
            StartCoroutine(DestroyAfterDelay());
        }
    }

    private IEnumerator DestroyAfterDelay()
    {
        yield return new WaitForSeconds(_destroyDelay);

        GetComponent<NetworkObject>().Despawn();
        Destroy(gameObject);
    }
}
