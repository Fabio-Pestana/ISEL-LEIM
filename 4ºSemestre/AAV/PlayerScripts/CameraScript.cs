using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FollowCamera : MonoBehaviour
{
    [SerializeField]
    private GameObject target;

    [SerializeField]
    private float heightOffset = 1.5f;

    private Vector3 offset;

    private float angle;
    private float desiredAngle;
    private float currentAngle;

    public float damping = 1.0f;

    private Quaternion rotation;


    void Start()
    {
        offset = transform.position - target.transform.position;
    }

    void LateUpdate()
    {
        currentAngle = transform.eulerAngles.y;
        desiredAngle = target.transform.eulerAngles.y;
        angle = Mathf.LerpAngle(currentAngle, desiredAngle, damping * Time.deltaTime);

        rotation = Quaternion.Euler(0, angle, 0);
        transform.position = target.transform.position - (rotation * (-offset));

        Vector3 lookAtPosition = target.transform.position;
        lookAtPosition.y += heightOffset;

        transform.LookAt(lookAtPosition);
    }
}

