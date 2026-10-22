using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static UnityEngine.EventSystems.EventTrigger;

public class Player3P : MonoBehaviour
{
    [SerializeField]
    private int hp = 100;

    [SerializeField]
    private int money = 25;

    public float turningSpeed = 120.0f;

    [SerializeField]
    private GameObject swordHand;

    [SerializeField]
    private GameObject swordBack;

    [SerializeField]
    private GameObject swordRocks;

    [SerializeField]
    private GameObject heal_bottle;

    [SerializeField]
    private GameObject heal_bottle_body;

    [SerializeField]
    private AudioClip drawSword;

    [SerializeField]
    private AudioClip putSword;

    [SerializeField]
    private AudioClip moneySound;

    [SerializeField]
    private AudioClip moneySpend;

    [SerializeField]
    private AudioClip healingSound;

    [SerializeField]
    private AudioClip deathSound;

    [SerializeField]
    private ParticleSystem heal_Ps_normal;

    [SerializeField]
    private ParticleSystem heal_Ps_png;

    private AudioSource audioSource;

    private float turning;

    private Animator animator;
    private float moveX;
    private float moveY;

    private bool hasSword = false;
    private bool DrawSwordControll = false;
    private bool PutSwordControll = false;
    private bool closeToRiver = false;

    private bool onFire = false;
    private float damageInterval = 2.0f; // Intervalo de tempo entre danos
    private float damageTimer;

    private Rigidbody myRigidBody;

    private int enemiesKilled = 0;
    public bool isAttacking = false;

    [SerializeField]
    private List<AudioClip> swordClips;

    [SerializeField]
    private AudioSource mySword;

    private int soundIdx;

    private CapsuleCollider capsule;
    private float deffault_height;
    private float deff_y_capsule;


    public float roll_height;

    public float new_y_capsule;

    public bool isBlocking = false;

    private int numberHeals = 0;
    private bool canHeal = false;
    private bool canBuy = false;
    private bool death = false;
    private bool isMoneyBackClose = false;
    private bool isInSwordPlace = false;
    private GameObject moneyBag;
    private bool deathMenu = false;
    private bool endMenu = false;

    // Start is called before the first frame update
    void Start()
    {
        Cursor.lockState = CursorLockMode.Locked;
        myRigidBody = GetComponent<Rigidbody>();
        animator = GetComponent<Animator>();
        audioSource = GetComponent<AudioSource>();
        capsule = GetComponent<CapsuleCollider>();
        deffault_height = capsule.height;
        deff_y_capsule = capsule.center.y;

        swordHand.SetActive(false);
        swordBack.SetActive(false); 
        swordRocks.SetActive(true);
        heal_Ps_normal.Stop();
        heal_Ps_png.Stop();
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        if (!death)
        {
            turning = Input.GetAxis("Mouse X") * turningSpeed * Time.deltaTime;
            myRigidBody.MoveRotation(myRigidBody.rotation * Quaternion.Euler(0, turning, 0));
        }
    }
    
