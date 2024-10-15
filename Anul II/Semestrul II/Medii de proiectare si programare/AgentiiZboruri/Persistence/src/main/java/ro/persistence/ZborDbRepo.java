package ro.persistence;

import ro.model.Entity;
import ro.model.Zbor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ZborDbRepo implements Repository{
    private final DbUtils dbUtils;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ZborDbRepo(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Optional findOne(Object o) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Zbor WHERE id=?")) {
            preStmt.setInt(1, (Integer) o);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id=result.getInt("id");
                    String from=result.getString("from");
                    String to=result.getString("to");
                    String dataOra=result.getString("dataOra");
                    LocalDateTime dataOraDateTime = LocalDateTime.parse(dataOra, formatter);
                    Integer locuriDisponibile=result.getInt("locuriDisponibile");
                    Zbor zbor = new Zbor(id, from, to, dataOraDateTime, locuriDisponibile);
                    return Optional.of(zbor);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return Optional.empty();
    }

    @Override
    public Iterable findAll() {
        Connection con = dbUtils.getConnection();
        List<Zbor> zboruri = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Zbor")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id=result.getInt("id");
                    String from=result.getString("from");
                    String to=result.getString("to");
                    String dataOra=result.getString("dataOra");
                    LocalDateTime dataOraDateTime = LocalDateTime.parse(dataOra, formatter);
                    Integer locuriDisponibile=result.getInt("locuriDisponibile");
                    Zbor zbor = new Zbor(id, from, to, dataOraDateTime, locuriDisponibile);
                    zboruri.add(zbor);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return zboruri;
    }

    @Override
    public Optional save(Entity entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Zbor (id, from, to, dataOra, locuriDisponibile) VALUES (?, ?, ?, ?, ?)")) {
            Zbor zbor = (Zbor) entity;
            preStmt.setInt(1, zbor.getId());
            preStmt.setString(2, zbor.getFrom());
            preStmt.setString(3, zbor.getTo());
            String dataOraString = zbor.getDataOra().format(formatter);
            preStmt.setString(4, dataOraString);
            preStmt.setInt(5, zbor.getLocuriDisponibile());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public Iterable findAllFiltered(String to, String dataOra) {
        Connection con = dbUtils.getConnection();
        List<Zbor> zboruri = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Zbor WHERE to = ? AND dataOra = ?")) {
            preStmt.setString(1, to);
            preStmt.setString(2, dataOra);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id=result.getInt("id");
                    String from=result.getString("from");
                    String toResult=result.getString("to");
                    String dataOraR=result.getString("dataOra");
                    LocalDateTime dataOraDateTime = LocalDateTime.parse(dataOraR, formatter);
                    Integer locuriDisponibile=result.getInt("locuriDisponibile");
                    Zbor zbor = new Zbor(id, from, toResult, dataOraDateTime, locuriDisponibile);
                    zboruri.add(zbor);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return zboruri;
    }

    @Override
    public Optional delete(Object o) {
        return Optional.empty();
    }

    @Override
    public Optional update(Entity entity) {
        return Optional.empty();
    }

    @Override
    public Iterable changeEntities(Map entities) {
        return null;
    }
}