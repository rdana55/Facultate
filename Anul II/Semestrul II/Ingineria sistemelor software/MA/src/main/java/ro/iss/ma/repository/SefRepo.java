package ro.iss.ma.repository;

import ro.iss.ma.domain.Sef;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SefRepo implements Repository<Integer, Sef>{

    private final String url;
    private final String username;
    private final String password;

    public SefRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Sef> findOne(Integer aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Sef> findAll() {
        return null;
    }

    @Override
    public Optional<Sef> save(Sef entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Sef> delete(Integer aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Sef> update(Sef entity) {
        return Optional.empty();
    }

    public boolean login(String username, String password) {
        try (Connection con = getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT \"password\" FROM \"Persoana\" WHERE \"username\" = ?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String storedPassword = result.getString("password");
                    //String encryptedPassword = encryptPassword(password);
                    return storedPassword.equals(password);
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

    public Optional<Sef> findOne(String username) {
        try (Connection con = getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM \"Persoana\" p JOIN \"Sef\" a ON p.id = a.id WHERE p.username = ?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("id");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    String user = result.getString("username");
                    String pass = result.getString("password");
                    return Optional.of(new Sef(id, nume, prenume, user, pass));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return Optional.empty();
    }
}