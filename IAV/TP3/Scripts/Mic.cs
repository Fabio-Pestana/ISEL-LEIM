using System.Collections;
using UnityEngine;

public class Mic : MonoBehaviour
{
    AudioSource audioSource;
    public AudioClip audioClip;
    public bool useMicrophone;
    public string deviceName;
    public bool cubes;

    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        audioSource = GetComponent<AudioSource>();
        if (useMicrophone)
        {
            if(Microphone.devices.Length > 0)
            {
                deviceName = Microphone.devices[0];
                audioSource.clip = Microphone.Start(deviceName, true, 10, AudioSettings.outputSampleRate);
                // Debug.Log(AudioSettings.outputSampleRate);
                while (Microphone.GetPosition(deviceName) < 48 * 30);
            }
            else
            {
                useMicrophone = false;
            }
        }

        if(!useMicrophone) 
        { 
            audioSource.clip = audioClip;
        }

        audioSource.loop = true;
        audioSource.Play();
        if (cubes)
        {
            SetCubes();
            StartCoroutine(Interact());
        }
        
    }

    /*
    void Update()
    {
        float energy = AudioAnalysis.MeanEnergy(audioSource);
        if(AudioAnalysis.ConvertToDB(energy) > 60)
        {
            Debug.Log(AudioAnalysis.ComputeSpectrumPeak(audioSource, true));
        }
    }
    */

    void SetCubes()
    {
        for(int i = 0; i < 12; i++)
        {
            GameObject cube = GameObject.CreatePrimitive(PrimitiveType.Cube);
            cube.transform.Rotate(new Vector3(0, 180f / 12f * i, 0));
            cube.transform.position += cube.transform.forward * 15;
            cube.name = "Band " + i;
            cube.GetComponent<MeshRenderer>().material.color = Color.HSVToRGB(i / 12f, 1, 1);
        }
    }

    IEnumerator Interact()
    {
        while(true)
        {
            yield return null;
            float energy = AudioAnalysis.MeanEnergy(audioSource);
            
            if(AudioAnalysis.ConvertToDB(energy) > 80)
            {
                float peakFrequency = AudioAnalysis.ComputeSpectrumPeak(audioSource, true);
                float concentration = AudioAnalysis.ConcentrationAroundPeak(peakFrequency);
                Debug.Log(concentration);
                if(concentration > 0.7f) 
                {
                    transform.Rotate(new Vector3(0, 180f / 12f, 0));
                }
                else
                {
                    transform.position += transform.forward;
                }

                yield return new WaitForSeconds(0.5f);
            }
        }
    }
}
