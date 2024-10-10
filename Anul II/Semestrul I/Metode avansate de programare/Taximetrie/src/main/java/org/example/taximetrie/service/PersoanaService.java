package org.example.taximetrie.service;

import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.events.Event;
import org.example.taximetrie.observer.Observable;
import org.example.taximetrie.observer.Observer;
import org.example.taximetrie.paging.Page;
import org.example.taximetrie.paging.Pageable;
import org.example.taximetrie.repository.PersoanaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersoanaService implements Observable<Event> {
    private PersoanaRepo repo;
    private List<Observer<Event>> observers = new ArrayList<>();


    public PersoanaService(PersoanaRepo repo) {
        this.repo = repo;
    }

    public Optional<Persoana> findPersonById(Long userId) {
        return repo.findOne(userId);
    }

    public Iterable<Persoana> findAll() {
        Iterable<Persoana> allPersons = repo.findAll();
        return allPersons;
    }

    public Page<Persoana> findAllPersoanaPage(Pageable pageable) {
        return repo.findAllOnPage(pageable);
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
}
