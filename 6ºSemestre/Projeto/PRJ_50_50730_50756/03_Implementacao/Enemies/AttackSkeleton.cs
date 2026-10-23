using UnityEngine;

public class AttackSkeleton : StateMachineBehaviour
{
    private Skeleton skeleton;
    private Transform playerTransform;
    private Transform enemyTransform;
    private float rotationSpeed = 5.0f;

    // OnStateEnter is called when a transition starts and the state machine starts to evaluate this state
    override public void OnStateEnter(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        skeleton = animator.GetComponent<Skeleton>();
        if (skeleton.player != null)
            playerTransform = skeleton.player;
        enemyTransform = animator.transform;
    }

    // OnStateUpdate is called on each Update frame between OnStateEnter and OnStateExit callbacks
    override public void OnStateUpdate(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {

        if (!skeleton.IsServer)
            return;


        Vector3 direction = playerTransform.position - enemyTransform.position;
        direction.y = 0;

        Quaternion targetRotation = Quaternion.LookRotation(direction);

        enemyTransform.rotation = Quaternion.Slerp(enemyTransform.rotation, targetRotation, rotationSpeed * Time.deltaTime);
    }
}
