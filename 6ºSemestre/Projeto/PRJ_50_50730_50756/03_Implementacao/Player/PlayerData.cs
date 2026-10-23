using System;
using Unity.Netcode;
using UnityEngine;

public class PlayerData : NetworkBehaviour
{
    public NetworkVariable<int> selectedCharacter = new NetworkVariable<int>(-1);

    public override void OnNetworkSpawn()
    {
        base.OnNetworkSpawn();

        if (IsOwner)
        {
            // Envia a selecao de personagem para o servidor
            int charIndex = GetCharacterIndex();
            UpdateCharacterSelectionServerRpc(charIndex);
        }
    }

    public int GetCharacterIndex()
    {
        string name = ManagerUI.Instance.GetClassName();

        if (name == "Knight") return 0;
        if (name == "Mage") return 1;
        if (name == "Cleric") return 2;
        return -1;
    }

    [ServerRpc]
    private void UpdateCharacterSelectionServerRpc(int characterIndex)
    {
        selectedCharacter.Value = characterIndex;
    }
}