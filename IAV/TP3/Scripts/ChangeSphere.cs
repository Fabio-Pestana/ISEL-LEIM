using UnityEngine;

public class ChangeSphere : MonoBehaviour
{
    public AudioSource audioSource;
    MeshRenderer mr;
    Mesh mesh;
    Vector3[] vertices, originalVertices;


    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        mr = GetComponent<MeshRenderer>();
        mesh = GetComponent<MeshFilter>().mesh;
        originalVertices = mesh.vertices;
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        float r = 40f;
        float rms = AudioAnalysis.ComputeRMS(audioSource);
        float[] bands = AudioAnalysis.FreqBands(audioSource);

        transform.localScale = new Vector3(15f * rms, 15 * rms, 15 * rms);
        mr.material.color = new Color(bands[0]/r, bands[2]/r, bands[3]/r, bands[5]/r);

        transform.Rotate(new Vector3(0, bands[4], 0));
        vertices = mesh.vertices;
        for (int p = 0; p< vertices.Length; p++)
        {
            Vector3 dir = originalVertices[p] - Vector3.zero;
            vertices[p] = originalVertices[p] + dir * bands[p % bands.Length] / r;
        }

        mesh.vertices = vertices;
        mesh.RecalculateNormals();
        mesh.RecalculateTangents();
        mesh.RecalculateBounds();
    }
}
