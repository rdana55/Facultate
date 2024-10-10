package org.example.taximetrie.events;

public class TaximetristChangeEvent implements Event{
    private String numeClient;
    private String locatie;

    private Long clientId;
    private Long soferId;

    private String raspuns;

    public TaximetristChangeEvent(Long clientId, String numeClient, String locatie) {
        this.numeClient = numeClient;
        this.locatie = locatie;
    }

    public TaximetristChangeEvent(Long clientId, Long soferId, String raspuns) {
        this.soferId=soferId;
        this.clientId = clientId;
        this.raspuns = raspuns;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public String getLocatie() {
        return locatie;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getRaspuns() {
        return raspuns;
    }

    public Long getSoferId() {
        return soferId;
    }
}
