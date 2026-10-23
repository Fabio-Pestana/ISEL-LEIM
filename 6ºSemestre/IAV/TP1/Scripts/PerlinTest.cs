using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PerlinTest : MonoBehaviour
{
    float t = 0;
    float tt = 0;
    public float inc = 0.001f;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        t += Time.deltaTime;
        float hc1 = 0.5f * (Mathf.Cos(2 * t + 0.4f) + 1);
        float hc2 = 0.25f * (Mathf.Cos(4 * t) + 1);
        float hc3 = 0.125f * (Mathf.Cos(6 * t) + 1);
        float hr = Random.Range(0f, 1f);

        float hp = Mathf.PerlinNoise(tt, 1);
        tt += inc;

    }
}
