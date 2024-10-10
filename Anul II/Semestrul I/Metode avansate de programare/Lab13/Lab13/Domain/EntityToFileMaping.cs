namespace Lab13.Domain;

public class EntityToFileMaping
{
    public static Document CreateDocument(string line)
    {
        string[] fields = line.Split(',');
        Document document = new Document()
        {
            id = fields[0],
            Name = fields[1],
            Date = DateTime.Parse(fields[2])
        };
        return document;
    }

    public static Factura CreateFactura(string line)
    {
        string[] fields = line.Split(',');
        Factura factura = new Factura()
        {
            id = fields[0],
            Name = fields[1],
            Date = DateTime.Parse(fields[2]),
            Achizitii = new List<String>(),
            DataScadenta = DateTime.Parse(fields[4]),
            Categorie = (Categorii)Enum.Parse(typeof(Categorii), fields[5])
        };
        string[] achizitiiSTR = fields[3].Split(';');
        foreach (var i in achizitiiSTR)
        {
            factura.Achizitii.Add(i);
        }
        return factura;
    }

    public static Achizitie CreateAchizitie(string line)
    {
        string[] fields = line.Split(',');
        Achizitie achizitie = new Achizitie()
        {
            id = fields[0],
            Produs = fields[1],
            PretProdus = Double.Parse(fields[2]),
            Cantitate = int.Parse(fields[3]),
            Factura = fields[4]
        };
        return achizitie;
    }
}