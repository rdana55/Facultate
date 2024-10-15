package ro.iss.ma.domain;

import java.time.LocalDateTime;

public class Angajat extends Persoana{

    private LocalDateTime intrare;
    private LocalDateTime iesire;
    public Angajat(Integer id, String nume, String prenume, String username, String password, LocalDateTime intrare, LocalDateTime iesire) {
        super(id, nume, prenume, username, password);
        this.intrare=intrare;
        this.iesire=iesire;
    }



    public LocalDateTime getIntrare() {
        return intrare;
    }

    public LocalDateTime getIesire() {
        return iesire;
    }

    public void setIntrare(LocalDateTime intrare) {
        this.intrare = intrare;
    }

    public void setIesire(LocalDateTime iesire) {
        this.iesire = iesire;
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "nume='" + getNume() + '\'' +
                ", prenume='" + getPrenume() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", intrare=" + intrare +
                ", iesire=" + iesire +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angajat angajat = (Angajat) o;
        return getNume().equals(angajat.getNume()) &&
                getPrenume().equals(angajat.getPrenume()) &&
                getUsername().equals(angajat.getUsername()) &&
                getPassword().equals(angajat.getPassword()) &&
                intrare.equals(angajat.intrare) &&
                iesire.equals(angajat.iesire);
    }
}
