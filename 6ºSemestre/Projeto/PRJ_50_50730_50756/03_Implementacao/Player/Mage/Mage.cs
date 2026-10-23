using Unity.Netcode;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.Windows;
using static UnityEngine.UI.Image;
using System.Collections;

public class Mage : CharacterBase
{
    private bool skill = false;
    private bool ultimate = false;
    private GameObject _attackActive;
    private GameObject _skill1Active;
    private GameObject _skill2Active;

    [SerializeField]
    private int _damageAtck2 = 100;

    [SerializeField]
    private int _damageAtck3 = 150;

    [SerializeField]
    private int _damageUlti = 300;

    [SerializeField]
    private GameObject _attck1;
    [SerializeField]
    private GameObject _attck2;
    [SerializeField]
    private GameObject _attck3;
    [SerializeField]
    private ParticleSystem _ultimatePrefab;

    private Vector3 _worldMousePosition;

    protected override void Awake()
    {
        base.Awake();
        _attackActive = null;
    }

    
    void FixedUpdate()
    {
        if (!IsOwner) return;

        ReadGamepad();

        if (ultimate || skill)
        {
            _direction = Vector2.zero;
        }

        _direction = _direction.normalized;

        MovementAndAnimation(_direction, !ultimate);
    }

    // Input System Events
    public void OnAttack(InputAction.CallbackContext context)
    {
        if (context.performed)
        {
            _animator.SetTrigger("Attack");   
        }
    }

    public void OnAttackMobile()
    {
        _animator.SetTrigger("Attack");
    }

    public void OnAttack2(InputAction.CallbackContext context)
    {
        if (context.performed && !hability2OnCooldown && playerLevel.Value >= 2)
        {
            _animator.SetTrigger("Habilidade1");
            skill = true;
            StartCoroutine(Hability2CooldownRoutine());
        }
    }

    public void OnAttack2Mobile()
    {
        if (!hability2OnCooldown && playerLevel.Value >= 2)
        {
            _animator.SetTrigger("Habilidade1");
            skill = true;
            StartCoroutine(Hability2CooldownRoutine());
        }
    }

    public void OnAttack3(InputAction.CallbackContext context)
    {
        if (context.performed && !hability3OnCooldown && playerLevel.Value >= 3)
        {
            _animator.SetTrigger("Habilidade2");
            skill = true;

            Ray ray = _camera.ScreenPointToRay(_mousePosition);
            RaycastHit hit;

            if (Physics.Raycast(ray, out hit, 100f))
            {
                _worldMousePosition = hit.point;
            }
            StartCoroutine(Hability3CooldownRoutine());
        }
    }

    public void OnAttack3Mobile()
    {
        if (!hability3OnCooldown && playerLevel.Value >= 3)
        {
            Vector3 defaultPosition = transform.position + transform.forward * 5f;
            _worldMousePosition = defaultPosition;
            _animator.SetTrigger("Habilidade2");
            skill = true;
            StartCoroutine(Hability3CooldownRoutine());
            MageSkill2ServerRPC(_worldMousePosition);
        }
    }

    public void OnAttack4(InputAction.CallbackContext context)
    {
        if (context.performed && !hability4OnCooldown && playerLevel.Value >= 4)
        {
            _animator.SetTrigger("Habilidade3");
            ultimate = true;
            StartCoroutine(Hability4CooldownRoutine());

        }
    }

    public void OnAttack4Mobile()
    {
        if (!hability4OnCooldown && playerLevel.Value >= 4) 
        {
            _animator.SetTrigger("Habilidade3");
            ultimate = true;
            StartCoroutine(Hability4CooldownRoutine());
        }
    }

    // Animator Eventos
    public void CanMoveSkill()
    {
        skill = false;
        _animator.ResetTrigger("Habilidade1");
        _animator.ResetTrigger("Habilidade2");
    }
    
    public void CanMoveUltimate()
    {
        ultimate = false;
        _animator.ResetTrigger("Habilidade3");
    }

    public void EnableAttack()
    {
        if (!IsOwner) return;

        MageAttackServerRPC();
    }

    public void EnableSkill1()
    {
        if (!IsOwner) return;

        MageSkill1ServerRPC();
    }

