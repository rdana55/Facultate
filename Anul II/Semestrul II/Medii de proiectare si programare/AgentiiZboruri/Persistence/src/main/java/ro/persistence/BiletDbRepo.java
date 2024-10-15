package ro.persistence;

import ro.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Random;

public class BiletDbRepo implements Repository {
    private final DbUtils dbUtils;
    private final PersoanaDbRepo persoanaDbRepo;
    private final ZborDbRepo zborDbRepo;
    private final AngajatDbRepo angajatDbRepo;

    public BiletDbRepo(DbUtils dbUtils, PersoanaDbRepo persoanaDbRepo, ZborDbRepo zborDbRepo, AngajatDbRepo angajatDbRepo) {
        this.dbUtils = dbUtils;
        this.persoanaDbRepo = persoanaDbRepo;
        this.zborDbRepo = zborDbRepo;
        this.angajatDbRepo = angajatDbRepo;
    }

    @Override
    public Optional findOne(Object o) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Bilet WHERE id=?")) {
            preStmt.setInt(1, (Integer) o);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Integer id = result.getInt("id");
                    Integer idAngajat = result.getInt("idAngajat");
                    Integer idZbor = result.getInt("idZbor");
                    Integer idPersoana = result.getInt("idPersoana");
                    Angajat angajat = (Angajat) angajatDbRepo.findOne(idAngajat).get();
                    Zbor zbor = (Zbor) zborDbRepo.findOne(idZbor).get();
                    Persoana persoana = (Persoana) persoanaDbRepo.findOne(idPersoana).get();
                    Bilet bilet = new Bilet(id, angajat, zbor, persoana);
                    return Optional.of(bilet);
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
        List<Bilet> bilete = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT * FROM Bilet")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    Integer idAngajat = result.getInt("idAngajat");
                    Integer idZbor = result.getInt("idZbor");
                    Integer idPersoana = result.getInt("idPersoana");
                    Angajat angajat = (Angajat) angajatDbRepo.findOne(idAngajat).get();
                    Zbor zbor = (Zbor) zborDbRepo.findOne(idZbor).get();
                    Persoana persoana = (Persoana) persoanaDbRepo.findOne(idPersoana).get();
                    Bilet bilet = new Bilet(id, angajat, zbor, persoana);
                    bilete.add(bilet);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return bilete;
    }

    @Override
    public Optional save(Entity entity) {
        return Optional.empty();
    }

    public Optional saveA(Integer idA, Integer idZ, Integer idC){
        Connection con = dbUtils.getConnection();
        Random random = new Random();
        try {
            // First, get the idP values from the Turist table where nrClient equals idC
            PreparedStatement preStmtTurist = con.prepareStatement("SELECT idP FROM Turist WHERE nrClient = ?");
            preStmtTurist.setInt(1, idC);
            ResultSet resultSet = preStmtTurist.executeQuery();

            // For each idP found, insert a new record into the Bilet table
            while (resultSet.next()) {
                Integer idP = resultSet.getInt("idP");
                if (idP != null) {
                    PreparedStatement preStmtBilet = con.prepareStatement("INSERT INTO Bilet (id, idAngajat, idZbor, idPersoana) VALUES (?, ?, ?, ?)");
                    preStmtBilet.setInt(1, random.nextInt(Integer.MAX_VALUE));
                    preStmtBilet.setInt(2, idA);
                    preStmtBilet.setInt(3, idZ);
                    preStmtBilet.setInt(4, idP);
                    preStmtBilet.executeUpdate();
                }
            }

            // Insert a new record into the Bilet table for the person with idC
            if (idC != null) {
                PreparedStatement preStmtBilet = con.prepareStatement("INSERT INTO Bilet (id, idAngajat, idZbor, idPersoana) VALUES (?, ?, ?, ?)");
                preStmtBilet.setInt(1, random.nextInt(Integer.MAX_VALUE));
                preStmtBilet.setInt(2, idA);
                preStmtBilet.setInt(3, idZ);
                preStmtBilet.setInt(4, idC);
                preStmtBilet.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.of(idC);
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

    public long count() {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) AS [count] FROM Bilet")) {
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    return result.getLong("count");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return 0;
    }

    public int getTicketsSoldForFlight(Integer idZ) {
        int count = 0;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) FROM Bilet WHERE idZbor = ?")) {
            preStmt.setInt(1, idZ);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return count;
    }
}