namespace Lab13.Domain;

public class FacturaDTO
{
    public String Name { get; set; }
    public int Count { get; set; }

    public FacturaDTO(string name, int count)
    {
        Name = name;
        Count = count;
    }

    public override string ToString()
    {
        return Name + ' ' + Count;
    }
}