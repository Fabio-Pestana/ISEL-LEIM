using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class SellerScript : MonoBehaviour
{
    public GameObject player;
    private Animator animator;
    private Vector3 _distToPlayer;
    public float condition = 3f;
    private float rotationSpeed = 5.0f;

    // Start is called before the first frame update
    void Start()
    {
        animator = GetComponent<Animator>();
    }

    // Update is called once per frame
    void Update()
    {
        _distToPlayer = player.transform.position - transform.position;
        if (_distToPlayer.magnitude <= condition)
        {
            _distToPlayer.y = 0;

            Quaternion targetRotation = Quaternion.LookRotation(_distToPlayer);

            transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, rotationSpeed * Time.deltaTime);
        }
    }

    public void welcome()
    {
        animator.SetTrigger("HiPlayer");
    }
}
