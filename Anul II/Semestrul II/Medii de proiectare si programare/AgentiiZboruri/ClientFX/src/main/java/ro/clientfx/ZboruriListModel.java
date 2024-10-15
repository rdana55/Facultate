package ro.clientfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ro.networking.ServicesImpl;
import ro.model.Zbor;
import ro.services.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ZboruriListModel {
    private ObservableList<Zbor> flightList;
    private ObservableList<String> uniqueFromList;
    private ObservableList<String> uniqueToList;
    private Services biletService; // add this line

    public ZboruriListModel(Iterable<Zbor> zborIterable, Services biletService) {

        this.biletService = biletService; // add this line

        List<Zbor> flightList = StreamSupport.stream(zborIterable.spliterator(), false)
                .filter(n -> n.getLocuriDisponibile()- biletService.getTicketsSoldForFlight(n.getId()) > 0)
                .map(n -> new Zbor(n.getId(), n.getFrom(), n.getTo(), n.getDataOra(), n.getLocuriDisponibile()))
                .collect(Collectors.toList());

        this.flightList = FXCollections.observableArrayList(flightList);

        List<String> fromList = flightList.stream()
                .map(Zbor::getFrom)
                .collect(Collectors.toList());

        List<String> toList = flightList.stream()
                .map(Zbor::getTo)
                .collect(Collectors.toList());

        this.uniqueFromList = FXCollections.observableArrayList(new ArrayList<>(new HashSet<>(fromList)));
        this.uniqueToList = FXCollections.observableArrayList(new ArrayList<>(new HashSet<>(toList)));
    }

    public ObservableList<Zbor> getFlightList() {
        return flightList;
    }

    public ObservableList<String> getUniqueFromList() {
        return uniqueFromList;
    }

    public ObservableList<String> getUniqueToList() {
        return uniqueToList;
    }

    public ObservableList<Zbor> getFilteredFlightList(String plecare, String sosire, LocalDateTime data) {
        List<Zbor> filteredFlightList = flightList.stream()
                .filter(n -> n.getFrom().equals(plecare))
                .filter(n -> n.getTo().equals(sosire))
                .filter(n -> n.getDataOra().getYear() == data.getYear() && n.getDataOra().getMonth() == data.getMonth() && n.getDataOra().getDayOfMonth() == data.getDayOfMonth())
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(filteredFlightList);
    }
}