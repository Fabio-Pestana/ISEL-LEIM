using UnityEngine;

public class SoundTime : MonoBehaviour
{
    AudioSource audioSource;

    GameObject[] cubes = new GameObject[1024];
    GameObject[] bandCubes = new GameObject[7];


    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        audioSource = GetComponent<AudioSource>();
        for (int i = 0; i < cubes.Length; i++)
        {
            GameObject cube = GameObject.CreatePrimitive(PrimitiveType.Cube);
            cube.transform.parent = transform;
            cube.GetComponent<MeshRenderer>().material = GetComponent<MeshRenderer>().material;

            cube.transform.Rotate(new Vector3(0, 360f / 1024f * i, 0));
            cube.transform.position += cube.transform.forward * 20;
            cube.name = "Sample " + i;

            cubes[i] = cube;
        }

        for(int i = 0;i < bandCubes.Length;i++)
        {
            GameObject cube = GameObject.CreatePrimitive(PrimitiveType.Cube);
            cube.transform.parent = transform;
            cube.GetComponent<MeshRenderer>().material = GetComponent<MeshRenderer>().material;

            cube.transform.Rotate(new Vector3(0, 360f / 7f * i, 0));
            cube.transform.position += cube.transform.forward * 10;
            cube.name = "Band " + i;

            bandCubes[i] = cube;
        }
    }

    // Update is called once per frame
    void Update()
    {
        // signal processing
        float meanEnergy = AudioAnalysis.MeanEnergy(audioSource);
        float energyDB = AudioAnalysis.ConvertToDB(meanEnergy);
        Debug.Log("Mean Energy (DB): " + energyDB);
        transform.localScale = new Vector3(
            transform.localScale.x,
            energyDB/50f,
            transform.localScale.z);

        float[] samples = AudioAnalysis.GetSpectrum(audioSource);

        for (int i = 0;i < cubes.Length; i++)
        {
            cubes[i].transform.localScale = new Vector3(
            cubes[i].transform.localScale.x,
            samples[i] * 10f,
            cubes[i].transform.localScale.z); 
        }

        float[] bands = AudioAnalysis.FreqBands(audioSource);

        for (int i = 0; i < bandCubes.Length; i++)
        {
            bandCubes[i].transform.localScale = new Vector3(
            bandCubes[i].transform.localScale.x,
            bands[i] * 0.1f,
            bandCubes[i].transform.localScale.z);
        }
    }
}
