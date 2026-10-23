using Unity.Netcode.Components;
using UnityEngine;

public class ClientNetAnimator : NetworkAnimator
{
    protected override bool OnIsServerAuthoritative()
    {
        return false;
    }
}
