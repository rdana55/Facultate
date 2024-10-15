namespace AgentiiZboruriC.domain;
using System;

[Serializable]
public class Angajat : Persoana
{
    internal string Username;
    internal string Password;

    public Angajat(int id, string nume, string prenume, string username, string password) : base(id, nume, prenume)
    {
        this.Username = username;
        this.Password = password;
    }

    public string GetUsername()
    {
        return Username;
    }

    public string GetPassword()
    {
        return Password;
    }

    public override string ToString()
    {
        return $"Angajat{{username='{Username}', password='{Password}'}}";
    }

    public int GetId()
    {
        return base.GetId();
    }

    public void SetId(int id)
    {
        base.SetId(id);
    }

    public override bool Equals(object obj)
    {
        if (this == obj) return true;
        if (obj == null || GetType() != obj.GetType()) return false;

        Angajat angajat = (Angajat)obj;

        return Username == angajat.Username && Password == angajat.Password;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(Username, Password);
    }

    public void SetUsername(string username)
    {
        this.Username = username;
    }

    public void SetPassword(string password)
    {
        this.Password = password;
    }

    public void SetNume(string nume)
    {
        base.SetNume(nume);
    }

    public void SetPrenume(string prenume)
    {
        base.SetPrenume(prenume);
    }

    public string GetNume()
    {
        return base.GetNume();
    }

    public string GetPrenume()
    {
        return base.GetPrenume();
    }
}
