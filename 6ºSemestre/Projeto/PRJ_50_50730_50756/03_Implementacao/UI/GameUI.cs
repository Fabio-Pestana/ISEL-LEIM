using System.Collections;
using TMPro;
using Unity.Netcode;
using Unity.Netcode.Transports.UTP;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;


public class GameUI : MonoBehaviour
{
    [SerializeField]
    private Slider healthSlider;

    [SerializeField]
    private Slider easeHealthSlider;

    [SerializeField]
    private GameObject mobileMoveStick;

    [SerializeField] 
    private Image[] uiImages = new Image[3];

    [SerializeField]    
    private TMP_Text pingText;

    private UnityTransport unityTransport;

    [SerializeField]
    private GameObject mobileAimStick;

    [SerializeField] private UnityEngine.UI.Button basicAttackButton;
    [SerializeField] private UnityEngine.UI.Button ability1Button;
    [SerializeField] private UnityEngine.UI.Button ability2Button;
    [SerializeField] private UnityEngine.UI.Button ultimateButton;

    [SerializeField] private Sprite[] imageSet1; // Habilidade 2
    [SerializeField] private Sprite[] imageSet2; // Habilidade 3
    [SerializeField] private Sprite[] imageSet3; // Habilidade 4

    [SerializeField] private Sprite[] imageBWSet1; // Habilidade 2
    [SerializeField] private Sprite[] imageBWSet2; // Habilidade 3
    [SerializeField] private Sprite[] imageBWSet3; // Habilidade 4

    private float maxHealth = 100f;

    private float health;

    private float lerpSpeed = 0.05f;

    [SerializeField]
    private GameObject game;

    [SerializeField]
    private GameObject options;

    [SerializeField]
    private GameObject loading;

    [SerializeField]
    private GameObject playersDead;

    [SerializeField]
    private GameObject winPanel;

    [SerializeField]
    private GameObject buffImage;

    [SerializeField] 
    private TMP_Text coins;

    private CharacterBase player;

    [SerializeField]
    private UnityEngine.UI.Button readyButton;

    [SerializeField]
    private TMP_Text readyButtonText;

    [SerializeField]
    private UnityEngine.UI.Button levelUpButton;

    [SerializeField]
    private TMP_Text levelUpText;

    [SerializeField]
    private TMP_Text nameText;

    [SerializeField]
    private TMP_Text floorText;

    private bool localReadyState = false;

    private bool inLobby = true;

    [SerializeField]
    private UnityEngine.UI.Button previous;

    [SerializeField]
    private UnityEngine.UI.Button next;

    [SerializeField]
    private UnityEngine.UI.Button saveGame;

    [SerializeField]
    private Slider volumeSlider;

    private void Awake()
    {
        previous.gameObject.SetActive(false);
        next.gameObject.SetActive(false);

        uiImages[0].gameObject.SetActive(!Application.isMobilePlatform);
        uiImages[1].gameObject.SetActive(!Application.isMobilePlatform);
        uiImages[2].gameObject.SetActive(!Application.isMobilePlatform);

        mobileMoveStick.SetActive(Application.isMobilePlatform);

        mobileAimStick.SetActive(Application.isMobilePlatform);

        basicAttackButton.gameObject.SetActive(Application.isMobilePlatform);
        ability1Button.gameObject.SetActive(Application.isMobilePlatform);
        ability2Button.gameObject.SetActive(Application.isMobilePlatform);
        ultimateButton.gameObject.SetActive(Application.isMobilePlatform);
    }

    void Start()
    {
        unityTransport = (UnityTransport) NetworkManager.Singleton.NetworkConfig.NetworkTransport;
        LoadVariables(maxHealth);
        options.SetActive(false);
        readyButton.gameObject.SetActive(false);
        readyButton.onClick.AddListener(OnReadyButtonPressed);
        levelUpButton.onClick.AddListener(OnLevelUpButtonPressed);

        next.onClick.AddListener(OnNextPressed);
        previous.onClick.AddListener(OnPreviousPressed);
    }

