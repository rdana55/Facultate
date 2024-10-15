package ro.model;

public class Persoana extends Entity<Integer>{
    private String nume;
    private String prenume;

    public Persoana(Integer id, String nume, String prenume) {
        super(id);
        this.nume = nume;
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    @Override
    public String toString() {
        return "Persoana{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
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

        Persoana persoana = (Persoana) o;

        if (nume != null ? !nume.equals(persoana.nume) : persoana.nume != null) return false;
        return prenume != null ? prenume.equals(persoana.prenume) : persoana.prenume == null;
    }

    @Override
    public int hashCode() {
        int result = nume != null ? nume.hashCode() : 0;
        result = 31 * result + (prenume != null ? prenume.hashCode() : 0);
        return result;
    }
}
