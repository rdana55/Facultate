package org.example.taximetrie.domain;

public class Sofer extends Persoana{

    private String indicativMasina;
    public Sofer(String username, String nume, String indicativMasina) {
        super(username, nume);
        this.indicativMasina=indicativMasina;
    }

    public String getIndicativMasina(){
        return indicativMasina;
    }

    public void setIndicativMasina(String indicativMasina){
        this.indicativMasina=indicativMasina;
    }

    @Override
    public String toString(){
        return "Sofer { " + "username = " + getUsername() + ", nume = " + getNume() + ", indicativMasina = " + indicativMasina + " }";
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof Sofer sofer)) return false;
        return getUsername().equals(sofer.getUsername());
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }
}