    // Update is called once per frame
    void Update()
    {
        if (!CanvasController.isPaused)
        {
            if (death)
            {
                animator.SetTrigger("dead");
                StartCoroutine(Retry());
            }
            else
            {

                moveX = Input.GetAxis("Horizontal");
                moveY = Input.GetAxis("Vertical");

                if (Input.GetKey(KeyCode.LeftShift))
                {
                    moveY *= 2.0f;
                }

                animator.SetFloat("X", moveX, 0.2f, Time.deltaTime);
                animator.SetFloat("Y", moveY, 0.2f, Time.deltaTime);


                if (Input.GetKey(KeyCode.Alpha1) && hasSword && !DrawSwordControll && !swordHand.activeSelf && !closeToRiver)
                {
                    animator.SetTrigger("Draw");
                    animator.SetBool("Weapon", true);
                    DrawSwordControll = true;
                    canHeal = false;
                }
                else if (Input.GetKey(KeyCode.Alpha2) && hasSword && !PutSwordControll && swordHand.activeSelf)
                {
                    animator.SetTrigger("Put");
                    animator.SetBool("Weapon", false);
                    PutSwordControll = true;
                    canHeal = false;
                }
                else if (Input.GetKey(KeyCode.Alpha3) && numberHeals > 0)
                {
                    if (hasSword && !PutSwordControll && swordHand.activeSelf)
                    {
                        animator.SetTrigger("Put");
                        animator.SetBool("Weapon", false);
                        PutSwordControll = true;
                    }

                    canHeal = true;
                }

                if (Input.GetKey(KeyCode.Q))
                {
                    animator.SetTrigger("Roll");
                    canHeal = false;
                }

                if (heal_bottle_body.activeSelf && numberHeals <= 0)
                {
                    heal_bottle_body.SetActive(false);
                }
                else if (numberHeals > 0 && !heal_bottle.activeSelf)
                {
                    heal_bottle_body.SetActive(true);
                }

                if (Input.GetButtonUp("Fire1"))
                {
                    if (swordHand.activeSelf)
                    {
                        animator.SetTrigger("Attack");
                    }
                    
                    if (canHeal && numberHeals>0)
                    {
                        animator.SetTrigger("Heal");
                        heal_bottle.SetActive(true);
                    }
                    else if(canHeal)
                    {
                        canHeal = false;
                    }

                }

                if (Input.GetButtonDown("Fire2") && hasSword && !isBlocking)
                {
                    animator.SetTrigger("Block");
                    animator.SetBool("isBlocking", true);
                    isBlocking = true;
                }
                else if (Input.GetButtonUp("Fire2") && hasSword && isBlocking)
                {
                    animator.SetBool("isBlocking", false);
                    isBlocking = false;
                }

                if (onFire)
                {
                    if (damageTimer < damageInterval)
                    {
                        damageTimer += Time.deltaTime; // Incrementa o temporizador com o tempo decorrido
                    }
                }

                if (canBuy)
                {
                    if (Input.GetKeyUp(KeyCode.E))
                    {
                        if (money >= 25)
                        {
                            money = money - 25;
                            numberHeals++;
                            audioSource.PlayOneShot(moneySpend);
                        }
                    }
                }

                if (isMoneyBackClose)
                {
                    if (Input.GetKeyUp(KeyCode.E))
                    {
                        money += Random.Range(10, 26);
                        Destroy(moneyBag);
                        audioSource.PlayOneShot(moneySound);
                        moneyBag = null;
                        isMoneyBackClose = false;
                    }
                }

                if (enemiesKilled >= 6)
                {
                    if (swordHand.activeSelf)
                    {
                        animator.SetTrigger("Put");
                        animator.SetBool("Weapon", false);
                        PutSwordControll = true;
                    }

                    animator.SetBool("End", true);
                }

                if (hp <= 0 && !death)
                {
                    death = true;
                    audioSource.PlayOneShot(deathSound);
                }
            }
        }
    }

    private void OnTriggerStay(Collider other)
    {
        if (other.gameObject.CompareTag("SwordPlace") && Input.GetKey(KeyCode.E) && !hasSword)
        {
            animator.SetBool("Weapon", true);
            swordHand.SetActive(true);
            swordRocks.SetActive(false);
            hasSword = true;
            isInSwordPlace = false;
        }

        if (other.gameObject.CompareTag("NPC") && Input.GetKey(KeyCode.E))
        {
            NPC p = other.gameObject.GetComponent<NPC>();
            p.StartTalking();
        }


        if (other.gameObject.CompareTag("Fire"))
        {
            // Verificar se o temporizador atingiu o intervalo de dano
            if (damageTimer >= damageInterval)
            {
                hp -= 1; // Reduzir o HP em 1 ponto
                damageTimer = 0f; // Reiniciar o temporizador de dano
            }
        }

        if (other.gameObject.CompareTag("RiverTrigger") && hasSword && !closeToRiver && swordHand.activeSelf)
        {
            animator.SetTrigger("Put");
            animator.SetBool("Weapon", false);
            PutSwordControll = true;
            closeToRiver = true;
        }
    }

    private void OnTriggerEnter(Collider other)
    {

        if (other.gameObject.CompareTag("Fire"))
        {
            onFire = true;
        }

        if (other.gameObject.CompareTag("moneyBag"))
        {
            moneyBag = other.gameObject;
            isMoneyBackClose = true;
        }

        if (other.gameObject.CompareTag("RiverTrigger") && hasSword && swordHand.activeSelf)
        {
            animator.SetTrigger("Put");
            animator.SetBool("Weapon", false);
            PutSwordControll = true;
            closeToRiver = true;
        }

        if (other.gameObject.CompareTag("Shop"))
        {
            SellerScript p = other.gameObject.GetComponent<SellerScript>();
            if(!canBuy)
            {
                canBuy = true;
                p.welcome();
            }
        }

        if (other.gameObject.CompareTag("EnemySword"))
        {
            SkeletonAi enemyScript = other.gameObject.GetComponentInParent<SkeletonAi>();
            if (enemyScript != null)
            {
                if (enemyScript.isAttacking)
                {
                    int damage = Random.Range(1, 6);
                    if (isBlocking)
                        damage = 0;

                    hp = hp - damage;
                }
            }
        }


        if (other.gameObject.CompareTag("SwordPlace") && !hasSword)
        {
            isInSwordPlace = true;
        }
    }

