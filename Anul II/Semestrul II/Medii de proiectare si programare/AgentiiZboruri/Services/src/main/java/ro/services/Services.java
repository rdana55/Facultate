package ro.services;

import ro.model.Angajat;
import ro.model.Zbor;

import java.util.List;
import java.util.Optional;

public interface Services {
    Iterable<Angajat> findAll();
    boolean login(String username, String password);
    Optional findOneU(String username);
    long getTotalNumberOfTickets();
    int getTicketsSoldForFlight(Integer idZ);
    Optional saveTicket(Integer idA, Integer idZ, Integer idC);
    List<Zbor> getFlightList();
}
