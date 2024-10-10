package org.example.taximetrie.service;

import org.example.taximetrie.domain.Comanda;
import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.domain.Sofer;
import org.example.taximetrie.repository.ComandaRepo;
import org.example.taximetrie.repository.SoferRepo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComandaService {
    private ComandaRepo repo;

    public ComandaService(ComandaRepo repo) {
        this.repo = repo;
    }

    public Optional<Comanda> findComandaById(Long userId) {
        return repo.findOne(userId);
    }

    public Iterable<Comanda> findAll() {
        Iterable<Comanda> allPersons = repo.findAll();
        return allPersons;
    }

    public void saveComanda(Long idP, Long idS, LocalDateTime data){
        repo.save(idP, idS, data);
    }

    public List<Persoana> findClientiOnorati(Long idS){
        return repo.findClientiOnorati(idS);
    }

    public List<Comanda> findComenziByDate(LocalDateTime date){
        List<Comanda> allComenzi= (List<Comanda>) repo.findAll();
        List<Comanda> comenziByDate = new ArrayList<>();
        for (Comanda comanda : allComenzi){
            if (comanda.getData().getYear()==date.getYear() && comanda.getData().getMonth()==date.getMonth() && comanda.getData().getDayOfMonth()==date.getDayOfMonth()){
                comenziByDate.add(comanda);
            }
        }
        return comenziByDate;
    }

    public int getNrComenziInUltimile3Luni() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMonthsAgo = now.minus(3, ChronoUnit.MONTHS);

        List<Comanda> comenzi = findComenziByDateRange(threeMonthsAgo, now);

        return comenzi.size();
    }

    public List<Comanda> findComenziByDateRange(LocalDateTime start, LocalDateTime end){
        List<Comanda> allComenzi= (List<Comanda>) repo.findAll();
        List<Comanda> comenziByDateRange = new ArrayList<>();
        for (Comanda comanda : allComenzi){
            if (comanda.getData().isAfter(start) && comanda.getData().isBefore(end)){
                comenziByDateRange.add(comanda);
            }
        }
        return comenziByDateRange;
    }
}
