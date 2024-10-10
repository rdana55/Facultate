package org.example.taximetrie.repository;

import org.example.taximetrie.domain.Persoana;
import org.example.taximetrie.paging.Page;
import org.example.taximetrie.paging.Pageable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersoanaRepo implements PagingRepository<Long, Persoana>{

    private final String url;
    private final String username;
    private final String password;

    public PersoanaRepo(String url, String username, String password) {
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
    public Optional<Persoana> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Persoana persoana = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Persoana\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String nume = resultSet.getString("nume");

                persoana = new Persoana(username, nume);
                persoana.setId(id1);

                return Optional.of(persoana);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(persoana);
    }

    @Override
    public Iterable<Persoana> findAll() {
        List<Persoana> persoane = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Persoana\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String nume = resultSet.getString("nume");
                Persoana persoana = new Persoana(username, nume);
                persoana.setId(id);

                persoane.add(persoana);
            }
            return persoane;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persoane;
    }

    @Override
    public Optional<Persoana> save(Persoana entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> update(Persoana entity) {
        return Optional.empty();
    }

    @Override
    public Page<Persoana> findAllOnPage(Pageable pageable) {
        List<Persoana> persoanas = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pagestatement = connection.prepareStatement("SELECT * FROM public.\"Persoana\" LIMIT ? OFFSET ?");
             PreparedStatement countstatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Persoana\"")
        ) {
            pagestatement.setInt(1, pageable.getPageSize());
            pagestatement.setInt(2, pageable.getPageSize() * pageable.getPageNr());

            try (
                    ResultSet pageResultSet = pagestatement.executeQuery();
                    ResultSet countResultSet = countstatement.executeQuery();
            ) {
                int count = 0;
                if (countResultSet.next()) {
                    count = countResultSet.getInt("count");
                }

                while (pageResultSet.next()) {
                    Long idp = pageResultSet.getLong("id");
                    String username1 = pageResultSet.getString("username");
                    String nume = pageResultSet.getString("nume");

                    Persoana persoana = new Persoana(username1, nume);
                    persoana.setId(idp);
                    persoanas.add(persoana);
                }
                return new Page<>(persoanas, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
