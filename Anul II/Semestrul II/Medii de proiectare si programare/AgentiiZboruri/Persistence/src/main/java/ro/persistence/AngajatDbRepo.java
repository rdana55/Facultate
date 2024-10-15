package ro.persistence;

import ro.model.Angajat;
import ro.model.Entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AngajatDbRepo implements Repository {

    private final DbUtils dbUtils;

    public AngajatDbRepo(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Optional findOne(Object o) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT a.id, a.username, a.password, p.nume, p.prenume FROM Angajat a JOIN Persoana p ON a.id = p.id WHERE a.id=?")) {
            preStmt.setInt(1, (Integer) o);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    Angajat angajat = new Angajat(id, nume, prenume, username, password);
                    return Optional.of(angajat);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return Optional.empty();
    }

    public Optional findOneU(String username) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT a.idP, a.username, a.password, p.nume, p.prenume FROM Angajat a JOIN Persoana p ON a.idP = p.id WHERE a.username=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("idP");
                    String password = result.getString("password");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    Angajat angajat = new Angajat(id, nume, prenume, username, password);
                    return Optional.of(angajat);
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
        List<Angajat> angajati = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT a.id, a.username, a.password, p.nume, p.prenume FROM Angajat a JOIN Persoana p ON a.id = p.id")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    Angajat angajat = new Angajat(id, nume, prenume, username, password);
                    angajati.add(angajat);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return angajati;
    }

    @Override
    public Optional save(Entity entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Angajat (id, username, password) VALUES (?, ?, ?)")) {
            Angajat angajat = (Angajat) entity;
            preStmt.setInt(1, angajat.getId());
            preStmt.setString(2, angajat.getUsername());
            String encryptedPassword = encryptPassword(angajat.getPassword());
            preStmt.setString(3, encryptedPassword);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    public boolean login(String username, String password) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT password FROM Angajat WHERE username = ?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String storedPassword = result.getString("password");
                    String encryptedPassword = encryptPassword(password);
                    return storedPassword.equals(encryptedPassword);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    public String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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