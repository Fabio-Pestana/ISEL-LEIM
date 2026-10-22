using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SwimSound : MonoBehaviour
{

    [SerializeField]
    private AudioClip clip_swim;

    [SerializeField]
    private AudioClip clip_idle;

    private float moveY;
    private float moveX;
    private Animator animator;
    private AudioSource audioSource;
    private bool isMoving;

    // Start is called before the first frame update
    void Start()
    {
        animator = GetComponent<Animator>();
        audioSource = GetComponent<AudioSource>();
    }

    // Update is called once per frame
    void Update()
    {
        moveY = animator.GetFloat("Y");
        moveX = animator.GetFloat("X");

        isMoving = Mathf.Abs(moveY) > 0.3f || Mathf.Abs(moveX) > 0.3f;
    }

    void PlaySwimSound()
    {
        if (isMoving && !audioSource.isPlaying)
        {
            audioSource.PlayOneShot(clip_swim);
        }
    }

    void PlaySwimIDLESound()
    {
        if (!audioSource.isPlaying)
        {
            audioSource.PlayOneShot(clip_idle);
        }
    }
}

