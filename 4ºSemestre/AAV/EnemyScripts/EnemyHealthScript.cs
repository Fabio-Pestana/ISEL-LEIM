using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyHealth : MonoBehaviour
{
    [SerializeField]
    private Camera main_camera;

    void Update()
    {
        transform.LookAt(main_camera.transform);
    }
}
