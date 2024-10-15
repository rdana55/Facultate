package ro.model;

public class Turist extends Persoana{
    Integer nrClient;

    public Turist(Integer id, String nume, String prenume, Integer nrClient) {
        super(id, nume, prenume);
        this.nrClient = nrClient;
    }

    public Integer getNrClient() {
        return nrClient;
    }

    public void setNrClient(Integer nrClient) {
        this.nrClient = nrClient;
    }

    @Override
    public String toString() {
        return "Turist{" +
                "nrClient=" + nrClient +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Turist turist = (Turist) o;

        return nrClient != null ? nrClient.equals(turist.nrClient) : turist.nrClient == null;
    }

    @Override
    public int hashCode() {
        return nrClient != null ? nrClient.hashCode() : 0;
    }

    public String getNume() {
        return super.getNume();
    }

    public void setNume(String nume) {
        super.setNume(nume);
    }

    public String getPrenume() {
        return super.getPrenume();
    }

    public void setPrenume(String prenume) {
        super.setPrenume(prenume);
    }
}
