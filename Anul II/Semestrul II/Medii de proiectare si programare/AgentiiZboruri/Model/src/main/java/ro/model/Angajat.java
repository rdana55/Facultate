package ro.model;

public class Angajat extends Persoana{
    private String username;
    private String password;

    public Angajat(Integer id, String nume, String prenume, String username, String password) {
        super(id, nume, prenume);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
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

        Angajat angajat = (Angajat) o;

        if (username != null ? !username.equals(angajat.username) : angajat.username != null) return false;
        return password != null ? password.equals(angajat.password) : angajat.password == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNume(String nume) {
        super.setNume(nume);
    }

    public void setPrenume(String prenume) {
        super.setPrenume(prenume);
    }

    public String getNume() {
        return super.getNume();
    }

    public String getPrenume() {
        return super.getPrenume();
    }

}
