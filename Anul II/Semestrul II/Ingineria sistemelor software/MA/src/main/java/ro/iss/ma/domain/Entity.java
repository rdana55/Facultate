package ro.iss.ma.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Entity<ID> implements Serializable {
    protected ID id;
    private static final Random random = new Random();

    public Entity() {
        this.id = generateRandomNaturalNumber();
    }

    public Entity(ID id) {
        this.id = id;
    }

    private ID generateRandomNaturalNumber() {
        int maxDigits = 999;
        int randomNumber = random.nextInt(maxDigits + 1);
        return (ID) Integer.valueOf(randomNumber);
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Entity { " + "id = " + id + " }";
    }
}

