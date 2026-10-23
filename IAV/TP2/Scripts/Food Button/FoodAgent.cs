using System.Collections;
using System.Collections.Generic;
using Unity.MLAgents;
using Unity.MLAgents.Actuators;
using Unity.MLAgents.Sensors;
using UnityEngine;
using System;

public class FoodAgent : Agent
{
    Rigidbody rb;

    public Button button;

    public FoodSpawner foodSpawner;

    public event EventHandler OnEpisodeBeginEvent;

    private void Awake()
    {
        base.Awake();
        rb = GetComponent<Rigidbody>();
    }

    public override void OnEpisodeBegin()
    {
        float x = UnityEngine.Random.Range(-9.5f, 9.5f);
        float z;
        if(x > 0)
            z = UnityEngine.Random.Range(-9.5f, 9.5f);
        else
            z = UnityEngine.Random.Range(0.5f, 9.5f);

        transform.localPosition = new Vector3(x, 0, z);
        OnEpisodeBeginEvent?.Invoke(this, EventArgs.Empty);
    }


    public override void CollectObservations(VectorSensor sensor)
    {
        Vector3 dirToButton = (button.transform.position - transform.position).normalized;

        sensor.AddObservation(dirToButton.x);
        sensor.AddObservation(dirToButton.z);
        sensor.AddObservation(button.CanPressButton() ? 1 : 0);
        sensor.AddObservation(foodSpawner.HasFoodSpawned() ? 1 : 0);

        if(foodSpawner.HasFoodSpawned())
        {
            Vector3 dirToFood = (foodSpawner.GetFoodPosition() - transform.position).normalized;
            sensor.AddObservation(dirToFood.x);
            sensor.AddObservation(dirToFood.z);
        }
        else
        {
            sensor.AddObservation(0);
            sensor.AddObservation(0);
        }
    }

    public override void OnActionReceived(ActionBuffers actions)
    {
        float moveX = actions.DiscreteActions[0];
        float moveZ = actions.DiscreteActions[1];
        bool buttonPressed = actions.DiscreteActions[2] == 1;

        float strength = 5;
        Vector3 force = Vector3.zero;

        switch (moveX)
        {
            case 0: force.x = 0; break;
            case 1: force.x = 1; break;
            case 2: force.x = -1; break;
        }

        switch (moveZ)
        {
            case 0: force.z = 0; break;
            case 1: force.z = 1; break;
            case 2: force.z = -1; break;
        }

        rb.AddForce(force * strength);
        //rb.velocity = force * strength;

        if (buttonPressed)
        {
            Collider[] colliderArray = Physics.OverlapBox(transform.position, Vector3.one * 0.5f);
            foreach(Collider collider in colliderArray)
            {
                if(collider.TryGetComponent<Button>(out Button button))
                {
                    if (button.CanPressButton())
                    {
                        button.PressButton();
                        AddReward(1f);
                    }
                }
            }

            AddReward(-1f / MaxStep);
        }
    }

    public override void Heuristic(in ActionBuffers actionsOut)
    {
        ActionSegment<int> discreteActions = actionsOut.DiscreteActions;
        int horizontal = Mathf.RoundToInt(Input.GetAxisRaw("Horizontal"));
        int vertical = Mathf.RoundToInt(Input.GetAxisRaw("Vertical"));

        switch (horizontal)
        {
            case 0: discreteActions[0] = 0; break;
            case -1: discreteActions[0] = 2; break;
            case 1: discreteActions[0] = 1; break;
        }

        switch (vertical) 
        {
            case 0: discreteActions[1] = 0; break;
            case -1: discreteActions[1] = 2; break;
            case 1: discreteActions[1] = 1; break;
        }

        discreteActions[2] = Input.GetKey(KeyCode.Space) ? 1 : 0;
    }

    private void OnCollisionEnter(Collision collision)
    {
        if(collision.gameObject.TryGetComponent<Food>(out Food food))
        {
            AddReward(1f);
            Destroy(food.gameObject);
            EndEpisode();
        }


        if (collision.gameObject.TryGetComponent<Wall>(out Wall wall))
        {
            AddReward(-1f);
            EndEpisode();
        }
    }
}
