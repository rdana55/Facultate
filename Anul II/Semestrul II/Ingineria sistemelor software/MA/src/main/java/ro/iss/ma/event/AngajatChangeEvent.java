package ro.iss.ma.event;

import ro.iss.ma.domain.Angajat;
import ro.iss.ma.domain.Sarcina;

import java.util.Optional;

public class AngajatChangeEvent implements Event {
    private EventType type;
    private Optional<Angajat> angajat;

    private Sarcina sarcina;

    public AngajatChangeEvent(EventType type, Optional<Angajat> angajat) {
        this.type = type;
        this.angajat = angajat;
    }

    public AngajatChangeEvent(EventType type, Optional<Angajat> angajat, Sarcina sarcina) {
        this.type = type;
        this.angajat = angajat;
        this.sarcina = sarcina;
    }

    public EventType getType() {
        return type;
    }

    public Optional<Angajat> getAngajat() {
        return angajat;
    }

    public Sarcina getSarcina() {
        return sarcina;
    }
}

