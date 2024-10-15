package ro.services;

import ro.model.Bilet;

public interface Observer {
    void ticketSold(Bilet bilet) throws Exception;
}
