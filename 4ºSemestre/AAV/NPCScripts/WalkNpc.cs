using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WalkNpc : StateMachineBehaviour
{
    private NPC npc;
    private float distanceToWayPoint;

    // OnStateEnter is called when a transition starts and the state machine starts to evaluate this state
    override public void OnStateEnter(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        npc = animator.GetComponent<NPC>();
        npc.MoveToNextPoint();
        if (animator.GetComponent<NPC>().navMeshAgent.isStopped)
        {
            animator.GetComponent<NPC>().navMeshAgent.isStopped=false;
        }
    }

    // OnStateUpdate is called on each Update frame between OnStateEnter and OnStateExit callbacks
    override public void OnStateUpdate(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        distanceToWayPoint = (npc.transform.position - npc.waypoints[npc.currentTarget].position).magnitude;
        animator.SetFloat("DistWp", distanceToWayPoint);
    }

    // OnStateExit is called when a transition ends and the state machine finishes evaluating this state
    override public void OnStateExit(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    {
        animator.GetComponent<NPC>().navMeshAgent.isStopped = true;
    }

    // OnStateMove is called right after Animator.OnAnimatorMove()
    //override public void OnStateMove(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    //{
    //    // Implement code that processes and affects root motion
    //}

    // OnStateIK is called right after Animator.OnAnimatorIK()
    //override public void OnStateIK(Animator animator, AnimatorStateInfo stateInfo, int layerIndex)
    //{
    //    // Implement code that sets up animation IK (inverse kinematics)
    //}
}
