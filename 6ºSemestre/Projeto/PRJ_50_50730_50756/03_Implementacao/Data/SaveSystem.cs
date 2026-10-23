using UnityEngine;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using System.Collections.Generic;

public static class SaveSystem
{
    private static string userPath = Application.persistentDataPath + "/userData.txt";
    private static string sessionsPath = Application.persistentDataPath + "/sessions.dat";

    public static void SaveUser(string user)
    {
        BinaryFormatter formatter = new BinaryFormatter();
        FileStream stream = new FileStream(userPath, FileMode.Create);

        UserData data = new UserData(user, System.Guid.NewGuid().ToString());

        formatter.Serialize(stream, data);
        stream.Close();
    }

    public static UserData LoadUser()
    {
        if (File.Exists(userPath))
        {
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(userPath, FileMode.Open);

            UserData data = formatter.Deserialize(stream) as UserData;
            stream.Close();

            return data;
        }
        else
        {
            return null;
        }
    }

    public static void SaveGameSession(GameSessionData newSession)
    {
        List<GameSessionData> sessions = LoadAllGameSessions();
        sessions.Add(newSession);

        BinaryFormatter formatter = new BinaryFormatter();
        FileStream stream = new FileStream(sessionsPath, FileMode.Create);
        formatter.Serialize(stream, sessions);
        stream.Close();
    }

    public static List<GameSessionData> LoadAllGameSessions()
    {
        if (File.Exists(sessionsPath))
        {
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(sessionsPath, FileMode.Open);

            var sessions = formatter.Deserialize(stream) as List<GameSessionData>;
            stream.Close();
            return sessions;
        }
        else
        {
            return new List<GameSessionData>();
        }
    }

    public static void UpdateGameSession(GameSessionData updatedSession)
    {
        List<GameSessionData> sessions = LoadAllGameSessions();

        // Procurar idx da session com o mesmo nome
        int index = sessions.FindIndex(s => s.SessionName == updatedSession.SessionName);

        if (index != -1)
        {
            sessions[index] = updatedSession;

            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(sessionsPath, FileMode.Create);
            formatter.Serialize(stream, sessions);
            stream.Close();
        }
        else
        {
            SaveGameSession(updatedSession);
        }
    }

    public static GameSessionData GetSessionFromName(string name)
    {
        List<GameSessionData> sessions = LoadAllGameSessions();

        // Procurar idx da session com o mesmo nome
        GameSessionData session = sessions.Find(s => s.SessionName == name);

        return session;
    }


    public static List<GameSessionData> GetMultiplayerSessions()
    {
        return LoadAllGameSessions().FindAll(session => session.IsMultiplayer);
    }

    public static List<GameSessionData> GetSingleplayerSessions()
    {
        return LoadAllGameSessions().FindAll(session => !session.IsMultiplayer);
    }
}

