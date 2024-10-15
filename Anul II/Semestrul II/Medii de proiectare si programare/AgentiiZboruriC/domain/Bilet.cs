namespace AgentiiZboruriC.domain;

using System;

[Serializable]
public class Bilet : Entity<int>
{
    internal Angajat Angajat;
    internal Zbor Zbor;
    internal Persoana Persoana;

    public Bilet(int id, Angajat angajat, Zbor zbor, Persoana persoana) : base(id)
    {
        this.Angajat = angajat;
        this.Zbor = zbor;
        this.Persoana = persoana;
    }

    public Angajat GetAngajat()
    {
        return Angajat;
    }

    public Zbor GetZbor()
    {
        return Zbor;
    }

    public Persoana GetPersoana()
    {
        return Persoana;
    }

    public override string ToString()
    {
        return $"Bilet{{angajat={Angajat}, zbor={Zbor}, persoana={Persoana}}}";
    }

    public void SetAngajat(Angajat angajat)
    {
        this.Angajat = angajat;
    }

    public void SetZbor(Zbor zbor)
    {
        this.Zbor = zbor;
    }

    public void SetPersoana(Persoana persoana)
    {
        this.Persoana = persoana;
    }

    public override bool Equals(object obj)
    {
        if (this == obj) return true;
        if (obj == null || GetType() != obj.GetType()) return false;

        Bilet bilet = (Bilet)obj;

        return Angajat == bilet.Angajat && Zbor == bilet.Zbor && Persoana == bilet.Persoana;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(Angajat, Zbor, Persoana);
    }

    public int GetId()
    {
        return base.GetId();
    }

    public void SetId(int id)
    {
        base.SetId(id);
    }
}
