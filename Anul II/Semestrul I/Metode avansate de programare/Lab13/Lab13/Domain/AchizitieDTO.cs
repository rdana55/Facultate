namespace Lab13.Domain;

public class AchizitieDTO
{
    public String nume { get; set; }
    public String numeFactura { get; set; }
    public String type { get; set; }

    public AchizitieDTO(String nume, String numeFactura, String type)
    {
        this.nume = nume;
        this.numeFactura = numeFactura;
        this.type = type;
    }

    public override string ToString()
    {
        return nume + ' ' + numeFactura + ' ' + type;
    }
}