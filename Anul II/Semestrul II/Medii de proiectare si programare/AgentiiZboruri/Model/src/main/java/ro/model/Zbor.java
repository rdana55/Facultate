package ro.model;

import java.time.LocalDateTime;

public class Zbor extends Entity<Integer>{
    private String from;
    private String to;
    private LocalDateTime dataOra;
    private Integer locuriDisponibile;

    public Zbor(Integer id, String from, String to, LocalDateTime dataOra, Integer locuriDisponibile) {
        super(id);
        this.from = from;
        this.to = to;
        this.dataOra = dataOra;
        this.locuriDisponibile = locuriDisponibile;
    }

    public Zbor(){
        super();};

    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public Integer getLocuriDisponibile() {
        return locuriDisponibile;
    }

    @Override
    public String toString() {
        return "Zbor{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", dataOra=" + dataOra +
                ", locuriDisponibile=" + locuriDisponibile +
                '}';
    }

    public void setLocuriDisponibile(Integer locuriDisponibile) {
        this.locuriDisponibile = locuriDisponibile;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Zbor zbor = (Zbor) o;

        if (from != null ? !from.equals(zbor.from) : zbor.from != null) return false;
        if (to != null ? !to.equals(zbor.to) : zbor.to != null) return false;
        if (dataOra != null ? !dataOra.equals(zbor.dataOra) : zbor.dataOra != null) return false;
        return locuriDisponibile != null ? locuriDisponibile.equals(zbor.locuriDisponibile) : zbor.locuriDisponibile == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (dataOra != null ? dataOra.hashCode() : 0);
        result = 31 * result + (locuriDisponibile != null ? locuriDisponibile.hashCode() : 0);
        return result;
    }

}
