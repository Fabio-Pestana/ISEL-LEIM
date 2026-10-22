using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class StepsScript : MonoBehaviour
{
    
    [SerializeField]
    private List<AudioClip> clipList;

    private int soundIdx;
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

        isMoving = Mathf.Abs(moveY) > 0.1f || Mathf.Abs(moveX) > 0.1f;
    }

    void PlayStepSound()
    {
        if (isMoving && moveY <= 1 && moveY >= -2 && !audioSource.isPlaying) {
            soundIdx = (int)Mathf.Floor(Random.Range(0, clipList.Count));
            audioSource.PlayOneShot(clipList[soundIdx]);
        }
    }
    void PlayRunStepSound()
    {
        if (isMoving && moveY <= 2 && moveY > 1)
        {
            soundIdx = (int)Mathf.Floor(Random.Range(0, clipList.Count));
            audioSource.PlayOneShot(clipList[soundIdx]);
        }
    }
}
