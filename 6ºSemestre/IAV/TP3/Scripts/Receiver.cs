using UnityEngine;
using extOSC;

public class Receiver : MonoBehaviour
{
    public OSCReceiver receiver;

    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        receiver = gameObject.AddComponent<OSCReceiver>();
        receiver.LocalPort = 7001;
        receiver.Bind("/1/fader1", MessageReceiver);
        receiver.Bind("/1/fader2", MessageReceiver2);
        receiver.Bind("/accxyz", ReceiveAcc);
    }

    void ReceiveAcc(OSCMessage message)
    {
        float x = message.Values[0].FloatValue;
        float y = message.Values[1].FloatValue;
        float z = message.Values[2].FloatValue;

        Debug.Log("Acc = " + new Vector3(x, y, z));
        /*
        Vector3 v;
        message.ToVector3(out v);
        Debug.Log(v);
        */
    }

    void MessageReceiver(OSCMessage message)
    {
        Debug.Log("FADER1 = " + message.Values[0].FloatValue);
    }

    void MessageReceiver2(OSCMessage message)
    {
        Debug.Log("FADER2 = " + message.Values[0].FloatValue);
    }
}
