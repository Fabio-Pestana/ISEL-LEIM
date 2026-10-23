using UnityEngine;


[System.Serializable]
public class PlayerSessionData
{
    public string PlayerID;
    public int Level;
    public int Coins;
    public enum PlayerClass
    {
        Mage,
        Cleric,
        Knight
    }

    public PlayerClass Class;

    public PlayerSessionData(string playerID, int level, int coins, PlayerClass playerClass)
    {
        PlayerID = playerID;
        Level = level;
        Coins = coins;
        Class = playerClass;
    }
}