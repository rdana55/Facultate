package ro.iss.ma.service;

import ro.iss.ma.controller.AngajatController;
import ro.iss.ma.domain.Sarcina;
import ro.iss.ma.domain.Sef;
import ro.iss.ma.domain.Angajat;
import ro.iss.ma.event.AngajatChangeEvent;
import ro.iss.ma.event.EventType;
import ro.iss.ma.observer.Observable;
import ro.iss.ma.observer.Observer;
import ro.iss.ma.repository.AngajatRepo;
import ro.iss.ma.repository.SarcinaRepo;
import ro.iss.ma.repository.SefRepo;

import java.util.*;

public class Service implements Observable<AngajatChangeEvent> {

    String url="jdbc:postgresql://localhost:5432/MonitorizareAngajatiISS";
    String username = "postgres";
    String password = "postgres";

    SefRepo sefRepo = new SefRepo(url, username, password);
    AngajatRepo angajatRepo = new AngajatRepo(url, username, password);

    SarcinaRepo sarcinaRepo = new SarcinaRepo(url, username, password);

    private final List<Observer<AngajatChangeEvent>> observers = new ArrayList<>();

    public Service(SefRepo sefRepo, AngajatRepo angajatRepo, SarcinaRepo sarcinaRepo) {
        this.sefRepo = sefRepo;
        this.angajatRepo = angajatRepo;
        this.sarcinaRepo = sarcinaRepo;
    }

    public boolean loginSef(String username, String password){
        return sefRepo.login(username,password);
    }
    public boolean loginAngajat(String username, String password){
        return angajatRepo.login(username,password);
    }

    public Optional<Sef> findOneS(String username){
        return sefRepo.findOne(username);
    }

    public Optional<Angajat> findOneA(String username){
        return angajatRepo.findOne(username);
    }

    public void addSarcina(Sarcina sarcina) {
        sarcinaRepo.save(sarcina);
    }

    public void updateSarcina(Sarcina sarcina) {
        sarcinaRepo.update(sarcina);
        AngajatChangeEvent event = new AngajatChangeEvent(EventType.SARCINA, Optional.ofNullable(sarcina.getAngajat()), sarcina);
        notifyObserver(event);
    }

    public void updateAngajatO(Angajat angajat){
        angajatRepo.update(angajat);
        AngajatChangeEvent event = new AngajatChangeEvent(EventType.LOGOUT, Optional.of(angajat));
        notifyObserver(event);
    }

    public void updateAngajatI(Angajat angajat){
        angajatRepo.update(angajat);
        AngajatChangeEvent event = new AngajatChangeEvent(EventType.LOGIN, Optional.of(angajat));
        notifyObserver(event);
    }


    public void deleteSarcina(Sarcina sarcina) {
        sarcinaRepo.delete(sarcina.getId());
        AngajatChangeEvent event = new AngajatChangeEvent(EventType.SARCINAD, Optional.ofNullable(sarcina.getAngajat()), sarcina);
        notifyObserver(event);
    }

    public List<Angajat> getAllAngajati() {
        return (List<Angajat>) angajatRepo.findAll();
    }

    public List<Sarcina> getAllSarcini() {
        return (List<Sarcina>) sarcinaRepo.findAll();
    }

    public List<Sarcina> getSarciniAngajat(Angajat angajat) {
        return (List<Sarcina>) sarcinaRepo.getSarciniAngajat(angajat);
    }
    @Override
    public void addObserver(Observer<AngajatChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<AngajatChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(AngajatChangeEvent event) {
        observers.forEach(x -> x.update(event));
    }
}