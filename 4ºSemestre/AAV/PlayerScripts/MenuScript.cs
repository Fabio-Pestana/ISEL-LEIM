using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MenuScript : MonoBehaviour
{
    public GameObject mainMenu;
    public GameObject creditsMenu;

    // Start is called before the first frame update
    void Start()
    {
        mainMenu.SetActive(true);
        creditsMenu.SetActive(false);
    }

    public void Play()
    {
        Cursor.lockState = CursorLockMode.Locked;
        SceneManager.LoadScene("Project");
    }

    public void Quit()
    {
        Application.Quit();
    }

    public void Credits()
    {
        mainMenu.SetActive(false);
        creditsMenu.SetActive(true);
    }

    public void GoBack()
    {
        creditsMenu.SetActive(false);
        mainMenu.SetActive(true);
    }
}
