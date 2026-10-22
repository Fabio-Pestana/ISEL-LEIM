using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;


public class CanvasController : MonoBehaviour
{
    public GameObject inGame;
    public GameObject pausedGame;
    public GameObject retryMenu;
    public GameObject optionsMenu;
    public GameObject endMenu;

    public Image sword;
    public Image noSword;
    public Image heal;
    public Image interactBack;
    public Image noHeal;
    public TextMeshProUGUI numberHeals;
    public TextMeshProUGUI toInteract;
    public TextMeshProUGUI money;

    public TextMeshProUGUI keyboardSword;
    public TextMeshProUGUI keyboardHeal;

    public Slider mouseSense;
    public Slider music;
    public AudioSource terrain;

    public Player3P script;
    public int Heals;
    public int Money;
    private string money_str= "Press E to get money";
    private string sword_str = "Press E to lift the sword";
    private string shop_str = "Press E to Buy Heals, 25 coins for one heal";
    private string shop_no_money_str = "No money for heals";
    public static bool isPaused = false;
    private List<AudioSource> allAudioSources;

    // Start is called before the first frame update
    void Start()
    {
        StartGame();
        inGame.SetActive(true);
        pausedGame.SetActive(false);
        optionsMenu.SetActive(false);
        retryMenu.SetActive(false);
        endMenu.SetActive(false);
        allAudioSources = new List<AudioSource>(FindObjectsOfType<AudioSource>());
    }

    // Update is called once per frame
    void Update()
    {
        if (script.IsDead() && !retryMenu.activeSelf)
        {
            RetryMenu();
        }else if (script.EndGame() && !endMenu.activeSelf)
        {
            EndMenu();
        }
        else if (!optionsMenu.activeSelf)
        {
             if (Input.GetKeyUp(KeyCode.Escape))
             {
                if(isPaused)
                {
                    ResumeGame();
                }
                else{
                    PausedMenu();
                }
             }

             if (!isPaused)
             {
                GameInterface();
             }
        } 
    }

    public void StartGame()
    {
        noHeal.enabled = true;
        heal.enabled = false;
        sword.enabled = false;
        noSword.enabled = true;
        numberHeals.enabled = false;
        toInteract.enabled = false;
        interactBack.enabled = false;
        Heals = script.getNumberHeals();
        Money = script.getMoney();
        money.text = Money.ToString();
    }

    public void GameInterface()
    {
        if (Heals != script.getNumberHeals())
        {
            Heals = script.getNumberHeals();
            numberHeals.text = Heals.ToString();
        }

        if (Money != script.getMoney())
        {
            Money = script.getMoney();
            money.text = Money.ToString();
        }

        if (script.isMoneyClose())
        {
            toInteract.text = money_str;
            toInteract.enabled = true;
            interactBack.enabled = true;
        }
        else if (script.isShopClose())
        {
            toInteract.text = shop_str;
            if (Money < 25)
                toInteract.text = shop_no_money_str;

            toInteract.enabled = true;
            interactBack.enabled = true;
        }
        else if (script.isSwordPlace())
        {
            toInteract.text = sword_str;
            toInteract.enabled = true;
            interactBack.enabled = true;
        }
        else
        {
            toInteract.enabled = false;
            interactBack.enabled = false;
        }

        if (Heals <= 0)
        {
            noHeal.enabled = true;
            heal.enabled = false;
            numberHeals.enabled = false;
            keyboardHeal.enabled = false;
        }
        else
        {
            noHeal.enabled = false;
            heal.enabled = true;
            numberHeals.enabled = true;
            keyboardHeal.enabled = true;
        }

        if (script.isSwordActive())
        {
            noSword.enabled = false;
            sword.enabled = true;
            keyboardSword.text = "2";
        }
        else
        {
            noSword.enabled = true;
            sword.enabled = false;
            keyboardSword.text = "1";
        }
    }




    public void PausedMenu()
    {
        Cursor.lockState = CursorLockMode.None;
        isPaused = true;
        inGame.SetActive(false);
        pausedGame.SetActive(true);

        Time.timeScale = 0f;
    }

    public void ResumeGame()
    {
        Cursor.lockState = CursorLockMode.Locked;
        StartGame();
        isPaused = false;
        pausedGame.SetActive(false);
        inGame.SetActive(true);

        Time.timeScale = 1f;
    }

    public void BackTotheMenu()
    {
        Cursor.lockState = CursorLockMode.None;
        Time.timeScale = 1f;
        isPaused = false;
        SceneManager.LoadScene("Menu");
    }

    public void PlayAgain()
    {
        Cursor.lockState = CursorLockMode.Locked;
        Time.timeScale = 1f;
        isPaused = false;
        SceneManager.LoadScene("Project");
    }

    public void Quit()
    {
        Application.Quit();
    }

    public void Options()
    {
        pausedGame.SetActive(false);
        optionsMenu.SetActive(true);
    }

    public void RetryMenu()
    {
        Cursor.lockState = CursorLockMode.None;
        inGame.SetActive(false);
        retryMenu.SetActive(true);
        Time.timeScale = 0f;
    }

    public void EndMenu()
    {
        Cursor.lockState = CursorLockMode.None;
        inGame.SetActive(false);
        endMenu.SetActive(true);
        Time.timeScale = 0f;
    }

    public void ChangeVolume()
    {
        foreach (AudioSource audioSource in allAudioSources)
        {
            audioSource.volume = music.value;
        }
    }

    public void ChangeSensitivity()
    {
        script.turningSpeed = mouseSense.value; 
    }

    public void GoBack()
    {
        optionsMenu.SetActive(false);
        pausedGame.SetActive(true);
    }
}
