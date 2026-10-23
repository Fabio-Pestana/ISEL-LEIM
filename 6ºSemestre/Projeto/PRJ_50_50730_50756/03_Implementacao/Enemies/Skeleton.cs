using UnityEngine;
using Unity.Netcode;
using UnityEngine.AI;
using System.Collections;
using NUnit.Framework;
using System.Linq;
using System.Collections.Generic;


public class Skeleton : NetworkBehaviour
{
    public Vector3 currentTarget;
    public NavMeshAgent navMeshAgent;
    public float xmin, xmax;
    public float zmin, zmax;

    public Transform player;

    private float _maxDistanceToCheck = 20.0f;
    protected Animator _animator;
    private IEnumerator courutineSaw;
    private RaycastHit hit;
    protected bool isDead = false;

    protected int monney;
    protected int hp;
    protected CapsuleCollider capsule;
    public bool isAttacking = false;

    [SerializeField]
    protected int damage = 5;

    [SerializeField]
    private bool combo = false;

    private void Awake()
    {
        navMeshAgent = GetComponent<NavMeshAgent>();
        _animator = GetComponent<Animator>();
        capsule = GetComponent<CapsuleCollider>();
        currentTarget = transform.position;
        courutineSaw = Saw();
        StartCoroutine(courutineSaw);
    }

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (!IsServer)
        {
            enabled = false;
            return;
        }
    }

    IEnumerator Saw()
    {
        while (true)
        {

            yield return new WaitForSeconds(0.2f);

            //true se acertar em um collider
            if (Physics.SphereCast(transform.position + Vector3.up, 0.5f, transform.forward, out hit, _maxDistanceToCheck))
            {
                if (hit.transform.CompareTag("Player"))
                {
                    // Obter a posicao do player e usar o metodo IsPositionReachable
                    Vector3 playerPos = hit.transform.position;
                    if (playerPos.x >= xmin && playerPos.x <= xmax &&
                        playerPos.z >= zmin && playerPos.z <= zmax &&
                        IsPositionReachable(playerPos))
                    {
                        _animator.SetBool("Saw", true);
                    }
                }
                else
                {
                    _animator.SetBool("Saw", false);
                }
            }
            else
            {
                _animator.SetBool("Saw", false);
            }
        }
    }

    void Update()
    {
        if (hp <= 0 && !isDead)
        {
            _animator.SetBool("isDead", true);
            navMeshAgent.isStopped = true;
            StartCoroutine(DestroyAfterTime(5f));
        }

        if(hp > 0)
            UpdateDistantPlayer();
    }

    private void UpdateDistantPlayer()
    {
        player = null;
        float closestDistance = Mathf.Infinity;
        bool playerReachable = false;

        if (GameManager.Instance.players.Count == 0)
            return;

        foreach (GameObject p in GameManager.Instance.players)
        {
            if (p == null) continue;

            Vector3 playerPos = p.transform.position;

            bool inArea = playerPos.x >= xmin && playerPos.x <= xmax &&
                    playerPos.z >= zmin && playerPos.z <= zmax;

            bool reachable = false;
            if (inArea)
            {
                reachable = IsPositionReachable(playerPos);
            }

            if (inArea && reachable)
            {
                float sqrDistance = (transform.position - playerPos).magnitude;

                if (sqrDistance < closestDistance)
                {
                    closestDistance = sqrDistance;
                    player = p.transform;
                    playerReachable = true;
                }
            }
        }

        if (playerReachable)
        {
            _animator.SetFloat("DistPlayer", closestDistance);
        } else
        {
            player = null;
            _animator.SetFloat("DistPlayer", 100);
            _animator.SetBool("Saw", false);
        }
    }

    
    public void MoveToNextPoint()
    {
        int attempts = 0;
        const int maxAttempts = 30;
        bool pointFound = false;

        while (!pointFound && attempts < maxAttempts)
        {
            attempts++;

            float x = Random.Range(xmin, xmax);
            float z = Random.Range(zmin, zmax);
            currentTarget = new Vector3(x, 1, z);

            if(IsPositionReachable(currentTarget))
            {
                pointFound = true;
                break;
            }
        }
        
        if(!pointFound)
        {
            currentTarget = new Vector3(xmin, 1, zmin);
        }
        navMeshAgent.SetDestination(currentTarget);
    }

    private bool IsPositionReachable(Vector3 position)
    {
        NavMeshPath path = new NavMeshPath();
        if (navMeshAgent.CalculatePath(position, path))
        {
            return path.status == NavMeshPathStatus.PathComplete;
        }
        return false;
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("Weapon"))
        {
            // Obtem o CharacterBase do objeto pai da arma
            CharacterBase character = other.GetComponentInParent<CharacterBase>();
            if (character != null)
            {
                int damage = character.WeaponDamage();
                if (damage > 0)
                {
                    Debug.Log("player dano:" + damage);
                    ReceiveDamage(damage);
                }
            }
        }

        if (other.gameObject.CompareTag("Projectile"))
        {
            moveProjectile aux = other.GetComponent<moveProjectile>();
            int value = aux.GetDamage();

            if (aux.IsDestroyedOnTouch())
            {
                if(!aux.EnemyTouchFireBall())
                {
                    ReceiveDamage(value);
                }
            }
            else
            {
                ReceiveDamage(value);
            }
                
            Debug.Log("player dano:" + value);
        }
    }

    public virtual IEnumerator DestroyAfterTime(float time)
    {
        isDead = true;
        capsule.enabled = false;
        GameManager.Instance.DistributeCoinsServerRPC(monney);
        GameManager.Instance.EnemyKilled();

        yield return new WaitForSeconds(time);

        DestroyFromScene();
    }

    public void DestroyFromScene()
    {
        Destroy(gameObject);
        GetComponent<NetworkObject>().Despawn(true);
    }

    public void Attack()
    {
        isAttacking = true;
    }

    public void ResetAttack()
    {
        isAttacking = false;

        if (combo)
        {
            bool doCombo = Random.value > 0.5f;
            _animator.SetBool("Combo", doCombo);
        }
    }

    public virtual void ReceiveDamage(int damage)
    {
        hp = hp - damage;
    }

    public virtual int GetDamage()
    {
        if (isAttacking)
            return damage;
        else 
            return 0;
    }

    public virtual void SetHp(int value)
    {
        hp = value;
    }

    public void SetDamage(int value)
    {
        damage = value;
    }

    public void SetMonney(int value)
    {
        monney = value;
    }
}
