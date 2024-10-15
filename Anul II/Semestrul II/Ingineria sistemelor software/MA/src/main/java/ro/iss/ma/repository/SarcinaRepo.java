package ro.iss.ma.repository;

import ro.iss.ma.domain.Angajat;
import ro.iss.ma.domain.Sarcina;
import ro.iss.ma.domain.Stare;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SarcinaRepo implements Repository<Integer, Sarcina> {
    private String url;
    private String username;
    private String password;

    public SarcinaRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Optional<Sarcina> findOne(Integer id) {
        String SQL = "SELECT * FROM \"Sarcina\" WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                AngajatRepo angajatRepo = new AngajatRepo(url, username, password);
                Angajat angajat = angajatRepo.findOne(rs.getInt("idA")).orElse(null);
                Sarcina sarcina = new Sarcina(
                        angajat,
                        rs.getString("descriere"),
                        Stare.valueOf(rs.getString("stare"))
                );
                return Optional.of(sarcina);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Sarcina> findAll() {
        List<Sarcina> sarcini = new ArrayList<>();
        String SQL = "SELECT * FROM \"Sarcina\"";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AngajatRepo angajatRepo = new AngajatRepo(url, username, password);
                Angajat angajat = angajatRepo.findOne(rs.getInt("idA")).orElse(null);
                Sarcina sarcina = new Sarcina(
                        rs.getInt("id"),
                        angajat,
                        rs.getString("descriere"),
                        Stare.valueOf(rs.getString("stare"))
                );
                sarcini.add(sarcina);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return sarcini;
    }

    @Override
    public Optional<Sarcina> save(Sarcina sarcina) {
        String SQL = "INSERT INTO \"Sarcina\"(id, idA, descriere, stare) "
                + "VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, sarcina.getId());
            pstmt.setInt(2, sarcina.getAngajat().getId());
            pstmt.setString(3, sarcina.getDescriere());
            pstmt.setString(4, sarcina.getStare().name());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Optional.ofNullable(sarcina);
    }

    @Override
    public Optional<Sarcina> delete(Integer id) {
        String SQL = "DELETE FROM \"Sarcina\" WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                return Optional.empty();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Sarcina> update(Sarcina sarcina) {
        String SQL = "UPDATE \"Sarcina\" SET idA = ?, descriere = ?, stare = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, sarcina.getAngajat().getId());
            pstmt.setString(2, sarcina.getDescriere());
            pstmt.setString(3, sarcina.getStare().name());
            pstmt.setInt(4, sarcina.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                return Optional.of(sarcina);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return Optional.empty();
    }

    public List<Sarcina> getSarciniAngajat(Angajat angajat) {
        List<Sarcina> sarcini = new ArrayList<>();
        String SQL = "SELECT * FROM \"Sarcina\" WHERE idA = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, angajat.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Sarcina sarcina = new Sarcina(
                        angajat,
                        rs.getString("descriere"),
                        Stare.valueOf(rs.getString("stare"))
                );
                sarcina.setId(rs.getInt("id"));
                sarcini.add(sarcina);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return sarcini;
    }

}
