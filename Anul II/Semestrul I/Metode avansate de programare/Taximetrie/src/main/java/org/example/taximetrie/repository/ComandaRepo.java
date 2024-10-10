package org.example.taximetrie.repository;

import org.example.taximetrie.domain.Comanda;
import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.domain.Sofer;
import org.example.taximetrie.paging.Page;
import org.example.taximetrie.paging.Pageable;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComandaRepo implements Repository<Long,Comanda> {

    private final String url;
    private final String username;
    private final String password;

    public ComandaRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    protected String getUrl() {
        return url;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    @Override
    public Optional<Comanda> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Comanda comanda = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Comanda\" WHERE id='" + id + "'")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                Long idPersoana = resultSet.getLong("idPersoana");
                Long idSofer = resultSet.getLong("idSofer");
                Timestamp timestamp = resultSet.getTimestamp("data");
                LocalDateTime data = timestamp.toLocalDateTime();

                Persoana persoana = null;
                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM \"Persoana\" WHERE id='" + idPersoana + "'");
                ResultSet resultSet1 = statement1.executeQuery();
                if (resultSet1.next()) {
                    Long id2 = resultSet1.getLong("id");
                    String username = resultSet1.getString("username");
                    String nume = resultSet1.getString("nume");

                    persoana = new Persoana(username, nume);
                    persoana.setId(id2);
                }

                Sofer sofer = null;
                PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM \"Sofer\" WHERE id='" + idSofer + "'");
                ResultSet resultSet2 = statement2.executeQuery();
                if (resultSet2.next()) {
                    Long id3 = resultSet2.getLong("id");
                    String username = resultSet2.getString("username");
                    String nume = resultSet2.getString("nume");
                    String indicativMasina = resultSet2.getString("indicativMasina");

                    sofer = new Sofer(username, nume, indicativMasina);
                    sofer.setId(id3);
                }

                comanda = new Comanda(persoana, sofer, data);
                comanda.setId(id1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(comanda);
    }


    @Override
    public Iterable<Comanda> findAll() {
        List<Comanda> comenzi = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Comanda\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idPersoana = resultSet.getLong("idPersoana");
                Long idSofer = resultSet.getLong("idSofer");
                Timestamp timestamp = resultSet.getTimestamp("data");
                LocalDateTime data = timestamp.toLocalDateTime();

                Persoana persoana = null;
                PreparedStatement statementPersoana = connection.prepareStatement("SELECT * FROM \"Persoana\" WHERE id='" + idPersoana + "'");
                ResultSet resultSetPersoana = statementPersoana.executeQuery();
                if (resultSetPersoana.next()) {
                    Long idPers = resultSetPersoana.getLong("id");
                    String username = resultSetPersoana.getString("username");
                    String nume = resultSetPersoana.getString("nume");

                    persoana = new Persoana(username, nume);
                    persoana.setId(idPers);
                }

                Sofer sofer = null;
                PreparedStatement statementSofer = connection.prepareStatement("SELECT * FROM \"Sofer\" WHERE id='" + idSofer + "'");
                ResultSet resultSetSofer = statementSofer.executeQuery();
                if (resultSetSofer.next()) {
                    Long idSof = resultSetSofer.getLong("id");
                    String username = resultSetSofer.getString("username");
                    String nume = resultSetSofer.getString("nume");
                    String indicativMasina = resultSetSofer.getString("indicativMasina");

                    sofer = new Sofer(username, nume, indicativMasina);
                    sofer.setId(idSof);
                }

                Comanda comanda = new Comanda(persoana, sofer, data);
                comanda.setId(id);

                comenzi.add(comanda);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comenzi;
    }

    @Override
    public Optional<Comanda> save(Comanda entity) {
        return Optional.empty();
    }

    //@Override
    public Optional<Comanda> save(Long idP, Long idS, LocalDateTime data) {
        if (idP == null || idS == null || data == null)
            throw new IllegalArgumentException("Entity cannot be null");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Comanda\" (idPersoana, idSofer, data) VALUES (?, ?, ?)")) {

            statement.setLong(1, idP);
            statement.setLong(2, idS);
            statement.setTimestamp(3, Timestamp.valueOf(data));

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                //return Optional.of(entity);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Comanda> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Comanda> update(Comanda entity) {
        return Optional.empty();
    }

    public List<Persoana> findClientiOnorati(Long idS){
        List<Persoana> clientiOnorati = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT idPersoana FROM \"Comanda\" WHERE idSofer='" + idS + "'")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idPersoana = resultSet.getLong("idPersoana");
                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM \"Persoana\" WHERE id='" + idPersoana + "'");
                ResultSet resultSet1 = statement1.executeQuery();
                if (resultSet1.next()) {
                    Long id2 = resultSet1.getLong("id");
                    String username = resultSet1.getString("username");
                    String nume = resultSet1.getString("nume");

                    Persoana persoana = new Persoana(username, nume);
                    persoana.setId(id2);
                    clientiOnorati.add(persoana);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientiOnorati;
    }
}
