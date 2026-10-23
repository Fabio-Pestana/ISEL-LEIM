using UnityEngine;

public class DrawSpectrum : MonoBehaviour
{
    public AudioSource audiosource;

    // Update is called once per frame
    void Update()
    {
        float[] samples = AudioAnalysis.GetSpectrum(audiosource);

        for (int i = 1; i < samples.Length - 1; i++) 
        {
            Debug.DrawLine(new Vector3(i - 1, AudioAnalysis.ConvertToDB(samples[i - 1] * samples[i - 1]), 0),
                new Vector3(i + 1, AudioAnalysis.ConvertToDB(samples[i + 1] * samples[i + 1]), 0), 
                Color.yellow);
        }
    }
}