    // Update is called once per frame
    void Update()
    {
        if (healthSlider.value != health)
        {
            healthSlider.value = health;
        }

        if (healthSlider.value != easeHealthSlider.value)
        {
            easeHealthSlider.value = Mathf.Lerp(easeHealthSlider.value, health, lerpSpeed);
        }

        if (unityTransport != null && NetworkManager.Singleton != null)
        {
            var rtt = unityTransport.GetCurrentRtt(unityTransport.ServerClientId);
            pingText.text = $"Ping: {rtt / 2} ms";
        }
    }

    public void TakeDamage(float damage)
    {
        health -= damage;
    }

    public void Heal()
    {
        health = maxHealth;
    }

    public void LoadVariables(float maxValue)
    {
        healthSlider.maxValue = maxValue;
        healthSlider.value = maxValue;

        easeHealthSlider.maxValue = maxValue;
        easeHealthSlider.value = maxValue;

        health = maxValue;
        maxHealth = maxValue;
    }

    public void UpdateCoins(int coinCount)
    {
        coins.text = coinCount.ToString();
    }

    public void SetBuffActive(bool isActive)
    {
        buffImage.SetActive(isActive);
    }

    public void SetPlayer(CharacterBase value)
    {
        player = value;

        if (Application.isMobilePlatform)
        {
            SetupAbilityButtons(value);

            basicAttackButton.gameObject.SetActive(true);
            ability1Button.gameObject.SetActive(true);
            ability2Button.gameObject.SetActive(true);
            ultimateButton.gameObject.SetActive(true);
        }
        else
        {
            basicAttackButton.gameObject.SetActive(false);
            ability1Button.gameObject.SetActive(false);
            ability2Button.gameObject.SetActive(false);
            ultimateButton.gameObject.SetActive(false);
        }
    }

    private void SetupAbilityButtons(CharacterBase character)
    {
        if (!Application.isMobilePlatform || character == null) return;

        basicAttackButton.onClick.RemoveAllListeners();
        ability1Button.onClick.RemoveAllListeners();
        ability2Button.onClick.RemoveAllListeners();
        ultimateButton.onClick.RemoveAllListeners();

        // Configuracao especifica por classe
        if (character is Knight knight)
        {
            //Basic Attack
            basicAttackButton.onClick.AddListener(() => {
                knight.OnAttackMobile();
            });

            //Ability1
            var trigger = ability1Button.gameObject.AddComponent<EventTrigger>();

            var pointerDown = new EventTrigger.Entry();
            pointerDown.eventID = EventTriggerType.PointerDown;
            pointerDown.callback.AddListener((e) => {
                knight.OnBlockMobileStart();
            });

            var pointerUp = new EventTrigger.Entry();
            pointerUp.eventID = EventTriggerType.PointerUp;
            pointerUp.callback.AddListener((e) => {
                knight.OnBlockMobileEnd();
            });

            trigger.triggers.Add(pointerDown);
            trigger.triggers.Add(pointerUp);

            //Ability2
            ability2Button.onClick.AddListener(() => {
                knight.OnAttack3Mobile();
            });

            //Ability3
            ultimateButton.onClick.AddListener(() => {
                knight.OnAttack4Mobile();
            });
        }
        else if (character is Mage mage)
        {
            //Basic Attack
            basicAttackButton.onClick.AddListener(() => {
                mage.OnAttackMobile();
            });

            //Ability1
            ability1Button.onClick.AddListener(() => {
                mage.OnAttack2Mobile();
            });

            //Ability2
            ability2Button.onClick.AddListener(() => {
                mage.OnAttack3Mobile();
            });

            //Ability3
            ultimateButton.onClick.AddListener(() => {
                mage.OnAttack4Mobile();
            });
        }
        else if (character is Cleric cleric)
        {
            //Basic Attack
            basicAttackButton.onClick.AddListener(() => {
                cleric.OnAttackMobile();
            });

            //Ability1
            ability1Button.onClick.AddListener(() => {
                cleric.HealingMobile();
            });

            //Ability2
            ability2Button.onClick.AddListener(() => {
                cleric.BuffMobile();
            });

            //Ability3
            ultimateButton.onClick.AddListener(() => {
                cleric.ReviveMobile();
            });
        }
    }

