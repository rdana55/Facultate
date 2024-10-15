package ro.model;

public class Bilet extends Entity<Integer>{
    Angajat angajat;
    Zbor zbor;
    Persoana persoana;

    public Bilet(Integer along, Angajat angajat, Zbor zbor, Persoana persoana) {
        super(along);
        this.angajat = angajat;
        this.zbor = zbor;
        this.persoana = persoana;
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public Zbor getZbor() {
        return zbor;
    }

    public Persoana getPersoana() {
        return persoana;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "angajat=" + angajat +
                ", zbor=" + zbor +
                ", persoana=" + persoana +
                '}';
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public void setZbor(Zbor zbor) {
        this.zbor = zbor;
    }

    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bilet bilet = (Bilet) o;

        if (angajat != null ? !angajat.equals(bilet.angajat) : bilet.angajat != null) return false;
        if (zbor != null ? !zbor.equals(bilet.zbor) : bilet.zbor != null) return false;
        return persoana != null ? persoana.equals(bilet.persoana) : bilet.persoana == null;
    }

    public int hashCode() {
        int result = angajat != null ? angajat.hashCode() : 0;
        result = 31 * result + (zbor != null ? zbor.hashCode() : 0);
        result = 31 * result + (persoana != null ? persoana.hashCode() : 0);
        return result;
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
    }

}
