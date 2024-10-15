package ro.model;

public class Client extends Persoana{
    private String adresa;

    public Client(Integer id, String nume, String prenume, String adresa) {
        super(id, nume, prenume);
        this.adresa = adresa;

    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "Client{" +
                "adresa='" + adresa + '\'' +
                '}';
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override

    public void setId(Integer id) {
        super.setId(id);
    }


    public void setNume(String nume) {
        super.setNume(nume);
    }

    public void setPrenume(String prenume) {
        super.setPrenume(prenume);
    }

}