    public void SaveGame()
    {
        if (player == null) return;

        GameManager.Instance.SaveGameSession();
    }

    public void CameraBttn(bool value)
    {
        previous.gameObject.SetActive(value);
        next.gameObject.SetActive(value);

        if(value)
        {
            uiImages[0].gameObject.SetActive(false);
            uiImages[1].gameObject.SetActive(false);
            uiImages[2].gameObject.SetActive(false);
            mobileMoveStick.SetActive(false);
            mobileAimStick.SetActive(false);
            basicAttackButton.gameObject.SetActive(false);
            ability1Button.gameObject.SetActive(false);
            ability2Button.gameObject.SetActive(false);
            ultimateButton.gameObject.SetActive(false);
        }
        else
        {
            uiImages[0].gameObject.SetActive(!Application.isMobilePlatform);
            uiImages[1].gameObject.SetActive(!Application.isMobilePlatform);
            uiImages[2].gameObject.SetActive(!Application.isMobilePlatform);
            mobileMoveStick.SetActive(Application.isMobilePlatform);
            mobileAimStick.SetActive(Application.isMobilePlatform);
            basicAttackButton.gameObject.SetActive(Application.isMobilePlatform);
            ability1Button.gameObject.SetActive(Application.isMobilePlatform);
            ability2Button.gameObject.SetActive(Application.isMobilePlatform);
            ultimateButton.gameObject.SetActive(Application.isMobilePlatform);
        }
    }

    private void OnNextPressed()
    {
        if (player == null) return;

        player.NextCamera();
    }

    private void OnPreviousPressed()
    {
        if (player == null) return;

        player.PreviousCamera();
    }

    private void OnReadyButtonPressed()
    {
        if (player == null) 
        {
            return;
        }

        player.ToggleReadyStateServerRpc();

        localReadyState = !localReadyState;

        if (inLobby)
        {
            readyButtonText.text = localReadyState ? "Cancel" : "Start Game";
        }
        else
        {
            readyButtonText.text = localReadyState ? "Cancel" : "Next Level";
        }
    }

    private void OnLevelUpButtonPressed()
    {
        if (player == null) return;

        int level = player.GetPlayerLevel();
        int playerCoins = player.GetCoins();
        int costToLevelUp = GetLevelUpCost(level);
        level = level + 1;

        player.SpendCoinsServerRpc(costToLevelUp);
        player.SetLevelServerRpc(level);

        LevelUpTextAndInteractable(level, playerCoins, GetLevelUpCost(level));
    }

    public void LevelUpActive()
    {
        if (player == null) return;

        if (localReadyState)
        {
            OnReadyButtonPressed();
        }
        readyButton.gameObject.SetActive(false);

        levelUpButton.gameObject.SetActive(true);
        int level = player.GetPlayerLevel();
        int playerCoins = player.GetCoins();

        int costToLevelUp = GetLevelUpCost(level);

        LevelUpTextAndInteractable(level, playerCoins, costToLevelUp);
    }

    private void LevelUpTextAndInteractable(int level, int coins, int tolevelUp)
    {
        if (level == 5)
        {
            levelUpButton.interactable = false;
            levelUpText.text = $"Level Max!";
        }
        else if (coins >= tolevelUp)
        {
            levelUpButton.interactable = true;
            levelUpText.text = $"Spend {tolevelUp} coins to Level Up";
        }
        else
        {
            levelUpButton.interactable = false;
            levelUpText.text = "Not Enough Coins to Level Up";
        }
    }

