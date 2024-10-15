package ro.iss.ma.repository;

import ro.iss.ma.domain.Angajat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.OffsetDateTime;
public class AngajatRepo implements Repository<Integer, Angajat>{

    private final String url;
    private final String username;
    private final String password;

    public AngajatRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Angajat> findOne(Integer id) {
        try (Connection con = getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM \"Persoana\" p JOIN \"Angajat\" a ON p.id = a.id WHERE p.id = ?")) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    String user = result.getString("username");
                    String pass = result.getString("password");

                    OffsetDateTime intrareOffset = result.getObject("intrare", OffsetDateTime.class);
                    OffsetDateTime iesireOffset = result.getObject("iesire", OffsetDateTime.class);

                    LocalDateTime intrare = intrareOffset.toLocalDateTime();
                    LocalDateTime iesire = iesireOffset.toLocalDateTime();

                    return Optional.of(new Angajat(id, nume, prenume, user, pass, intrare, iesire));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Angajat> findAll() {
        List<Angajat> angajati = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM \"Persoana\" p JOIN \"Angajat\" a ON p.id = a.id")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    String user = result.getString("username");
                    String pass = result.getString("password");

                    Timestamp intrareOffset = result.getTimestamp("intrare");
                    Timestamp iesireOffset = result.getTimestamp("iesire");

                    LocalDateTime intrare = intrareOffset.toLocalDateTime();
                    LocalDateTime iesire = iesireOffset.toLocalDateTime();

                    angajati.add(new Angajat(id, nume, prenume, user, pass, intrare, iesire));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return angajati;
    }

    @Override
    public Optional<Angajat> save(Angajat entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Angajat> delete(Integer aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Angajat> update(Angajat entity) {
        String sql = "UPDATE \"Angajat\" SET intrare = ?, iesire = ? WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement preStmt = con.prepareStatement(sql)) {

            preStmt.setTimestamp(1, Timestamp.valueOf(entity.getIntrare()));
            preStmt.setTimestamp(2, Timestamp.valueOf(entity.getIesire()));
            preStmt.setInt(3, entity.getId());

            int affectedRows = preStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating angajat failed, no rows affected.");
            }

            return Optional.of(entity);
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.empty();
        }
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

    public Optional<Angajat> findOne(String username) {
        try (Connection con = getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT * FROM \"Persoana\" p JOIN \"Angajat\" a ON p.id = a.id WHERE p.username = ?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("id");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    String user = result.getString("username");
                    String pass = result.getString("password");
                    //LocalDateTime intrare = result.getObject("intrare", LocalDateTime.class);
                    //LocalDateTime iesire = result.getObject("iesire", LocalDateTime.class);

                    OffsetDateTime intrareOffset = result.getObject("intrare", OffsetDateTime.class);
                    OffsetDateTime iesireOffset = result.getObject("iesire", OffsetDateTime.class);

                    LocalDateTime intrare = intrareOffset.toLocalDateTime();
                    LocalDateTime iesire = iesireOffset.toLocalDateTime();

                    return Optional.of(new Angajat(id, nume, prenume, user, pass, intrare, iesire));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return Optional.empty();
    }
}