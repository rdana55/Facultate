package com.example.socialnetworkmap.repository;

import com.example.socialnetworkmap.domain.Entity;
import com.example.socialnetworkmap.domain.FriendshipRequest;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDbRepo<ID, E extends Entity<ID>> {

    private final String url;
    private final String username;
    private final String password;

    public AbstractDbRepo(String url, String username, String password) {
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

    protected abstract E createEntityFromResultSet(ResultSet resultSet) throws SQLException, NoSuchAlgorithmException;

    protected abstract void setEntityParametersForInsertUpdate(PreparedStatement statement, E entity) throws SQLException;

    protected abstract Connection getConnection() throws SQLException;

    protected Optional<E> findOne(String query, Object... parameters) {
        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createEntityFromResultSet(resultSet));
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute findOne query", e);
        }
        return Optional.empty();
    }
/*
    protected Iterable<E> findAll(String query, Pageable pageable, Object... params) {
        List<E> entities = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            // Setarea LIMIT și OFFSET
            statement.setInt(params.length + 1, pageable.getPageSize());
            statement.setInt(params.length + 2, pageable.getPageNumber() * pageable.getPageSize());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(createEntityFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute findAll query", e);
        }

        return entities;
    }

 */

    protected Iterable<E> findAll(String query, Object... params) {
        List<E> entities = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(createEntityFromResultSet(resultSet));
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute findAll query", e);
        }

        return entities;
    }



    protected Optional<E> saveOrUpdate(String query, E entity) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            setEntityParametersForInsertUpdate(statement, entity);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to save or update entity, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setEntityId(entity, generatedKeys.getObject(1));
                    return Optional.of(entity);
                } else {
                    throw new RuntimeException("Failed to retrieve generated ID after insert.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save or update entity", e);
        }
    }

    protected Optional<E> delete(String query, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute delete query", e);
        }

        return Optional.empty();
    }

    public abstract Optional<E> findOne(ID id);

    public abstract Iterable<E> findAll();

    public abstract Optional<FriendshipRequest> findOne(Long senderId, Long receiverId);

    public abstract Iterable<FriendshipRequest> findAll(Long id);

    public abstract Optional<E> save(E entity);

    public abstract Optional<E> delete(ID id);

    public abstract Optional<E> update(E entity);

    protected abstract void setEntityId(E entity, Object id);
}