    public void EnableSkill2()
    {
        if (!IsOwner) return;
        if(Application.isMobilePlatform) return;

        MageSkill2ServerRPC(_worldMousePosition);
    }

    // Server RPCs

    [ServerRpc(RequireOwnership = false)]
    private void MageAttackServerRPC()
    {
        if (_attck1 == null) return;

        Vector3 spawnPosition = transform.position + transform.forward * 1f;
        spawnPosition.y = transform.position.y + 1.2f;

        _attackActive = Instantiate(
            _attck1,
            spawnPosition,
            transform.rotation
        );

        NetworkObject netObj = _attackActive.GetComponent<NetworkObject>();
        netObj.Spawn();

        // Dano buff
        moveProjectile projectileScript = _attackActive.GetComponent<moveProjectile>();
        if (projectileScript != null)
        {
            float multiplier = isBuffed.Value ? 2f : 1f;
            projectileScript.SetDamage(Mathf.RoundToInt(weaponDamage * multiplier));
            projectileScript.SetDestroyOnTouch(true);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void MageSkill1ServerRPC()
    {
        if (_attck2 == null) return;

        Vector3 spawnPosition = transform.position + transform.forward * 1f;
        spawnPosition.y = transform.position.y + 1.2f;

        _skill1Active = Instantiate(
            _attck2,
            spawnPosition,
            transform.rotation
        );

        NetworkObject netObj = _skill1Active.GetComponent<NetworkObject>();
        netObj.Spawn();

        // Dano buff
        moveProjectile projectileScript = _skill1Active.GetComponent<moveProjectile>();
        if (projectileScript != null)
        {
            float multiplier = isBuffed.Value ? 2f : 1f;
            projectileScript.SetDamage(Mathf.RoundToInt(_damageAtck2 * multiplier));
            projectileScript.SetDestroyOnTouch(true);
            projectileScript.SetFireBall(true);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void MageSkill2ServerRPC(Vector3 worldPos)
    {
        if (_attck3 == null) return;

        Vector3 spawnPosition = worldPos;
        spawnPosition.y = transform.position.y + 0.1f;

        _skill2Active = Instantiate(
            _attck3,
            spawnPosition,
            Quaternion.identity
        );

        _skill2Active.GetComponent<NetworkObject>().Spawn();

        float radius = 6f;
        Collider[] hitColliders = Physics.OverlapSphere(spawnPosition, radius);

        foreach (Collider collider in hitColliders)
        {
            Skeleton skeleton = collider.GetComponent<Skeleton>();
            if (skeleton != null)
            {
                float multiplier = isBuffed.Value ? 2f : 1f;
                int aux = Mathf.RoundToInt(_damageAtck3 * multiplier);

                float distance = Vector3.Distance(spawnPosition, collider.transform.position);
                float falloff = 1f - Mathf.Clamp01(distance / radius); // entre 1 (centro) e 0 (borda)

                int damage = Mathf.RoundToInt(aux * falloff);

                if (damage < 40)
                    damage = 40;

                Debug.Log("Dano: " + damage);
                skeleton.ReceiveDamage(damage);
            }
        }
    }


    [ServerRpc(RequireOwnership = false)]
    private void MageUltimateServerRPC()
    {
        float radius = 3f;
        Vector3 origin = transform.position;

        // Detectar colisores no raio
        Collider[] hitColliders = Physics.OverlapSphere(origin, radius);

        foreach (Collider collider in hitColliders)
        {
            Skeleton skeleton = collider.GetComponent<Skeleton>();
            if (skeleton != null)
            {
                // Aplicar dano
                float multiplier = isBuffed.Value ? 2f : 1f;
                skeleton.ReceiveDamage(Mathf.RoundToInt(_damageUlti * multiplier));
                Debug.Log("Dano: " + Mathf.RoundToInt(_damageUlti * multiplier));
            }
        }
    }

    // Particle Systems
    public void EnableUltimate()
    {

        if (_ultimatePrefab != null)
        {
            _ultimatePrefab.Play();

            if (!IsOwner) return;

            MageUltimateServerRPC();
        }
    }
}