    private void OnTriggerExit(Collider other)
    {

        if (other.gameObject.CompareTag("Fire"))
        {
            onFire = false;
        }

        if (other.gameObject.CompareTag("RiverTrigger") && hasSword)
        {
            closeToRiver = false;
        }

        if (other.gameObject.CompareTag("moneyBag"))
        {
            moneyBag = null;
            isMoneyBackClose = false;
        }

        if (other.gameObject.CompareTag("Shop"))
        {
            canBuy = false;
        }

        if (other.gameObject.CompareTag("SwordPlace"))
        {
            isInSwordPlace = false;
        }

        if (other.gameObject.CompareTag("NPC"))
        {
            NPC p = other.gameObject.GetComponent<NPC>();
            p.Leave();
        }
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("River"))
        {
            animator.SetBool("Swim", true);
        }
    }

    private void OnCollisionExit(Collision collision)
    {
        if (collision.gameObject.CompareTag("River"))
        {
            animator.SetBool("Swim", false);
        }
    }

    void DrawSword()
    {
        swordBack.SetActive(false);
        swordHand.SetActive(true);
        animator.ResetTrigger("Draw");
        DrawSwordControll = false;
    }

    void AttackSword()
    {
        soundIdx = (int)Mathf.Floor(Random.Range(0, swordClips.Count));
        mySword.PlayOneShot(swordClips[soundIdx]);
        isAttacking = true;
        animator.ResetTrigger("Block");
        animator.ResetTrigger("Roll");
    }

    void PutSword()
    {
        swordHand.SetActive(false);
        swordBack.SetActive(true);
        animator.ResetTrigger("Put");
        PutSwordControll = false;
    }

    void Block()
    {
        animator.ResetTrigger("Block");
    }

    void Heal()
    {
        animator.ResetTrigger("Heal");
        numberHeals--;
        heal_bottle.SetActive(false);
        hp = hp + 20;
        if(hp>100) hp = 100;
        heal_Ps_normal.Play();
        heal_Ps_png.Play();
        audioSource.PlayOneShot(healingSound);
        StartCoroutine(StopAfterSeconds(2f));
    }

    void DrawSound()
    {
        audioSource.PlayOneShot(drawSword);
    }

    void PutSound()
    {
        audioSource.PlayOneShot(putSword);
    }

    public void IncrementEnemyKillCount()
    {
        enemiesKilled++;
    }

    public void NormalCapsule()
    {
        capsule.height = deffault_height;
        Vector3 newCenter = capsule.center;
        newCenter.y = deff_y_capsule;
        capsule.center = newCenter;
    }

    public void RollCapsule()
    {
        capsule.height = roll_height;
        Vector3 newCenter = capsule.center;
        newCenter.y = new_y_capsule;
        capsule.center = newCenter;
    }

    public void ResetAttack()
    {
        isAttacking = false;
    }

    private IEnumerator StopAfterSeconds(float seconds)
    {
        yield return new WaitForSeconds(seconds);
        heal_Ps_normal.Stop();
        heal_Ps_png.Stop();
    }

    public int getHp()
    {
        return hp;
    }

    public int getNumberHeals()
    {
        return numberHeals;
    }

    public int getMoney()
    {
        return money;
    }

    public bool isSwordActive()
    {
       return swordHand.activeSelf;
    }

    public bool isMoneyClose()
    {
        return isMoneyBackClose;
    }

    public bool isShopClose()
    {
        return canBuy;
    }

    public bool isSwordPlace()
    {
        return isInSwordPlace;
    }

    public bool IsDead()
    {
        return deathMenu;
    }

    public bool EndGame()
    {
        return endMenu;
    }

    public void End()
    {
        endMenu = true;
    }

    private IEnumerator Retry()
    {
        yield return new WaitForSeconds(4f);
        deathMenu = true;
    }
}
