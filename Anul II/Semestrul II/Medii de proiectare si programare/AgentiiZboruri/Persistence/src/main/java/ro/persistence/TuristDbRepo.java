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

public class TuristDbRepo implements Repository{

    private final DbUtils dbUtils;

    public TuristDbRepo(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public List<Persoana> findAllByNr(int nr) {
        Connection con = dbUtils.getConnection();
        List<Persoana> turisti = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT t.*, p.nume, p.prenume FROM Turist t JOIN Persoana p ON t.idP = p.id WHERE t.nrClient = ?")) {
            preStmt.setInt(1, nr);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer idP = result.getInt("idP");
                    String nume = result.getString("nume");
                    String prenume = result.getString("prenume");
                    Persoana turist = new Persoana(idP, nume, prenume);
                    turisti.add(turist);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return turisti;
    }

    @Override
    public Optional findOne(Object o) {
        return Optional.empty();
    }

    @Override
    public Iterable findAll() {
        return null;
    }

    @Override
    public Optional save(Entity entity) {
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