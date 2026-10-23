using extOSC;
using UnityEngine;

public class TouchOSCReceiver : MonoBehaviour
{
    public OSCReceiver receiver;
    private GameObject cube;
    private Vector3[] buffer = new Vector3[32];
    private int num = 0;
    private bool fader = true;
    private Vector3 localScale;

    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        cube = GetComponentInChildren<MeshRenderer>().gameObject;
        receiver = gameObject.AddComponent<OSCReceiver>();
        receiver.LocalPort = 7001;
        receiver.Bind("/1/fader1", CubeScaling);
        receiver.Bind("/accxyz", RecordAcc);
        receiver.Bind("/1/toggle1", SwitchMode);
        localScale = Vector3.one;
    }

    protected void CubeScaling(OSCMessage message)
    {
        message.ToFloat(out localScale.y);
    }

    void RecordAcc(OSCMessage message)
    {
        Vector3 acc;
        message.ToVector3(out acc);

        buffer[num++] = acc;
        if (num == 32) num = 0;        
    }

    Vector3 GetMeanAcc()
    {
        Vector3 sum = Vector3.zero;
        for(int i = 0; i < 32; i++)
        {
            sum += buffer[i];
        }
        return sum/32;
    }

    enum Orientation { FLAT, UP, SIDE};
    Orientation ori;


    void SwitchMode(OSCMessage message)
    {
        
        fader = (message.Values[0].FloatValue == 1);
        Debug.Log("Modo alterado: Fader = " + fader);
    }


    // Update is called once per frame
    void Update()
    {
        if(!fader)
        {
            Vector3 v = GetMeanAcc();

            float vx = Mathf.Abs(v.x);
            float vy = Mathf.Abs(v.y);
            float vz = Mathf.Abs(v.z);
            ori = Orientation.FLAT;
            if(vx > vz && vx > vy)
                ori = Orientation.SIDE;
            if(vy > vx && vy > vz)
                ori = Orientation.UP;

            Debug.Log(ori);

            switch (ori)
            {
                case Orientation.FLAT:
                    localScale = new Vector3(1, 0.1f, 1);
                    break;
                case Orientation.UP:
                    localScale = new Vector3(1, 1, 0.1f);
                    break;
                case Orientation.SIDE:
                    localScale = new Vector3(0.1f, 1, 1);
                    break;
            }
        }

        cube.transform.localScale = localScale;
    }
}