    private int GetLevelUpCost(int level)
    {
        switch (level)
        {
            case 1: return 300;
            case 2: return 500;
            case 3: return 700;
            default: return 1000;
        }
    }

    public void LevelUpDesactive()
    {
        levelUpButton.gameObject.SetActive(false);
        readyButton.gameObject.SetActive(true);
    }

    public IEnumerator ShowReadyButtonAfterDelay()
    {
        yield return new WaitForSeconds(1f); 
        readyButton.gameObject.SetActive(true);
    }


    public void Exit()
    {
        if (NetworkManager.Singleton.IsHost || NetworkManager.Singleton.IsClient)
        {
            NetworkManager.Singleton.Shutdown();
        }

        Application.Quit();
    }

    public void MainMenu()
    {
        if (NetworkManager.Singleton.IsHost || NetworkManager.Singleton.IsClient)
        {
            NetworkManager.Singleton.Shutdown();
        }

        ManagerUI.Instance.GoToMainMenu();
    }

    public void SetReadyButton(bool value)
    {
        if(!value && localReadyState)
        {
            OnReadyButtonPressed();
        }

        readyButton.gameObject.SetActive(value);
    }

    public void SetName(string name)
    {
        nameText.text = name;
    }

    public void SetFloor(string name)
    {
        floorText.text = name;
    }

    public void LoadingScreen()
    {
        game.SetActive(false);
        inLobby = false;
        OnReadyButtonPressed();
        loading.SetActive(true);
    }

    public void OptionsScreen()
    {
        game.SetActive(false);
        options.SetActive(true);
        volumeSlider.value = SoundManager.Instance.GetCurrentValue();
        if (NetworkManager.Singleton.IsHost)
        {
            saveGame.gameObject.SetActive(true);
        }
        else
        {
            saveGame.gameObject.SetActive(false);
        }
    }

    public void ChangeVolume(float value)
    {
        SoundManager.Instance.SetVolume(value);
    }

    public void GameScreen()
    {
        ActivateGame();
        SetReadyButton(false);
    }

    public void ToNexus()
    {
        game.SetActive(false);
        SetReadyButton(false);
        playersDead.SetActive(true);
    }

    public void VictoryToNexus()
    {
        game.SetActive(false);
        SetReadyButton(false);
        winPanel.SetActive(true);
    }

    public void GameScreenFromPlayersDeadOrWin()
    {
        ActivateGame();
        inLobby = true;
        readyButtonText.text = localReadyState ? "Cancel" : "Start Game";
        readyButton.gameObject.SetActive(true);
        CameraBttn(false);
    }

    public void GameScreenFromOptions()
    {
        ActivateGame();
        SetReadyButton(inLobby);
    }


    public void ActivateGame()
    {
        options.SetActive(false);
        loading.SetActive(false);
        playersDead.SetActive(false);
        winPanel.SetActive(false);
        game.SetActive(true);
    }

    public void SetImages()
    {
        if (player == null) return;

        int aux = 0;

        if(player is Mage)
        {
            aux = 1;
        }else if(player is Cleric) 
        {
            aux = 2;
        }

        if (Application.isMobilePlatform)
        {
            ability1Button.image.sprite = imageSet1[aux];
            ability2Button.image.sprite = imageSet2[aux];
            ultimateButton.image.sprite = imageSet3[aux];
        }
        else
        {
            uiImages[0].sprite = imageSet1[aux];
            uiImages[1].sprite = imageSet2[aux];
            uiImages[2].sprite = imageSet3[aux];
        }
    }

