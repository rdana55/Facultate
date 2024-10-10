using Lab13.Domain;

namespace Lab13.Repository;

class FacturaRepo: InFileRepository<string, Factura>
{
    public FacturaRepo(string filename) : base(filename,EntityToFileMaping.CreateFactura){}

    public List<Factura> getScadent()
    {
        List<Factura> filtered = new List<Factura>();
        foreach (var fac in FindAll().ToList())
        {
            if (fac.DataScadenta.Month == DateTime.Today.Month)
            {
                filtered.Add(fac);
            }
        }

        return filtered;
    }
    
    public Factura findOne(string id)
    {
        List<Factura> fac = FindAll().Where(o => id.Equals(o.id)).ToList();
        return fac[0];
    }
}