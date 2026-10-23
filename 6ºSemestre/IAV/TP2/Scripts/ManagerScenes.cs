using TMPro;
using UnityEngine;
using UnityEngine.SceneManagement;

public class ManagerScenes : MonoBehaviour
{
    [SerializeField]
    private GameObject menuPanel;

    [SerializeField]
    private GameObject scenePanel;

    [SerializeField]
    private TMP_Text title;

    // Start is called before the first frame update
    void Start()
    {
        if (FindObjectsOfType<ManagerScenes>().Length > 1)
        {
            Destroy(gameObject);
            return;
        }

        DontDestroyOnLoad(this);
        menuPanel.SetActive(true);
        scenePanel.SetActive(false);
    }

    private void LoadScene(string sceneName)
    {
        SceneManager.LoadScene(sceneName);

        if (sceneName == "Scenes/Base Scene")
        {
            menuPanel.SetActive(true);
            scenePanel.SetActive(false);
        }
        else
        {
            menuPanel.SetActive(false);
            scenePanel.SetActive(true);
        }
    }

    public void HelloWorld()
    {
        title.text = "Hello World";
        LoadScene("Scenes/Hello World/HelloWorld");
    }

    public void FoodButton()
    {
        title.text = "Food Button";
        LoadScene("Scenes/Food Button/FoodButton");
    }

    public void ProceduralRoad()
    {
        title.text = "Procedural Road";
        LoadScene("Scenes/Roads/ProceduralRoad");
    }

    public void ProceduralRoad3D()
    {
        title.text = "Procedural Road 3D";
        LoadScene("Scenes/Roads/ProceduralRoad3D");
    }

    public void RayPerceptionSensor()
    {
        title.text = "Ray Perception Sensor";
        LoadScene("Scenes/RayPerceptionSensor/RayPerceptionSensor");
    }

    public void Exit()
    {
        Application.Quit();
    }

    public void Back()
    {
        LoadScene("Scenes/Base Scene");
    }
}
