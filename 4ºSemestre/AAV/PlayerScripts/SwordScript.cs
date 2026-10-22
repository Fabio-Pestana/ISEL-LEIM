using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SwordScript : MonoBehaviour
{
    [SerializeField]
    private List<AudioClip> hittingClips;

    private int soundIdx;
    private AudioSource audioSource;
    public AudioSource playerSource;

    // Start is called before the first frame update
    void Start()
    {
        audioSource = GetComponent<AudioSource>();
    }

    private void OnTriggerEnter(Collider other)
    {

        if (other.gameObject.CompareTag("EnemySword"))
        {
            playerSource.Stop();
            soundIdx = (int)Mathf.Floor(Random.Range(0, hittingClips.Count));
            audioSource.PlayOneShot(hittingClips[soundIdx]);
        }
    }
}
