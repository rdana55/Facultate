package ro.iss.ma.domain;

public class Persoana extends Entity<Integer>{
    private String nume;
    private String prenume;
    private String username;
    private String password;

    public Persoana(Integer id, String nume, String prenume, String username, String password) {
        super(id);
        this.nume = nume;
        this.prenume = prenume;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Persoana{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Persoana persoana = (Persoana) o;

        if (!nume.equals(persoana.nume)) return false;
        if (!prenume.equals(persoana.prenume)) return false;
        if (!username.equals(persoana.username)) return false;
        return password.equals(persoana.password);
    }
}
