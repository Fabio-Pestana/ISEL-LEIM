using Unity.Netcode.Components;
using UnityEngine;

public class ClientNetTransform : NetworkTransform
{
    protected override bool OnIsServerAuthoritative()
    {
        return false;
    }
}
