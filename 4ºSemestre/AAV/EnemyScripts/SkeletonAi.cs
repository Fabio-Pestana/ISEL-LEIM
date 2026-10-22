using System.Collections;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.UI;

public class SkeletonAi : MonoBehaviour
{
    
    public GameObject player;
    private Player3P player3P;
    public Vector3 currentTarget;
    public NavMeshAgent navMeshAgent;
    public float xmin, xmax;
    public float zmin, zmax;

    private float _maxDistanceToCheck = 30.0f; //convenção em C# "_"
    private Animator _animator;
    private Vector3 _distToPlayer;
    private IEnumerator courutineSaw;
    private RaycastHit hit;
    private bool isDead = false;

    [SerializeField]
    private int hpMax = 100;
    [SerializeField]
    private int hpMin = 60;

    private int hp;
    private CapsuleCollider capsule;
    public bool isAttacking = false;

    [SerializeField]
    private Slider healthBar;



    // Start is called before the first frame update
    void Start()
    {
        navMeshAgent = GetComponent<NavMeshAgent>();
        _animator = GetComponent<Animator>();
        player3P = player.GetComponent<Player3P>();
        capsule = GetComponent<CapsuleCollider>();
        currentTarget = transform.position;
        hp = Random.Range(hpMin, hpMax + 1);
        healthBar.maxValue = hp;
        healthBar.value = hp;

        courutineSaw = Saw();
        StartCoroutine(courutineSaw);
    }

    IEnumerator Saw()
    {
        while (true)
        {

            yield return new WaitForSeconds(0.2f);

            //Debug.DrawRay(transform.position + Vector3.up, transform.forward * _maxDistanceToCheck, Color.green);

            //true se acertar em um collider
            if (Physics.SphereCast(transform.position + Vector3.up, 0.5f, transform.forward, out hit, _maxDistanceToCheck))
            {
                if (hit.transform.CompareTag("Player"))
                {
                    _animator.SetBool("Saw", true);
                    //Debug.DrawRay(transform.position + Vector3.up, transform.forward * _maxDistanceToCheck, Color.red);

                }
                else
                {
                    _animator.SetBool("Saw", false);
                    //Debug.DrawRay(transform.position + Vector3.up, transform.forward * _maxDistanceToCheck, Color.green);

                }
            }
            else
            {
                _animator.SetBool("Saw", false);
               // Debug.DrawRay(transform.position + Vector3.up, transform.forward * _maxDistanceToCheck, Color.green);
            }
        }
        //yield return null;
    }

    // Update is called once per frame
    void Update()
    {
        if (hp <= 0 && !isDead)
        {
            _animator.SetBool("isDead", true);
            healthBar.gameObject.SetActive(false);
            StartCoroutine(DestroyAfterTime(5f));
        }

        _distToPlayer = transform.position - player.transform.position;
        _animator.SetFloat("DistPlayer", _distToPlayer.magnitude);

        if(_distToPlayer.magnitude <= 15)
        {
            healthBar.gameObject.SetActive(true);
        }
        else if(healthBar.gameObject.activeSelf)
        {
            healthBar.gameObject.SetActive(false);
        }
    }

    public void MoveToNextPoint()
    {
        float x = Random.Range(xmin, xmax);
        float z = Random.Range(zmin, zmax);
        currentTarget = new Vector3(x, 5, z);
        navMeshAgent.SetDestination(currentTarget);
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("RiverTrigger"))
        {
            _animator.SetBool("Water", true);
        }

        if (other.gameObject.CompareTag("PlayerSword") && player3P.isAttacking && !player3P.isBlocking)
        {
            hp = hp - 10;
            healthBar.value = hp;
            if (hp <= 0)
                healthBar.value = 0;
        }
    }

    IEnumerator DestroyAfterTime(float time)
    {
        isDead = true;
        capsule.enabled = false;
        yield return new WaitForSeconds(time);
        player3P.IncrementEnemyKillCount();
        Destroy(gameObject); 
    }

    public void Attack()
    {
        isAttacking = true;
    }

    public void ResetAttack()
    {
        isAttacking = false;
    }
}
