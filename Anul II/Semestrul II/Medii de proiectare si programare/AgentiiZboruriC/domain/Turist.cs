namespace AgentiiZboruriC.domain;

public class Turist : Persoana
{
    public int? NrClient { get; set; }

    public Turist(int id, string nume, string prenume, int? nrClient) : base(id, nume, prenume)
    {
        this.NrClient = nrClient;
    }

    public override string ToString()
    {
        return $"Turist{{nrClient={NrClient}}}";
    }

    public override bool Equals(object obj)
    {
        if (this == obj) return true;
        if (obj == null || GetType() != obj.GetType()) return false;

        Turist turist = (Turist)obj;

        return NrClient != null ? NrClient.Equals(turist.NrClient) : turist.NrClient == null;
    }

    public override int GetHashCode()
    {
        return NrClient != null ? NrClient.GetHashCode() : 0;
    }
}