    public void setImage(int idx, bool normal)
    {
        if (player == null) return;

        int aux = 0;

        if (player is Mage)
        {
            aux = 1;
        }
        else if (player is Cleric)
        {
            aux = 2;
        }

        if(Application.isMobilePlatform)
        {
            switch (idx)
            {
                case 0:
                    ability1Button.image.sprite = NormalOrBW(aux, idx, normal);
                    break;
                case 1:
                    ability2Button.image.sprite = NormalOrBW(aux, idx, normal);
                    break;
                case 2:
                    ultimateButton.image.sprite = NormalOrBW(aux, idx, normal);
                    break;
            }
        }
        else
        {
            uiImages[idx].sprite = NormalOrBW(aux, idx, normal);
        }
    }

    public void UpdateImages(int level)
    {
        if (player == null) return;

        int aux = 0;

        if (player is Mage)
        {
            aux = 1;
        }
        else if (player is Cleric)
        {
            aux = 2;
        }

        if (Application.isMobilePlatform)
        {
            switch (level)
            {
                case 1:
                    ability1Button.image.sprite = NormalOrBW(aux, 0, false);
                    ability2Button.image.sprite = NormalOrBW(aux, 1, false);
                    ultimateButton.image.sprite = NormalOrBW(aux, 2, false);

                    ability1Button.interactable = false;
                    ability2Button.interactable = false;
                    ultimateButton.interactable = false;
                    break;
                case 2:

                    ability1Button.image.sprite = NormalOrBW(aux, 0, true);
                    ability2Button.image.sprite = NormalOrBW(aux, 1, false);
                    ultimateButton.image.sprite = NormalOrBW(aux, 2, false);

                    ability1Button.interactable = true;
                    ability2Button.interactable = false;
                    ultimateButton.interactable = false;
                    break;
                case 3:
                    ability1Button.image.sprite = NormalOrBW(aux, 0, true);
                    ability2Button.image.sprite = NormalOrBW(aux, 1, true);
                    ultimateButton.image.sprite = NormalOrBW(aux, 2, false);

                    ability1Button.interactable = true;
                    ability2Button.interactable = true;
                    ultimateButton.interactable = false;
                    break;
                default:
                    ability1Button.image.sprite = NormalOrBW(aux, 0, true);
                    ability2Button.image.sprite = NormalOrBW(aux, 1, true);
                    ultimateButton.image.sprite = NormalOrBW(aux, 2, true);

                    ability1Button.interactable = true;
                    ability2Button.interactable = true;
                    ultimateButton.interactable = true;
                    break;
            }
        }
        else
        {
            switch (level)
            {
                case 1:
                    uiImages[0].sprite = NormalOrBW(aux, 0, false);
                    uiImages[1].sprite = NormalOrBW(aux, 1, false);
                    uiImages[2].sprite = NormalOrBW(aux, 2, false);

                    break;
                case 2:
                    uiImages[0].sprite = NormalOrBW(aux, 0, true);
                    uiImages[1].sprite = NormalOrBW(aux, 1, false);
                    uiImages[2].sprite = NormalOrBW(aux, 2, false);
                    break;
                case 3:
                    uiImages[0].sprite = NormalOrBW(aux, 0, true);
                    uiImages[1].sprite = NormalOrBW(aux, 1, true);
                    uiImages[2].sprite = NormalOrBW(aux, 2, false);
                    break;
                default:
                    uiImages[0].sprite = NormalOrBW(aux, 0, true);
                    uiImages[1].sprite = NormalOrBW(aux, 1, true);
                    uiImages[2].sprite = NormalOrBW(aux, 2, true);
                    break;
            }
        }
    }

    public Sprite NormalOrBW(int idx, int whichImage, bool normal)
    {
        switch (whichImage)
        {
            case 0:
                if (normal) return imageSet1[idx];
                else return imageBWSet1[idx];
            case 1:
                if (normal) return imageSet2[idx];
                else return imageBWSet2[idx];

            case 2:
                if (normal) return imageSet3[idx];
                else return imageBWSet3[idx];
        }
        return null;
    }
}
