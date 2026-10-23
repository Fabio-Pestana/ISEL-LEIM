using System.Collections;
using Unity.Netcode;
using UnityEngine;
using UnityEngine.InputSystem;

public class Knight : CharacterBase
{
    private bool spin = false;
    private bool ultimate = false;
    private GameObject _ultimateActive;
    private bool cancelBlock = false;

    [SerializeField]
    private int _damageAtck3 = 100;

    [SerializeField]
    private int _damageUlti = 200;

    [SerializeField]
    private ParticleSystem _attck1;

    [SerializeField]
    private ParticleSystem _attck2;

    [SerializeField]
    private ParticleSystem _attck3;

    [SerializeField]
    private GameObject _ultimatePrefab;

    protected override void Awake()
    {
        base.Awake();
        _ultimateActive = null;
    }

    void FixedUpdate()
    {
        if (!IsOwner) return;

        ReadGamepad();

        if (ultimate || spin)
        {
            _direction = Vector2.zero;
        }
        
        _direction = _direction.normalized;

        MovementAndAnimation(_direction, !spin);
    }

    private void Update()
    {
        if (!IsOwner) return;

        if(hits.Value > 5)
        {
            cancelBlock = true;
            hits.Value = 0;
            blocking = false;
            _animator.SetBool("isBlocking", false);
            StartCoroutine(Hability2CooldownRoutine());
        }
    }

    public void OnAttack(InputAction.CallbackContext context)
    {
        if (context.performed && !blocking && !spin && !ultimate)
        {
            _animator.SetTrigger("Attack");   
        }
    }

    public void OnAttackMobile()
    {
        if (!blocking && !spin && !ultimate)
        {
            _animator.SetTrigger("Attack");
        }
    }

    public void OnBlock(InputAction.CallbackContext context)
    {
        if (context.started && !hability2OnCooldown && playerLevel.Value >= 2)
        {
            blocking = true;
            _animator.SetTrigger("Block");
            _animator.SetBool("isBlocking", true);
        }

        if (context.canceled && !cancelBlock && playerLevel.Value >= 2)
        {
            blocking = false;
            hits.Value = 0;
            _animator.SetBool("isBlocking", false);
            StartCoroutine(Hability2CooldownRoutine());
        }else if (context.canceled) cancelBlock = false;
    }

    public void OnBlockMobileStart()
    {
        if (!hability2OnCooldown && playerLevel.Value >= 2)
        {
            blocking = true;
            _animator.SetTrigger("Block");
            _animator.SetBool("isBlocking", true);
        }
    }

    public void OnBlockMobileEnd()
    {
        if (!cancelBlock)
        {
            blocking = false;
            hits.Value = 0;
            _animator.SetBool("isBlocking", false);
            StartCoroutine(Hability2CooldownRoutine());
        }else cancelBlock = false;
    }

    public void OnAttack3(InputAction.CallbackContext context)
    {
        if (context.performed && !blocking && !hability3OnCooldown && playerLevel.Value >= 3)
        {
            spin = true;
            _animator.SetTrigger("Habilidade3");
            StartCoroutine(Hability3CooldownRoutine());
        }
    }

    public void OnAttack3Mobile()
    {
        if (!blocking && !hability3OnCooldown && playerLevel.Value >= 3)
        {
            spin = true;
            _animator.SetTrigger("Habilidade3");
            StartCoroutine(Hability3CooldownRoutine());
        }
    }

    public void OnAttack4(InputAction.CallbackContext context)
    {
        if (context.performed && !blocking && !hability4OnCooldown && playerLevel.Value >= 4)
        {
            _animator.SetTrigger("Habilidade4");
            ultimate = true;
            StartCoroutine(Hability4CooldownRoutine());
        }
    }

    public void OnAttack4Mobile()
    {
        if (!blocking && !hability4OnCooldown && playerLevel.Value >= 4)
        {
            _animator.SetTrigger("Habilidade4");
            ultimate = true;
            StartCoroutine(Hability4CooldownRoutine());
        }
    }

    // Animator Eventos

    public void Hability3Spin()
    {
        spin = false;
        _animator.ResetTrigger("Habilidade3");
    }

    public void ResetUltimate()
    {
        if (!IsOwner) return;

        KnightUltimateServerRPC();
    }

    public void CanMoveUltimate()
    {
        ultimate = false;
        _animator.ResetTrigger("Habilidade4");
    }

    public void ResetBlock()
    {
        _animator.ResetTrigger("Block");
    }

    // Server RPCs
    [ServerRpc(RequireOwnership = false)]
    private void KnightUltimateServerRPC()
    {
        if (_ultimatePrefab == null) return;

        Vector3 spawnPosition = transform.position + transform.forward;
        spawnPosition.y = 2f;

        _ultimateActive = Instantiate(
            _ultimatePrefab,
            spawnPosition,
            transform.rotation
        );

        NetworkObject netObj = _ultimateActive.GetComponent<NetworkObject>();
        netObj.Spawn();

        // Dano segundo buff
        moveProjectile projectileScript = _ultimateActive.GetComponent<moveProjectile>();
        if (projectileScript != null)
        {
            float multiplier = isBuffed.Value ? 2f : 1f;
            projectileScript.SetDamage(Mathf.RoundToInt(_damageUlti * multiplier));
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void KnightAttck3ServerRPC()
    {
        float radius = 3f;
        Vector3 origin = transform.position;

        // detectar colisores no raio
        Collider[] hitColliders = Physics.OverlapSphere(origin, radius);

        foreach (Collider collider in hitColliders)
        {
            Skeleton skeleton = collider.GetComponent<Skeleton>();
            if (skeleton != null)
            {
                // Aplica dano
                float multiplier = isBuffed.Value ? 2f : 1f;
                skeleton.ReceiveDamage(Mathf.RoundToInt(_damageAtck3 * multiplier));
            }
        }
    }

    // Particle Systems
    public void EnableAttack1Slash()
    {
        if (_attck1 != null)
        {
            _attck1.Play();
        }
        ChangeIsAttackingServerRpc(true);
    }

    public void DisableAttack1Slash()
    {
        if (_attck1 != null)
        {
            _attck1.Stop();
        }
        ChangeIsAttackingServerRpc(false);
    }

    public void EnableAttack2Slash()
    {
        if (_attck2 != null)
        {
            _attck2.Play();
        }
        ChangeIsAttackingServerRpc(true);
    }

    public void DisableAttack2Slash()
    {

        if (_attck2 != null)
        {
            _attck2.Stop();
        }
        ChangeIsAttackingServerRpc(false);
    }

    public void EnableAttack3()
    {

        if (_attck3 != null)
        {
            _attck3.Play();

            if (!IsOwner) return;

            KnightAttck3ServerRPC();
        }
    }
}