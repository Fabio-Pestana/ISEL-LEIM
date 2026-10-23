using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PersistentEventSystem : MonoBehaviour
{
    private static bool created = false; // Evita duplicação

    void Awake()
    {
        if (created)
        {
            Destroy(gameObject);
            return;
        }

        created = true;
        DontDestroyOnLoad(gameObject);
    }
}
