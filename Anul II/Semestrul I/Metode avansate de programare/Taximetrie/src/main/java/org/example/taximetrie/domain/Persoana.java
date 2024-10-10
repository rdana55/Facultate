package org.example.taximetrie.domain;

import java.util.Objects;

public class Persoana extends Entity<Long>{
    private String username;
    private String nume;

    public Persoana(String username, String nume){
        this.username=username;
        this.nume=nume;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume){
        this.nume=nume;
    }

    @Override
    public String toString(){
        return "Persoana { " + "username = " + username + ", nume = " + nume + " }";
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof Persoana persoana)) return false;
        return getUsername().equals(persoana.getUsername());
    }

    @Override
    public int hashCode(){
        return Objects.hash(getUsername());
    }

}
