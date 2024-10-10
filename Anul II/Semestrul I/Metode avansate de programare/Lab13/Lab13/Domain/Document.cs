namespace Lab13.Domain;

public class Document : Entity<string>
{
    public String Name { set; get; }
    public DateTime Date { set; get; }

    public override string ToString()
    {
        return id + ' ' + Name + ' ' + Date;
    }
    
    public String getDTO()
    {
        return Name + ' ' + Date;
    }
}