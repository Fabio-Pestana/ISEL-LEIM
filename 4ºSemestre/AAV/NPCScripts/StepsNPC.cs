using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class StepsNPC : MonoBehaviour
{

    [SerializeField]
    private List<AudioClip> clipList;

    private int soundIdx;
    private AudioSource audioSource;

    // Start is called before the first frame update
    void Start()
    {
        audioSource = GetComponent<AudioSource>();
    }

    void PlayNPCstepSound()
    {
        if (!audioSource.isPlaying)
        {
            soundIdx = (int)Mathf.Floor(Random.Range(0, clipList.Count));
            audioSource.PlayOneShot(clipList[soundIdx]);
        }
    }
}
