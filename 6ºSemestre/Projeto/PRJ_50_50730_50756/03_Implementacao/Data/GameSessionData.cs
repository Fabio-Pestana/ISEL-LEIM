using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameSessionData
{
    public string SessionName;
    public bool IsMultiplayer;
    public List<PlayerSessionData> Players;

    public GameSessionData(string sessionName, bool isMultiplayer, List<PlayerSessionData> players)
    {
        SessionName = sessionName;
        IsMultiplayer = isMultiplayer;
        Players = players;
    }
}
