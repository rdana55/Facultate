package ro.iss.ma.domain;

public class Sarcina extends Entity<Integer> {
    private Angajat angajat;
    private String descriere;
    private Stare stare;


    public Sarcina(Angajat angajat, String descriere, Stare stare) {
        super();
        this.angajat = angajat;
        this.descriere = descriere;
        this.stare = stare;
    }


    public Sarcina(Integer id, Angajat angajat, String descriere, Stare stare) {
        super(id);
        this.angajat = angajat;
        this.descriere = descriere;
        this.stare = stare;
    }


    public Angajat getAngajat() {
        return angajat;
    }

    public String getDescriere() {
        return descriere;
    }

    public Stare getStare() {
        return stare;
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setStare(Stare stare) {
        this.stare = stare;
    }

    @Override
    public String toString() {
        return "Sarcina{" +
                "angajat=" + angajat +
                ", descriere='" + descriere + '\'' +
                ", stare=" + stare +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sarcina sarcina = (Sarcina) o;
        return angajat.equals(sarcina.angajat) &&
                descriere.equals(sarcina.descriere) &&
                stare == sarcina.stare;
    }

}
