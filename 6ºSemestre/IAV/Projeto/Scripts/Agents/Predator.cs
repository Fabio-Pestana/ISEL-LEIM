using UnityEngine;
using Unity.MLAgents;
using Unity.MLAgents.Sensors;
using Unity.MLAgents.Actuators;

public class Predator : Agent
{
    private float speed = 7f;

    [SerializeField]
    private Transform prey;

    public override void OnEpisodeBegin()
    {
        float x = Random.Range(-18f, 0f);
        float z = Random.Range(-18f, 0f);

        transform.localPosition = new Vector3(x, 0, z);
    }

    public override void CollectObservations(VectorSensor sensor)
    {
        Vector3 relativePos = prey.localPosition - transform.localPosition;
        Vector3 normalPos = relativePos.normalized;

        sensor.AddObservation(normalPos.x);
        sensor.AddObservation(normalPos.z);
        sensor.AddObservation(relativePos.magnitude);
    }

    public override void OnActionReceived(ActionBuffers actions)
    {
        float moveX = actions.ContinuousActions[0];
        float moveZ = actions.ContinuousActions[1];
        Vector3 movement = new Vector3(moveX, 0f, moveZ) * speed * Time.deltaTime;
        this.transform.localPosition += movement;

        AddReward(-1f / MaxStep);
    }


    public override void Heuristic(in ActionBuffers actionsOut)
    {
        var ca = actionsOut.ContinuousActions;
        ca[0] = Input.GetAxis("Horizontal");
        ca[1] = Input.GetAxis("Vertical");
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("Prey"))
        {
            collision.gameObject.GetComponent<Prey>().onCapture();
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("Wall"))
        {
            AddReward(-1f);
            prey.GetComponent<Prey>().EndEpisodeForAll();
        }
    }
}
