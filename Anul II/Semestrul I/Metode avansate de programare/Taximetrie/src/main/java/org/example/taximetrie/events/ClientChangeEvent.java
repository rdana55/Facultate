package org.example.taximetrie.events;

public class ClientChangeEvent implements Event{
    private String timpMaxim;
    private String indicativMasina;
    private Long clientId;
    private Long soferId;

    public ClientChangeEvent(Long clientId, String timpMaxim, String indicativMasina, Long soferId) {
        this.clientId = clientId;
        this.timpMaxim = timpMaxim;
        this.indicativMasina = indicativMasina;
        this.soferId=soferId;
    }

    public String getTimpMaxim() {
        return timpMaxim;
    }

    public String getIndicativMasina() {
        return indicativMasina;
    }
    public Long getClientId() {
        return clientId;
    }

    public Long getSoferId() {
        return soferId;
    }
}
