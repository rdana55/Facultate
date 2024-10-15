package ro.persistence;

import ro.model.Entity;
import ro.model.Persoana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersoanaDbRepo implements Repository{
    private final DbUtils dbUtils;

    public PersoanaDbRepo(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Optional findOne(Object o) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Persoana WHERE id=?")) {
            preStmt.setInt(1, (Integer) o);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id=result.getInt("id");
                    String nume=result.getString("nume");
                    String prenume=result.getString("prenume");
                    Persoana persoana = new Persoana(id,nume,prenume);
                    return Optional.of(persoana);
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
        List<Persoana> persoane = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Persoana")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id=result.getInt("id");
                    String nume=result.getString("nume");
                    String prenume=result.getString("prenume");
                    Persoana persoana = new Persoana(id,nume,prenume);
                    persoane.add(persoana);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return persoane;
    }

    @Override
    public Optional save(Entity entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Persoana (id, nume, prenume) VALUES (?, ?, ?)")) {
            Persoana persoana = (Persoana) entity;
            preStmt.setInt(1, persoana.getId());
            preStmt.setString(2, persoana.getNume());
            preStmt.setString(3, persoana.getPrenume());
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.of(entity);
        }
        return Optional.empty();
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