using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Unity.Collections;
using Unity.Collections.LowLevel.Unsafe;
using Unity.Netcode;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.TextCore.Text;
using UnityEngine.UIElements;

public class CharacterBase: NetworkBehaviour
{
    protected Rigidbody _rb;
    protected Animator _animator;
    protected PlayerInput _playerInput;
    protected Vector2 _direction;
    protected Vector2 _mousePosition;
    protected Vector2 _androidLookInput;
    protected bool _isMovementLocked = false;
    protected GameUI _gameUI;
    protected bool blocking = false;

    protected int weaponDamage = 25;

    [SerializeField]
    protected float hability2Cooldown;

    [SerializeField]
    protected float hability3Cooldown;

    [SerializeField]
    protected float hability4Cooldown;

    protected bool hability2OnCooldown = false; 
    protected bool hability3OnCooldown = false;
    protected bool hability4OnCooldown = false;

    [SerializeField]
    protected float rotationSpeed = 5f;

    [SerializeField]
    protected float moveSpeed = 5f;

    [SerializeField]
    protected Camera _camera;

    protected NetworkVariable<int> hits = new NetworkVariable<int>(0);

    protected NetworkVariable<float> maxHealth = new NetworkVariable<float>(150f);

    protected NetworkVariable<float> currentHealth = new NetworkVariable<float>(150f);

    protected NetworkVariable<bool> isAttacking = new NetworkVariable<bool>(false);
        
    protected NetworkVariable<bool> isBuffed = new NetworkVariable<bool>(false);

    public NetworkVariable<int> coins = new NetworkVariable<int>(0);

    public NetworkVariable<FixedString32Bytes> floorIndication = new NetworkVariable<FixedString32Bytes>();

    private NetworkVariable<bool> isReady = new NetworkVariable<bool>(false);

    private NetworkVariable<int> changeScene = new NetworkVariable<int>();

    public NetworkVariable<bool> isAlive = new NetworkVariable<bool>(true);

    protected NetworkVariable<int> playerLevel = new NetworkVariable<int>(1);

    private NetworkVariable<bool> changeFloor = new NetworkVariable<bool>(false);

    public NetworkVariable<bool> revive = new NetworkVariable<bool>(false);

    public NetworkVariable<bool> InGame = new NetworkVariable<bool>(false);

    public NetworkVariable<FixedString32Bytes> playerName = new NetworkVariable<FixedString32Bytes>();
    public NetworkVariable<FixedString64Bytes> id = new NetworkVariable<FixedString64Bytes>();

    private Coroutine lavaCoroutine = null;

    private List<GameObject> cameras = new List<GameObject>();

    private int currentCameraIndex = 0;

    [ServerRpc(RequireOwnership = false)]
    public void SetLevelServerRpc(int level)
    {
        playerLevel.Value = Mathf.Clamp(level, 1, 5);

        switch (playerLevel.Value)
        {
            case 1:
                maxHealth.Value = 150f;
                break;
            case 2:
                maxHealth.Value = 180f;
                break;
            case 3:
                maxHealth.Value = 200f;
                break;
            case 4:
                maxHealth.Value = 225f;
                break;
            case 5:
                maxHealth.Value = 250f;
                break;
        }

        currentHealth.Value = maxHealth.Value;
    }

    protected virtual void Awake()
    {
        _rb = GetComponentInParent<Rigidbody>();
        _animator = GetComponent<Animator>();
        _playerInput = GetComponent<PlayerInput>();
        _playerInput.enabled = false;
    }

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (IsServer)
        {
            currentHealth.Value = maxHealth.Value;
        }

        currentHealth.OnValueChanged += OnHealthChanged;
        coins.OnValueChanged += OnCoinsChanged;
        isBuffed.OnValueChanged += OnBuffChanged;
        changeScene.OnValueChanged += OnChangeScene;
        floorIndication.OnValueChanged += OnChangeFloor;
        maxHealth.OnValueChanged += OnMaxHealthChanged;
        playerLevel.OnValueChanged += OnPlayerLevelChanged;
        revive.OnValueChanged += OnReviveChanged;
        InGame.OnValueChanged += OnInGameChanged;

