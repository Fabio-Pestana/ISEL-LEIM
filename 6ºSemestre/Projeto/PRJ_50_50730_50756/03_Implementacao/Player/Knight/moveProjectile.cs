using Unity.Netcode;
using UnityEngine;

public class moveProjectile : NetworkBehaviour
{
    [SerializeField] 
    private float speed = 10f;

    [SerializeField] 
    private float maxDistance = 12f;

    [SerializeField] 
    private GameObject impactEffectPrefab;

    [SerializeField] 
    private Transform _impactPoint;

    private Vector3 spawnPosition;

    private bool isDestroyed = false;

    [SerializeField]
    private int damage = 75;

    private bool destroyOnTouch = false;

    private bool fireBall = false;

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (IsServer)
        {
            spawnPosition = transform.position;
        }
    }

    void Update()
    {
        if (isDestroyed) return;

        transform.Translate(Vector3.forward * speed * Time.deltaTime);

        CheckDistance();
    }

    private void CheckDistance()
    {
        if (Vector3.Distance(spawnPosition, transform.position) >= maxDistance)
        {
            DestroyProjectile();
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (!IsServer) return;

        if (other.CompareTag("Wall"))
        {
            InstanciateHitParticlesServerRPC();
            DestroyProjectile();
        }
    }


    [ServerRpc(RequireOwnership = false)]
    private void InstanciateHitParticlesServerRPC()
    {
        GameObject hitParticles = Instantiate(
            impactEffectPrefab,
            _impactPoint.position,
            _impactPoint.rotation
        );

        hitParticles.transform.Rotate(0, 0, -90f);
        hitParticles.GetComponent<NetworkObject>().Spawn();
    }


    public void DestroyProjectile()
    {
        if (!IsServer || isDestroyed) return; // So o servidor pode destruir

        isDestroyed = true;
        GetComponent<NetworkObject>().Despawn();
        Destroy(gameObject);
    }

    public bool EnemyTouchFireBall()
    {
        if (fireBall)
        {
            InstanciateHitParticlesServerRPC();
            FireBallServerRPC();
            DestroyProjectile();
            return true;
        }

        InstanciateHitParticlesServerRPC();
        DestroyProjectile();
        return false;
    }

    [ServerRpc(RequireOwnership = false)]
    private void FireBallServerRPC()
    {
        float radius = 2f;
        Vector3 origin = transform.position;

        // Detectar colisores no raio
        Collider[] hitColliders = Physics.OverlapSphere(origin, radius);

        foreach (Collider collider in hitColliders)
        {
            Skeleton skeleton = collider.GetComponent<Skeleton>();
            if (skeleton != null)
            {
                skeleton.ReceiveDamage(damage);
            }
        }
    }

    public int GetDamage()
    {
        return damage;
    }

    public void SetDamage(int value)
    {
        damage = value;
    }

    public void SetDestroyOnTouch(bool value)
    {
        destroyOnTouch = value;
    }

    public bool IsDestroyedOnTouch()
    {
        return destroyOnTouch;
    }

    public void SetFireBall(bool value)
    {
        fireBall = value;
    }

    public bool IsFireBall()
    {
        return fireBall;
    }
}
