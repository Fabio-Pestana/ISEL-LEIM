using UnityEngine;

public class ChaseSkeleton : StateMachineBehaviour
{
    private Skeleton skeleton;

    // OnStateEnter is called when a transition starts and the state machine starts to evaluate this state
    override public void OnStateEnter(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        skeleton = animator.GetComponent<Skeleton>();
        if (skeleton.player != null)
            skeleton.navMeshAgent.SetDestination(skeleton.player.transform.position);
    }

    // OnStateUpdate is called on each Update frame between OnStateEnter and OnStateExit callbacks
    override public void OnStateUpdate(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        if (skeleton.player != null)
            skeleton.navMeshAgent.SetDestination(skeleton.player.transform.position);
    }

    public override void OnStateExit(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        if (skeleton is Boss)
        {
            animator.SetFloat("Atck", Random.value);
        }
    }
}
