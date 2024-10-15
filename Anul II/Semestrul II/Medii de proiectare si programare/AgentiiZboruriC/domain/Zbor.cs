namespace AgentiiZboruriC.domain;
using System;

[Serializable]
public class Zbor : Entity<int>
{
    internal string From;
    internal string To;
    public DateTime DataOra;
    public int LocuriDisponibile;

    public Zbor(int id, string from, string to, DateTime dataOra, int locuriDisponibile) : base(id)
    {
        this.From = from;
        this.To = to;
        this.DataOra = dataOra;
        this.LocuriDisponibile = locuriDisponibile;
    }

    public int GetId()
    {
        return base.GetId();
    }

    public string GetFrom()
    {
        return From;
    }

    public string GetTo()
    {
        return To;
    }

    public DateTime GetDataOra()
    {
        return DataOra;
    }

    public int GetLocuriDisponibile()
    {
        return LocuriDisponibile;
    }

    public override string ToString()
    {
        return $"Zbor{{from='{From}', to='{To}', dataOra={DataOra}, locuriDisponibile={LocuriDisponibile}}}";
    }

    public void SetLocuriDisponibile(int locuriDisponibile)
    {
        this.LocuriDisponibile = locuriDisponibile;
    }

    public void SetDataOra(DateTime dataOra)
    {
        this.DataOra = dataOra;
    }

    public void SetTo(string to)
    {
        this.To = to;
    }

    public void SetFrom(string from)
    {
        this.From = from;
    }

    public override bool Equals(object obj)
    {
        if (this == obj) return true;
        if (obj == null || GetType() != obj.GetType()) return false;

        Zbor zbor = (Zbor)obj;

        return From == zbor.From && To == zbor.To && DataOra == zbor.DataOra && LocuriDisponibile == zbor.LocuriDisponibile;
    }

    public override int GetHashCode()
    {
        return HashCode.Combine(From, To, DataOra, LocuriDisponibile);
    }
}
