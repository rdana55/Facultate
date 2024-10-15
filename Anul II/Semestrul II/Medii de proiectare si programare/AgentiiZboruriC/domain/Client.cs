namespace AgentiiZboruriC.domain;

using System;
using System.Collections.Generic;

[Serializable]
public class Client : Persoana
{
    internal string Adresa;

    public Client(int id, string nume, string prenume, string adresa) : base(id, nume, prenume)
    {
        this.Adresa = adresa;
    }

    public string GetAdresa()
    {
        return Adresa;
    }

    public void SetAdresa(string adresa)
    {
        this.Adresa = adresa;
    }
    
    public int GetId()
    {
        return base.GetId();
    }

    public void SetId(int id)
    {
        base.SetId(id);
    }

    public void SetNume(string nume)
    {
        base.SetNume(nume);
    }

    public void SetPrenume(string prenume)
    {
        base.SetPrenume(prenume);
    }
}
