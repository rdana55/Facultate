package ro.persistence;

import ro.model.Client;
import ro.model.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClientDbRepo implements Repository{
    private final DbUtils dbUtils;
    private final PersoanaDbRepo persoanaDbRepo;

    public ClientDbRepo(DbUtils dbUtils, PersoanaDbRepo persoanaDbRepo) {
        this.dbUtils = dbUtils;
        this.persoanaDbRepo = persoanaDbRepo;
    }

    @Override
    public Optional findOne(Object o) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Client c JOIN Persoana p ON c.id = p.id WHERE c.id=?")) {
            preStmt.setInt(1, (Integer) o);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id=result.getInt("idP");
                    String adresa=result.getString("adresa");
                    String nume=result.getString("nume");
                    String prenume=result.getString("prenume");
                    Client client = new Client(id, nume, prenume, adresa);
                    return Optional.of(client);
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
        List<Client> clients = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Client c JOIN Persoana p ON c.id = p.id")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id=result.getInt("idP");
                    String adresa=result.getString("adresa");
                    String nume=result.getString("nume");
                    String prenume=result.getString("prenume");
                    Client client = new Client(id, nume, prenume, adresa);
                    clients.add(client);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return clients;
    }

    @Override
    public Optional save(Entity entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Client (id, adresa) VALUES (?, ?)")) {
            Client client = (Client) entity;
            preStmt.setInt(1, client.getId());
            preStmt.setString(2, client.getAdresa());
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
        return null;
    }

    @Override
    public Iterable changeEntities(Map entities) {
        return null;
    }
}