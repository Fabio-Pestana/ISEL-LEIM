using Unity.Netcode;
using UnityEngine;
using UnityEngine.InputSystem;

public class CameraPlayer : NetworkBehaviour
{
    [SerializeField] 
    private float height = 10f;
    
    [SerializeField] 
    private float smoothSpeed = 5f;
    
    [SerializeField] 
    private Camera playerCamera;
    
    [SerializeField] 
    private Transform cameraPivot;

    public override void OnNetworkSpawn()
    {
        if (!IsOwner)
        {
            if (playerCamera != null)
                playerCamera.gameObject.SetActive(false);
            return;
        }

        SetupCamera();
    }

    private void SetupCamera()
    {
        cameraPivot.localPosition = Vector3.up * height;
        cameraPivot.localRotation = Quaternion.Euler(90f, 0f, 0f);
    }

    private void LateUpdate()
    {
        if (!IsOwner) return;

        Vector3 targetPosition = transform.position + Vector3.up * height;
        cameraPivot.position = Vector3.Lerp(
            cameraPivot.position,
            targetPosition,
            smoothSpeed * Time.deltaTime
        );

        cameraPivot.rotation = Quaternion.Euler(90f, 0f, 0f);
    }
}