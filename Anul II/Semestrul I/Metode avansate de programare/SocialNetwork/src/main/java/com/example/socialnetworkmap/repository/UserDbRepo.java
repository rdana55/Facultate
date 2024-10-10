package com.example.socialnetworkmap.repository;

import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.paging.PagingRepository;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDbRepo extends AbstractDbRepo<Long,User> implements PagingRepository<Long,User> {

    public UserDbRepo(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    protected User createEntityFromResultSet(ResultSet resultSet) throws SQLException, NoSuchAlgorithmException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String password=resultSet.getString("password");
        User user = new User(firstName, lastName, password);
        user.setId(id);
        return user;
    }

    @Override
    protected void setEntityParametersForInsertUpdate(PreparedStatement statement, User entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        statement.setString(4,entity.getPassword());
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    @Override
    public Optional<User> findOne(Long id) {
        String query = "SELECT * FROM public.\"Users\" WHERE id = ?";
        return findOne(query, id);
    }

    /*
    @Override
    protected Iterable<User> findAll(String query, Pageable pageable, Object... params) {
        List<User> entities = new ArrayList<>();

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

    public Page<User> findAll(Pageable pageable) {
        String query = "SELECT * FROM public.\"Users\"";
        Iterable<User> users = findAll(query, pageable);
        return new PageImplementation<>(pageable, StreamSupport.stream(users.spliterator(), false));
    }

     */

    @Override
    public Iterable<User> findAll() {
        String query = "SELECT * FROM public.\"Users\"";
        return findAll(query);
    }

    @Override
    public Optional<FriendshipRequest> findOne(Long senderId, Long receiverId) {
        return Optional.empty();
    }

    @Override
    public Iterable<FriendshipRequest> findAll(Long id) {
        return null;
    }

    /*
    @Override
    public Iterable<FriendshipRequest> findAll(Long id) {
        return null;
    }

     */

    @Override
    public Optional<User> save(User entity) {
        String query = "INSERT INTO public.\"Users\" (id, first_name, last_name, password) VALUES (?, ?, ?, ?)";
        return saveOrUpdate(query, entity);
    }

    @Override
    public Optional<User> delete(Long id) {
        Optional<User> user;
        String deleteFriendshipQuery = "DELETE FROM public.\"Friends\" WHERE \"userId1\"=? OR \"userId2\"=?";
        String deleteUserQuery = "DELETE FROM public.\"Users\" WHERE \"id\"=?";

        try (Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword())) {
            try (PreparedStatement statement = connection.prepareStatement(deleteFriendshipQuery)) {
                statement.setLong(1, id);
                statement.setLong(2, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete friendship", e);
            }

            user = findOne(id);

            try (PreparedStatement statement = connection.prepareStatement(deleteUserQuery)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete user", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
    @Override
    public Optional<User> update(User entity) {
        Optional<User> existingUser = findOne(entity.getId());

        if (existingUser.isPresent()) {
            String query = "UPDATE public.\"Users\" SET \"first_name\"=?, \"last_name\"=? WHERE  \"id\"=?";
            return saveOrUpdate(query, entity);
        } else {
            return Optional.empty(); // Utilizatorul nu există, deci nu putem face actualizare
        }
    }

    @Override
    protected void setEntityId(User entity, Object id) {

    }

    @Override
    public Page<User> findAllOnPage(Pageable pageable){
        List<User> users=new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(getUrl(),getUsername(),getPassword());
        PreparedStatement pageStatement=connection.prepareStatement("SELECT * FROM public.\"Users\" LIMIT ? OFFSET ?");
        PreparedStatement countStatement= connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Users\"")
        ){
            pageStatement.setInt(1,pageable.getPageSize());
            pageStatement.setInt(2,pageable.getPageSize()*pageable.getPageNr());

            try(
                    ResultSet pageResultSet = pageStatement.executeQuery();
                    ResultSet countResultSet = countStatement.executeQuery();
                    ){
                int count = 0;
                if (countResultSet.next()){
                    count=countResultSet.getInt("count");
                }
                while (pageResultSet.next()){
                    users.add(createEntityFromResultSet(pageResultSet));
                }
                return new Page<>(users,count);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
