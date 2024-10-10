package org.example.taximetrie.service;

import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.domain.Sofer;
import org.example.taximetrie.events.ClientChangeEvent;
import org.example.taximetrie.events.Event;
import org.example.taximetrie.observer.Observable;
import org.example.taximetrie.observer.Observer;
import org.example.taximetrie.repository.PersoanaRepo;
import org.example.taximetrie.repository.SoferRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoferService implements Observable<Event> {
    private SoferRepo repo;
    private List<Observer<Event>> observers = new ArrayList<>();


    public SoferService(SoferRepo repo) {
        this.repo = repo;
    }

    public Optional<Sofer> findSoferById(Long userId) {
        return repo.findOne(userId);
    }

    public Iterable<Sofer> findAll() {
        Iterable<Sofer> allPersons = repo.findAll();
        return allPersons;
    }
    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver(Event t) {
        observers.forEach(o -> o.update(t));
    }

//    public void notifyObserver(Event t, Long clientId) {
//        if (t instanceof ClientChangeEvent) {
//            observers.stream()
//                    .filter(o -> o instanceof Persoana && ((Persoana) o).getId().equals(clientId))
//                    .forEach(o -> o.update(t));
//        } else {
//            observers.forEach(o -> o.update(t));
//        }
//    }
}
