namespace AgentiiZboruriC.domain;

using System;

[Serializable]
public class Persoana : Entity<int>
{
    internal string Nume;
    internal string Prenume;

    public Persoana(int id, string nume, string prenume) : base(id)
    {
        this.Nume = nume;
        this.Prenume = prenume;
    }

    public string GetNume()
    {
        return Nume;
    }

    public void SetNume(string nume)
    {
        this.Nume = nume;
    }

    public string GetPrenume()
    {
        return Prenume;
    }

    public void SetPrenume(string prenume)
    {
        this.Prenume = prenume;
    }

    public override string ToString()
    {
        return $"Persoana{{nume='{Nume}', prenume='{Prenume}'}}";
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

        Persoana persoana = (Persoana)obj;

        return Nume == persoana.Nume && Prenume == persoana.Prenume;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(Nume, Prenume);
    }
}