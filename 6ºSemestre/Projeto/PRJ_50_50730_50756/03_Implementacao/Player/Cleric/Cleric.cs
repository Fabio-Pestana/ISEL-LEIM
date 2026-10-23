using System;
using System.Collections;
using Unity.Netcode;
using UnityEngine;
using UnityEngine.InputSystem;

public class Cleric : CharacterBase
{
    private bool _noMove = false;

    [SerializeField]
    private ParticleSystem attck1;

    [SerializeField]
    private ParticleSystem heal;

    [SerializeField]
    private ParticleSystem buff;

    [SerializeField]
    private ParticleSystem revivePs;

    void FixedUpdate()
    {
        if (!IsOwner) return;

        ReadGamepad();

        if (_noMove)
        {
            _direction = Vector2.zero;
        }

        _direction = _direction.normalized;

        MovementAndAnimation(_direction, !_noMove);
    }

    // Input System Events

    public void OnAttack(InputAction.CallbackContext context)
    {
        if (context.performed && !_noMove)
        {
            _animator.SetTrigger("Attack");
        }
    }

    public void OnAttackMobile()
    {
        _animator.SetTrigger("Attack");
    }


    public void Healing(InputAction.CallbackContext context)
    {
        if (context.performed && !_noMove && !hability2OnCooldown && playerLevel.Value >= 2)
        {
            _animator.SetTrigger("Healing");
            _noMove = true;
            StartCoroutine(Hability2CooldownRoutine());
        }
    }

    public void HealingMobile()
    {
        if (!_noMove && !hability2OnCooldown && playerLevel.Value >= 2)
        {
            _animator.SetTrigger("Healing");
            _noMove = true;
            StartCoroutine(Hability2CooldownRoutine());
        }
    }

    public void Buff(InputAction.CallbackContext context)
    {
        if (context.performed && !_noMove && !hability3OnCooldown && playerLevel.Value >= 3)
        {
            _animator.SetTrigger("Buff");
            _noMove = true;
            StartCoroutine(Hability3CooldownRoutine());
        }
    }

    public void BuffMobile()
    {
        if (!_noMove && !hability3OnCooldown && playerLevel.Value >= 3)
        {
            _animator.SetTrigger("Buff");
            _noMove = true;
            StartCoroutine(Hability3CooldownRoutine());
        }
    }

    public void Revive(InputAction.CallbackContext context)
    {
        if (context.performed && !_noMove && !hability4OnCooldown && playerLevel.Value >= 4)
        {
            _noMove = true;
            _animator.SetTrigger("Habilidade4");
            StartCoroutine(Hability4CooldownRoutine());
        }
    }

    public void ReviveMobile()
    {
        if (!_noMove && !hability4OnCooldown && playerLevel.Value >= 4)
        {
            _noMove = true;
            _animator.SetTrigger("Habilidade4");
            StartCoroutine(Hability4CooldownRoutine());
        }
    }

    public void ResetAttack()
    {
        if (attck1 != null)
        {
            attck1.Stop();
        }
        _animator.ResetTrigger("Attack");
        ChangeIsAttackingServerRpc(false);
    }

    public void CanMoveUltimate()
    {
        CanMove();
        _animator.ResetTrigger("Habilidade4");
    }

    public void EnableAttack1Slash()
    {
        if (attck1 != null)
        {
            attck1.Play();
            ChangeIsAttackingServerRpc(true);
        }
    }

    public void HealOrBuff(int check)
    {
        if (check == 1)
        {
            heal.Play();
            _animator.ResetTrigger("Healing");

            if (!IsOwner) return;

            AreaHealServerRpc(transform.position, 4f);
        }
        else
        {
            buff.Play();
            _animator.ResetTrigger("Buff");

            if (!IsOwner) return;

            AreaBuffServerRpc(transform.position, 4f);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void AreaHealServerRpc(Vector3 origin, float radius)
    {
        Collider[] hits = Physics.OverlapSphere(origin, radius);

        foreach (Collider hit in hits)
        {
            CharacterBase character = hit.GetComponentInParent<CharacterBase>();
            if (character != null)
            {
                character.Heal();
            }
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void AreaBuffServerRpc(Vector3 origin, float radius)
    {
        Collider[] hits = Physics.OverlapSphere(origin, radius);

        foreach (Collider hit in hits)
        {
            CharacterBase character = hit.GetComponentInParent<CharacterBase>();
            if (character != null)
            {
                character.ActivateBuffServerRpc();
            }
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void ReviveAllPlayersServerRpc()
    {
        foreach (GameObject client in GameManager.Instance.players)
        {
            CharacterBase character = client.GetComponent<CharacterBase>();
            if (character != null && !character.isAlive.Value) 
            {
                character.revive.Value = true;
            }
        }
    }


    public void CanMove()
    {
        _noMove = false;
    }

    public void ReviveParticleSystem()
    {
        revivePs.Play();

        if (!IsOwner) return;

        ReviveAllPlayersServerRpc();
    }
}
