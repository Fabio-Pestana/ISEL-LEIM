using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FoodSpawner : MonoBehaviour
{
    bool hasFoodSpawned;
    GameObject food;
    public Material foodMaterial;

    public GameObject FoodGameObject()
    {
        return food;
    }

    public void HasFoodSpawner(bool state)
    {
        hasFoodSpawned = state;
    }

    public bool HasFoodSpawned()
    {
        return hasFoodSpawned;
    }

    public Vector3 GetFoodPosition()
    {
        return food.transform.position;
    }
    
    public void Spawn()
    {
        food =  GameObject.CreatePrimitive(PrimitiveType.Sphere);

        food.name = "food";

        Rigidbody rb = food.AddComponent<Rigidbody>();
        food.AddComponent<Food>();
        food.GetComponent<MeshRenderer>().material = foodMaterial;

        hasFoodSpawned = true;

        food.transform.parent = transform;
        food.transform.localPosition = new Vector3(0, 5, 0);

        rb.velocity = new Vector3(Random.Range(-2, 2), 0, Random.Range(-2, 2)); 
    }
}
