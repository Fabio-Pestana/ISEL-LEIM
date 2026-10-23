using UnityEngine;

public class WalkSkeleton : StateMachineBehaviour
{
    private Skeleton skeleton;
    private float distanceWP;

    // OnStateEnter is called when a transition starts and the state machine starts to evaluate this state
    override public void OnStateEnter(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        skeleton = animator.GetComponent<Skeleton>();
        skeleton.MoveToNextPoint();
    }

    // OnStateUpdate is called on each Update frame between OnStateEnter and OnStateExit callbacks
    override public void OnStateUpdate(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        distanceWP = (skeleton.transform.position - skeleton.currentTarget).magnitude;
        animator.SetFloat("DistPoint", distanceWP);
    }
}
