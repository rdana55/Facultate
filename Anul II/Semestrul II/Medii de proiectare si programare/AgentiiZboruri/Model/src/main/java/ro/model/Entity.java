package ro.model;

import java.io.Serializable;

public class Entity <ID> implements Serializable {
    private ID id;

    public Entity(ID id) {
        this.id = id;
    }

    public Entity() {

    }

    public void setId(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }


}
