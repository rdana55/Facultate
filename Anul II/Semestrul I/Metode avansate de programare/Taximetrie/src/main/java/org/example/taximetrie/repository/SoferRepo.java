package org.example.taximetrie.repository;

import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.domain.Sofer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoferRepo implements Repository<Long, Sofer> {

    private final String url;
    private final String username;
    private final String password;

    public SoferRepo(String url, String username, String password) {
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
    public Optional<Sofer> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Sofer sofer = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Sofer\" WHERE id='" + id + "'")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String nume = resultSet.getString("nume");
                String indicativMasina = resultSet.getString("indicativMasina");

                sofer = new Sofer(username, nume, indicativMasina);
                sofer.setId(id1);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }

        return Optional.ofNullable(sofer);
    }

        @Override
        public Iterable<Sofer> findAll () {
            List<Sofer> soferi = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Sofer\"");
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long id1 = resultSet.getLong("id");
                    String username = resultSet.getString("username");
                    String nume = resultSet.getString("nume");
                    String indicativMasina = resultSet.getString("indicativMasina");

                    Sofer sofer = new Sofer(username, nume, indicativMasina);
                    sofer.setId(id1);

                    soferi.add(sofer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return soferi;
        }

        @Override
        public Optional<Sofer> save (Sofer entity){
            return Optional.empty();
        }

        @Override
        public Optional<Sofer> delete (Long aLong){
            return Optional.empty();
        }

        @Override
        public Optional<Sofer> update (Sofer entity){
            return Optional.empty();
        }
}
