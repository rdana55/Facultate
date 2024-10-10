namespace Lab13.Domain;

public class Factura : Document
{
    public DateTime DataScadenta { set; get; }
    public List<String> Achizitii { get; set; }
    public Categorii Categorie { get; set; }
    public override string ToString()
    {
        return base.ToString() + ' ' + DataScadenta + ' ' + string.Join(", ", Achizitii) + ' ' + Categorie;
    }

    public String getDTO()
    {
        return Name + ' ' + DataScadenta;
    }
}

public enum Categorii
{
    Utilities=1, Groceries, Fashion, Entertainment, Electronics
}
