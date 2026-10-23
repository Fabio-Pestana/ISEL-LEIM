using UnityEngine;

[System.Serializable]
public class UserData
{
    public string UserID;
    public string UserName;

    public UserData(string user, string id)
    {
        UserID = id;
        UserName = user;
    }
}
