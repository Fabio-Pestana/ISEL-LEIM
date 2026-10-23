using UnityEngine;
using Unity.MLAgents;
using Unity.MLAgents.Sensors;
using Unity.MLAgents.Actuators;
using System.Collections.Generic;

public class Prey : Agent
{
    private float speed = 8f;
    private int maxPredators = 3;

    [SerializeField]
    private List<Transform> predators = new List<Transform>();

    [SerializeField]
    private bool player;

    [SerializeField]
    private TouchOSCReceiver receiver;

    public override void OnEpisodeBegin()
    {
        float x = Random.Range(-10f, 10f);
        float z;
        if (x > 0)
            z = Random.Range(-10f, 10f);
        else
            z = Random.Range(1f, 10f);

        transform.localPosition = new Vector3(x, 0, z);
    }

    public override void CollectObservations(VectorSensor sensor)
    {

        for (int i = 0; i < maxPredators; i++)
        {
            if(i < predators.Count)
            {
                Vector3 relativePos = predators[i].localPosition - transform.localPosition;
                Vector3 normalPos = relativePos.normalized;

                sensor.AddObservation(normalPos.x);
                sensor.AddObservation(normalPos.z);
                sensor.AddObservation(relativePos.magnitude);
            }
            else
            {
                sensor.AddObservation(0f);
                sensor.AddObservation(0f);
                sensor.AddObservation(-1f);
            }
        }
    }

    public override void OnActionReceived(ActionBuffers actions)
    {
        float moveX = actions.ContinuousActions[0];
        float moveZ = actions.ContinuousActions[1];
        Vector3 movement = new Vector3(moveX, 0f, moveZ) * speed * Time.deltaTime;
        this.transform.localPosition += movement;

        AddReward(+0.01f);
    }

    public override void Heuristic(in ActionBuffers actionsOut)
    {
        var ca = actionsOut.ContinuousActions;
        
        if (player)
        {
            Vector2 move = receiver.GetMovement();
            ca[0] = move.x;
            ca[1] = move.y;
        }
        else 
        {
            // assim fica parada para testar o predator
            ca[0] = Input.GetAxis("Horizontal") * 0;
            ca[1] = Input.GetAxis("Vertical") * 0;
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("Wall"))
        {
            AddReward(-3f);
            EndEpisodeForAll();
        }
    }

    public void onCapture()
    {
        AddReward(-1f);

        foreach (Transform predator in predators)
        {
            if (predator != null)
            {
                float distance = Vector3.Distance(predator.position, transform.position);
                float reward = CalculatePredatorReward(distance);
                //Debug.Log(reward);
                predator.GetComponent<Predator>().AddReward(reward);
            }
        }

        EndEpisodeForAll();
    }

    private float CalculatePredatorReward(float distance)
    {
        if (distance <= 6f) // quem matou ou quase matava
        {
            return 1f;
        }
        else if (distance <= 12f) // Ajudou na caça
        {
            return 0.7f;
        }
        else // Nao ajudou
        {
            return -1f;
        }
    }

    public void EndEpisodeForAll()
    {
        EndEpisode();

        foreach (Transform predator in predators)
        {
            if (predator != null)
            {
                predator.GetComponent<Predator>().EndEpisode();
            }
        }
    }
}
