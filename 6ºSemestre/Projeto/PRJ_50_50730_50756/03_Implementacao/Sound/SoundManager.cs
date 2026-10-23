using Unity.Netcode;
using UnityEngine;
using UnityEngine.Audio;

public class SoundManager : MonoBehaviour
{
    public static SoundManager Instance { get; private set; }

    [SerializeField]
    private AudioClip menuMusic;

    [SerializeField]
    private AudioClip gameMusic;

    private AudioSource audioSource;

    [SerializeField]    
    private AudioMixer mixer;

    private
    void Awake()
    {
        if (Instance != null)
        {
            Destroy(gameObject);
            return;
        }

        Instance = this;
        DontDestroyOnLoad(gameObject);

        audioSource = GetComponent<AudioSource>();
        audioSource.loop = true;
    }

    public void PlayMenuMusic()
    {
        if (audioSource.clip != menuMusic)
        {
            audioSource.clip = menuMusic;
            audioSource.Play();
        }
    }

    public void PlayGameMusic()
    {
        if (audioSource.clip != gameMusic)
        {
            audioSource.clip = gameMusic;
            audioSource.Play();
        }
    }

    public void SetVolume(float volume)
    {
        mixer.SetFloat("Volume", volume);
    }

    public float GetCurrentValue()
    {
        float volume;
        if (mixer.GetFloat("Volume", out volume))
        {
            return volume;
        }
        else return 0f;
    }
}
