namespace Lab13.Domain;

public class Achizitie : Entity<String>
{
    public String Produs { get; set; }
    public int Cantitate { get; set; }
    public double PretProdus { get; set; }
    public string Factura { get; set; }

    public override string ToString()
    {
       return id + ' ' + Produs + ' ' + Cantitate + ' ' + PretProdus + ' ' + Factura;
    }
}