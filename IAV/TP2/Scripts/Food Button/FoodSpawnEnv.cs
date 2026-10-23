using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class FoodSpawnEnv : MonoBehaviour
{
    Button button;
    FoodSpawner foodSpawner;
    FoodAgent foodAgent;

    private void Awake()
    {
        button = GetComponentInChildren<Button>();
        foodSpawner = GetComponentInChildren<FoodSpawner>();
        foodAgent = GetComponentInChildren<FoodAgent>();
    }

    // Start is called before the first frame update
    void Start()
    {
        foodAgent.OnEpisodeBeginEvent += HandleOnEpisodeBegin;
        button.OnButtonPressed += HandleOnButtonPressed;
    }

    public void HandleOnButtonPressed(object sender, EventArgs e)
    {
        foodSpawner.Spawn();
    }

    public void HandleOnEpisodeBegin(object sender, EventArgs e)
    {
        button.ResetButton();
        GameObject food = foodSpawner.FoodGameObject();
        if(food != null) Destroy(food);
        foodSpawner.HasFoodSpawner(false);
    }
}
