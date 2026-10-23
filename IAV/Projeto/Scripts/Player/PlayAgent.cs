using UnityEngine;

public class PlayAgent : MonoBehaviour
{
    [SerializeField]
    private GameObject text;

    [SerializeField]
    private GameObject env1;

    [SerializeField]
    private GameObject env2;

    [SerializeField]
    private TouchOSCReceiver receiver;

    [SerializeField]
    private GameObject cam;

    private bool _play;


    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        _play = true;
        text.SetActive(true);
        env1.SetActive(false);
        env2.SetActive(false);
    }

    void Update()
    {
        if (_play)
        {
            if (receiver.LeftBlock())
            {
                _play = false;
                cam.SetActive(false);
                text.SetActive(false);
                env1.SetActive(true);
            }
            else if (receiver.RightBlock())
            {
                _play = false;
                cam.SetActive(false);
                text.SetActive(false);
                env2.SetActive(true);
            }
        }
    }
}
