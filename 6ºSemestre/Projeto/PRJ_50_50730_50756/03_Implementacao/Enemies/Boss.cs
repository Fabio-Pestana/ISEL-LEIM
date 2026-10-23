using System.Collections;
using System.Text.RegularExpressions;
using UnityEngine;
using static UnityEngine.Rendering.DebugUI;

public class Boss : Skeleton
{
    private bool phase2 = false;

    private int phase2Hp;

    public override int GetDamage()
    {
        float multiplier = phase2 ? 1.5f : 1f;

        if (isAttacking)
            return (int) (damage * multiplier);
        else
            return 0;
    }

    public override void SetHp(int value)
    {
        base.SetHp(value);
        phase2Hp = Mathf.RoundToInt(value / 2);
    }

    public override void ReceiveDamage(int damage)
    {
        base.ReceiveDamage(damage);

        if (!phase2 && hp <= phase2Hp)
        {
            phase2 = true;
            _animator.SetBool("Phase2", phase2);
        }
    }

    public override IEnumerator DestroyAfterTime(float time)
    {
        isDead = true;
        capsule.enabled = false;
        GameManager.Instance.DistributeCoinsServerRPC(monney);
        GameManager.Instance.KilledBoss();

        yield return new WaitForSeconds(time);

        DestroyFromScene();
    }
}
