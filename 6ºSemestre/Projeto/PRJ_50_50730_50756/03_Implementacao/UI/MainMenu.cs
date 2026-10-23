using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class MainMenu : MonoBehaviour
{
    [SerializeField]
    private GameObject mainMenuCanvas;

    [SerializeField]
    private GameObject userCreationCanvas;

    [SerializeField]
    private GameObject options;

    private string nameText;

    [SerializeField] 
    private Slider volumeSlider;


    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        UserData userData = SaveSystem.LoadUser();

        if (userData != null)
        {
            ShowMainMenu();
        }
        else
        {
            ShowUserCreation();
        }
    }

    public void ShowMainMenu()
    {
        mainMenuCanvas.SetActive(true);
        options.SetActive(false);
        userCreationCanvas.SetActive(false);
    }

    private void ShowUserCreation()
    {
        mainMenuCanvas.SetActive(false);
        userCreationCanvas.SetActive(true);
    }

    public void ShowOptions()
    {
        mainMenuCanvas.SetActive(false);
        options.SetActive(true);
        volumeSlider.value = SoundManager.Instance.GetCurrentValue();
    }

    public void updateName(string value)
    {
        nameText = value.Trim();
    }

    public void OnConfirmUsername()
    {
        if (!string.IsNullOrEmpty(nameText))
        {
            SaveSystem.SaveUser(nameText);
            ShowMainMenu();
        }
    }

    public void GoToNexus()
    {
        UnityEngine.SceneManagement.SceneManager.LoadScene("Nexus");
    }

    public void GoToNexusSinglePlayer()
    {
        UnityEngine.SceneManagement.SceneManager.LoadScene("NexusSinglePlayer");
    }

    public void ExitGame()
    {
        Application.Quit();
    }

}
