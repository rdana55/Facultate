package ro.persistence;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ro.model.Entity;
import ro.model.Zbor;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@org.springframework.stereotype.Repository
public class ZborRepoREST implements ZborRepoRestI{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DbUtils dbUtils;

    @Autowired
    public ZborRepoREST(@Qualifier("props") Properties props) {
        dbUtils = new DbUtils(props);
    }

    @Override
    public Optional<Zbor> findOne(Integer o) {
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
    public Optional<Zbor> save(Zbor zbor) {
        Connection con = dbUtils.getConnection();
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preStmt = con.prepareStatement("INSERT INTO Zbor (id, \"from\", \"to\", dataOra, locuriDisponibile) VALUES (?, ?, ?, ?, ?)")) {

            preStmt.setInt(1, zbor.getId());
            preStmt.setString(2, zbor.getFrom());
            preStmt.setString(3, zbor.getTo());
            String dataOraString = zbor.getDataOra().format(formatter);
            preStmt.setString(4, dataOraString);
            preStmt.setInt(5, zbor.getLocuriDisponibile());
            preStmt.executeUpdate();
            con.commit();  // Confirm the transaction
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.of(zbor);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Zbor> delete(Integer o) {
        Connection con = dbUtils.getConnection();
        Zbor zbor = findOne(o).orElse(null);
        if (zbor == null) {
            System.out.println("Zbor with id " + o + " does not exist.");
            return Optional.empty();
        }
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Zbor WHERE id=?")) {
            preStmt.setInt(1, o);
            int affectedRows = preStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting flight failed, no rows affected.");
            }
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            return Optional.empty();
        }
        return Optional.of(zbor);
    }

    @Override
    public Optional<Zbor> update(Zbor zbor) {
        Connection con = dbUtils.getConnection();
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Zbor SET \"from\"=?, \"to\"=?, dataOra=?, locuriDisponibile=? WHERE id=?")) {
            preStmt.setString(1, zbor.getFrom());
            preStmt.setString(2, zbor.getTo());
            String dataOraString = zbor.getDataOra().format(formatter);
            preStmt.setString(3, dataOraString);
            preStmt.setInt(4, zbor.getLocuriDisponibile());
            preStmt.setInt(5, zbor.getId());
            int affectedRows = preStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating flight failed, no rows affected.");
            }
            con.commit();  // Confirm the transaction
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
            try {
                con.rollback();  // Rollback the transaction in case of an error
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }
        return Optional.of(zbor);
    }

    @Override
    public Iterable changeEntities(Map entities) {
        return null;
    }
}