        if (IsOwner)
        {
            _playerInput.enabled = true;
            FindAndSetupUI();
            UserData userData = SaveSystem.LoadUser();
            UpdateUserServerRpc(userData.UserName, userData.UserID);
            LoadPlayerServerRpc(userData.UserID);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void LoadPlayerServerRpc(string id)
    {
        GameSessionData session = GameManager.Instance.GetData();
        if (session == null || session.Players == null) return;

        PlayerSessionData savedData = session.Players
            .FirstOrDefault(p => p.PlayerID == id);

        if (savedData == null)
        {
            return;
        }

        PlayerSessionData.PlayerClass currentClass = PlayerSessionData.PlayerClass.Cleric;
        if (this is Mage) currentClass = PlayerSessionData.PlayerClass.Mage;
        else if (this is Knight) currentClass = PlayerSessionData.PlayerClass.Knight;

        if (currentClass == savedData.Class)
        {
            SetLevelServerRpc(savedData.Level);
        }

        SetCoinsServerRpc(savedData.Coins);
    }

    [ServerRpc(RequireOwnership = false)]
    public void UpdateUserServerRpc(FixedString32Bytes nome, FixedString64Bytes ID)
    {
        playerName.Value = nome;
        id.Value = ID;
    }

    private void OnReviveChanged(bool oldValue, bool newValue)
    {
        if (!IsOwner || !newValue) return;

        _playerInput.enabled = true;

        ShowForAllClientsServerRpc();

        _camera.gameObject.SetActive(true);

        _gameUI.CameraBttn(false);
        _animator.SetBool("Death", false);

        SetPosition();

        ChangeIsAliveServerRpc(true);

        HealServerRpc();

        ResetReviveServerRpc(false);

        RemoveCameras();
    }

    [ServerRpc(RequireOwnership = false)]
    public void ResetReviveServerRpc(bool value)
    {
        revive.Value = value;
    }

    public void OnMaxHealthChanged(float oldValue, float newValue)
    {
        if (!IsOwner) return;

        _gameUI.LoadVariables(maxHealth.Value);
    }

    private void FindAndSetupUI()
    {
        if (!IsOwner) return;

        // Encontra o GameUI na cena
        _gameUI = ManagerUI.Instance.GetGameUI();

        if (_gameUI != null)
        {
            _gameUI.LoadVariables(maxHealth.Value);
            _gameUI.UpdateCoins(coins.Value);
            _gameUI.SetBuffActive(isBuffed.Value);
            _gameUI.SetPlayer(this);
            _gameUI.SetImages();
            _gameUI.UpdateImages(GetPlayerLevel());
        }
    }

    public void OnHealthChanged(float oldValue, float newValue)
    {
        if (!IsOwner) return;

        if (newValue < oldValue)
        {
            // Vida diminuiu - tomar dano
            float damage = oldValue - newValue;
            _gameUI.TakeDamage(damage);
        }
        else if (newValue > oldValue && newValue == maxHealth.Value)
        {
            // Vida aumentou para o maximo
            _gameUI.Heal();
        }

        if (newValue <= 0 && isAlive.Value)
        {
            ChangeIsAliveServerRpc(false);
            StartCoroutine(HandleDeath());
        }
    }

    private IEnumerator HandleDeath()
    {
        _animator.SetBool("Death", true);
        _playerInput.enabled = false;

        yield return new WaitForSeconds(4f);

        Die();
    }

    protected void ReadGamepad()
    {
        if (Application.isMobilePlatform && isAlive.Value)
        {
            _direction = Gamepad.current.leftStick.ReadValue();
            _androidLookInput = Gamepad.current.rightStick.ReadValue();
        }
    }

    private void RotateWithRightStick()
    {
        if (_androidLookInput.sqrMagnitude > 0.1f)
        {
            Vector3 lookDirection = new Vector3(_androidLookInput.x, 0f, _androidLookInput.y);

            Quaternion targetRotation = Quaternion.LookRotation(lookDirection, Vector3.up);
            _rb.MoveRotation(Quaternion.Slerp(_rb.rotation, targetRotation, rotationSpeed * Time.fixedDeltaTime));
        }
    }

    protected void MovementAndAnimation(Vector2 direction, bool canRotate)
    {
        Vector3 moveDirection = new Vector3(direction.x, 0, direction.y);

        Vector3 movement = moveDirection * moveSpeed * Time.fixedDeltaTime;

        _rb.MovePosition(_rb.position + movement);
        UpdateAnimation(moveDirection);

        if (canRotate) 
        {
            if (Application.isMobilePlatform)
                RotateWithRightStick();
            else
                RotateToMouse();
        }
    }

    private void UpdateAnimation(Vector3 moveWorldDir)
    {
        // Converte a direcao do movimento global para local (relativa a rotacao do jogador)
        Vector3 moveLocalDir = transform.InverseTransformDirection(moveWorldDir);

        // Ajusta os parametros da animacao baseado na direcao local
        _animator.SetFloat("MoveX", moveLocalDir.x, 0.2f, Time.fixedDeltaTime);
        _animator.SetFloat("MoveY", moveLocalDir.z, 0.2f, Time.fixedDeltaTime);
    }

    private void RotateToMouse()
    {
        Ray ray = _camera.ScreenPointToRay(_mousePosition);
        RaycastHit hit;

        if (Physics.Raycast(ray, out hit, 100f))
        {
            Vector3 lookDirection = hit.point - transform.position;
            lookDirection.y = 0;

            if (lookDirection.sqrMagnitude > 0.01f)
            {
                Quaternion targetRotation = Quaternion.LookRotation(lookDirection);
                _rb.MoveRotation(Quaternion.Slerp(_rb.rotation, targetRotation, rotationSpeed * Time.fixedDeltaTime));
            }
        }
    }

    [ServerRpc(RequireOwnership = false)]
    protected void ChangeIsAliveServerRpc(bool value)
    {
        isAlive.Value = value;
    }

    private void Die()
    {
        if (!IsOwner) return;

        HideForAllClientsServerRpc();

        SetPosition();

        _camera.gameObject.SetActive(false);

        _gameUI.CameraBttn(true);

        GetCamerasInScene();

        GameManager.Instance.CheckAllPlayersDeadServerRpc();
    }

    public void GetCamerasInScene()
    {
        cameras = new List<GameObject>(GameObject.FindGameObjectsWithTag("Cameras"));
        Debug.Log(GameObject.FindGameObjectsWithTag("Cameras").Length);

        if (cameras.Count > 0)
        {
            currentCameraIndex = 0;
            SetActiveCamera(currentCameraIndex);
        }
    }

    public void NextCamera()
    {
        if (cameras.Count == 0) return;

        currentCameraIndex = (currentCameraIndex + 1) % cameras.Count;
        SetActiveCamera(currentCameraIndex);
    }

    public void PreviousCamera()
    {
        if (cameras.Count == 0) return;

        currentCameraIndex = (currentCameraIndex - 1 + cameras.Count) % cameras.Count;
        SetActiveCamera(currentCameraIndex);
    }

    private void SetActiveCamera(int index)
    {
        for (int i = 0; i < cameras.Count; i++)
        {
            var cam = cameras[i];
            var cameraComponent = cam.GetComponent<Camera>();
            var audioListener = cam.GetComponent<AudioListener>();

            bool isActive = (i == index);

            if (cameraComponent != null)
                cameraComponent.enabled = isActive;

            if (audioListener != null)
                audioListener.enabled = isActive;
        }
    }

    private void RemoveCameras()
    {
        for (int i = 0; i < cameras.Count; i++)
        {
            var cam = cameras[i];
            var cameraComponent = cam.GetComponent<Camera>();
            var audioListener = cam.GetComponent<AudioListener>();

            if (cameraComponent != null)
                cameraComponent.enabled = false;

            if (audioListener != null)
                audioListener.enabled = false;
        }
    }

    // Input System Events
    public void MousePosition(InputAction.CallbackContext context)
    {
        _mousePosition = context.ReadValue<Vector2>();
    }

    public void MovePlayer(InputAction.CallbackContext context)
    {
        _direction = context.ReadValue<Vector2>();
    }

    [ServerRpc]
    public void DamageFromSkeletonServerRpc(ulong skeletonNetworkId, bool blocking)
    {
        if (!IsServer) return;

        // Busca o esqueleto na rede pelo NetworkObjectId
        if (NetworkManager.Singleton.SpawnManager.SpawnedObjects.TryGetValue(skeletonNetworkId, out NetworkObject skeletonObject))
        {
            Skeleton skeleton = skeletonObject.GetComponent<Skeleton>();
            if (skeleton != null)
            {
                int damage = skeleton.GetDamage();
                if (damage > 0)
                {
                    if (blocking)
                    {
                        hits.Value += 1;
                        Debug.Log("No damage (hits): " + hits.Value);
                    }
                    else
                    {
                        currentHealth.Value -= damage;
                    }
                }
            }
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (!IsOwner) return;

        if (other.CompareTag("EnemyWeapon"))
        {
            NetworkObject skeletonNetObj = other.GetComponentInParent<NetworkObject>();
            if (skeletonNetObj != null)
            {
                DamageFromSkeletonServerRpc(skeletonNetObj.NetworkObjectId, blocking);
            }
        }

        if (other.CompareTag("Spawner"))
        {
            NetworkObject netObj = other.GetComponent<NetworkObject>();
            if (netObj != null && netObj.TryGetComponent<Spawner>(out var spawner))
            {
                spawner.RequestSpawnEnemiesServerRpc();
            }
        }

        if (other.CompareTag("LevelUp"))
        {
            _gameUI.LevelUpActive();
        }

        if (other.CompareTag("NextFloor") && changeFloor.Value)
        {
            _gameUI.SetReadyButton(true);
        }
    }

    private void OnTriggerStay(Collider other)
    {
        if (other.CompareTag("NextFloor") && changeFloor.Value)
        {
            if(_gameUI != null)
                _gameUI.SetReadyButton(true);
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (!IsOwner) return;

        if (other.CompareTag("LevelUp"))
        {
            _gameUI.LevelUpDesactive();
        }

        if (other.CompareTag("NextFloor") && changeFloor.Value)
        {
            _gameUI.SetReadyButton(false);
        }
    }

    public int WeaponDamage()
    {
        if (!isAttacking.Value) return 0;

        float multiplier = isBuffed.Value ? 2f : 1f;

        return Mathf.RoundToInt(weaponDamage * multiplier);
    }


    [ServerRpc(RequireOwnership = false)]
    public void ActivateBuffServerRpc()
    {
        if (!IsServer) return;

        isBuffed.Value = true;
        StartCoroutine(BuffTimerCoroutine());
    }

    private IEnumerator BuffTimerCoroutine()
    {
        yield return new WaitForSeconds(15f);
        isBuffed.Value = false;
    }

    [ServerRpc(RequireOwnership = false)]
    protected void ChangeIsAttackingServerRpc(bool value)
    {
        isAttacking.Value = value;
    }

    private void OnChangeScene(int oldValue, int newValue)
    {
        if (IsOwner && _gameUI != null)
        {
            switch (newValue)
            {
                case 1:
                    _gameUI.LoadingScreen();
                    break;
                case 2:
                    SetPosition();
                    _gameUI.GameScreen();
                    if (!isAlive.Value)
                    {
                        GetCamerasInScene();
                    }
                    break;
                case 3:
                    _gameUI.ToNexus();
                    break;
                case 4:
                    _gameUI.GameScreenFromPlayersDeadOrWin();
                    if (!isAlive.Value)
                    {
                        GetCamerasInScene();
                    }
                    break;
                case 5:
                    _gameUI.VictoryToNexus();
                    break;
                default:
                    _gameUI.LoadingScreen();
                    break;
            }
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void ChangeSceneServerRpc(int value)
    {
        changeScene.Value = value;
    }

    private void OnChangeFloor(FixedString32Bytes oldValue, FixedString32Bytes newValue)
    {
        if (IsOwner && _gameUI != null)
        {
            _gameUI.SetFloor(newValue.ToString());
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void ChangeFloorValueServerRpc(string value)
    {
        floorIndication.Value = new FixedString32Bytes(value);
    }


    private void OnCollisionEnter(Collision collision)
    {
        if (!IsOwner) return;

        if (collision.gameObject.CompareTag("Lava"))
        {
            if (lavaCoroutine == null)
            {
                lavaCoroutine = StartCoroutine(LavaDamageCoroutine());
            }
        }

        if (collision.gameObject.CompareTag("Saw"))
        {
            TakeDamageServerRpc(20f);
        }
    }

    private void OnCollisionExit(Collision collision)
    {
        if (!IsOwner) return;

        if (collision.gameObject.CompareTag("Lava"))
        {
            if (lavaCoroutine != null)
            {
                StopCoroutine(lavaCoroutine);
                lavaCoroutine = null;
            }
        }
    }

    private IEnumerator LavaDamageCoroutine()
    {
        while (true)
        {
            TakeDamageServerRpc(3f);
            yield return new WaitForSeconds(1f);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void TakeDamageServerRpc(float damage)
    {
        currentHealth.Value -= damage;
    }

    public void Heal()
    {
        if (!IsServer) return;

        currentHealth.Value = maxHealth.Value;
    }

    [ServerRpc(RequireOwnership = false)]
    private void HealServerRpc()
    {
        Heal();
    }

    private void OnCoinsChanged(int oldValue, int newValue)
    {
        if (IsOwner && _gameUI != null)
        {
            _gameUI.UpdateCoins(newValue);
        }
    }

    private void OnPlayerLevelChanged(int oldValue, int newValue)
    {
        if (IsOwner && _gameUI != null)
        {
            _gameUI.UpdateImages(newValue);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void AddCoinsServerRpc(int amount)
    {
        coins.Value += amount;
    }

    [ServerRpc(RequireOwnership = false)]
    public void SetCoinsServerRpc(int amount)
    {
        coins.Value = amount;
    }

    [ServerRpc(RequireOwnership = false)]
    public void SpendCoinsServerRpc(int amount)
    {
        if (coins.Value >= amount)
        {
            coins.Value -= amount;
        }
    }

    private void OnBuffChanged(bool oldValue, bool newValue)
    {
        if (IsOwner && _gameUI != null)
        {
            _gameUI.SetBuffActive(newValue);
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void ToggleReadyStateServerRpc()
    {
        isReady.Value = !isReady.Value;

        if (IsServer && GameManager.Instance != null)
        {
            GameManager.Instance.CheckIfAllPlayersAreReadyServerRpc();
        }
    }

    public bool IsReady()
    {
        return isReady.Value;
    }

    public void SetPosition()
    {
        List<GameObject> spawnPoints = GameObject.FindGameObjectsWithTag("SpawnPoint").ToList();

        if (spawnPoints == null || spawnPoints.Count == 0)
        {
            Debug.LogError("No spawn points found!");
            return;
        }


    int index = Random.Range(0, spawnPoints.Count);
    Transform spawn = spawnPoints[index].transform;

    transform.position = spawn.position;
    transform.rotation = spawn.rotation;
    }


    protected IEnumerator Hability2CooldownRoutine()
    {
        hability2OnCooldown = true;
        _gameUI.setImage(0, false);
        yield return new WaitForSeconds(hability2Cooldown);
        hability2OnCooldown = false;
        _gameUI.setImage(0, true);
    }

    protected IEnumerator Hability3CooldownRoutine()
    {
        hability3OnCooldown = true;
        _gameUI.setImage(1, false);
        yield return new WaitForSeconds(hability3Cooldown);
        hability3OnCooldown = false;
        _gameUI.setImage(1, true);
    }

    protected IEnumerator Hability4CooldownRoutine()
    {
        hability4OnCooldown = true;
        _gameUI.setImage(2, false);
        yield return new WaitForSeconds(hability4Cooldown);
        hability4OnCooldown = false;
        _gameUI.setImage(2, true);
    }

    public int GetPlayerLevel()
    {
        return playerLevel.Value;
    }

    public int GetCoins()
    {
        return coins.Value;
    }

    [ServerRpc(RequireOwnership = false)]
    public void SetChangeFloorServerRpc(bool value)
    {
        changeFloor.Value = value;
    }

    [ServerRpc(RequireOwnership = false)]
    private void HideForAllClientsServerRpc()
    {
        HideClientRpc();
    }

    [ClientRpc]
    private void HideClientRpc()
    {
        foreach (Renderer renderer in GetComponentsInChildren<Renderer>())
        {
            renderer.enabled = false;
        }
    }

    [ServerRpc(RequireOwnership = false)]
    private void ShowForAllClientsServerRpc()
    {
        ShowForAllClientsClientRpc();
    }

    [ClientRpc]
    private void ShowForAllClientsClientRpc()
    {
        var renderers = GetComponentsInChildren<Renderer>(true);
        foreach (var renderer in renderers)
        {
            renderer.enabled = true;
        }
    }

    private void OnInGameChanged(bool oldValue, bool newValue)
    {
        if (!IsOwner || oldValue == newValue) return;

        if (newValue)
        {
            SoundManager.Instance.PlayGameMusic();
        }
        else
        {
            SoundManager.Instance.PlayMenuMusic();
        }
    }

    [ServerRpc(RequireOwnership = false)]
    public void SetInGameStateServerRpc(bool state)
    {
        InGame.Value = state;
    }
}
