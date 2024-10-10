package com.example.socialnetworkmap.repository;

import com.example.socialnetworkmap.domain.Friendship;
import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.Tuple;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipDbRepo extends AbstractDbRepo<Tuple<Long, Long>, Friendship>{

    public FriendshipDbRepo(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    protected Friendship createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Long id1 = resultSet.getLong("userId1");
        Long id2 = resultSet.getLong("userId2");
        LocalDateTime dateTime = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
        return new Friendship(id1, id2, dateTime);
    }

    @Override
    protected void setEntityParametersForInsertUpdate(PreparedStatement statement, Friendship entity) throws SQLException {
        statement.setTimestamp(1, Timestamp.valueOf(entity.getDate()));
        statement.setLong(2, entity.getId().getLeft());
        statement.setLong(3, entity.getId().getRight());
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> friendshipId) {
        String query = "SELECT * FROM public.\"Friends\" WHERE \"userId1\"=? AND \"userId2\"=?";
        return findOne(query, friendshipId.getLeft(), friendshipId.getRight());
    }
/*
    @Override
    protected Iterable<Friendship> findAll(String query, Pageable pageable, Object... params) {
        List<Friendship> entities = new ArrayList<>();

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

    public Page<Friendship> findAll(Pageable pageable) {
        String query = "SELECT * FROM public.\"Friends\"";
        Iterable<Friendship> friendships = findAll(query, pageable);
        return new PageImplementation<>(pageable, StreamSupport.stream(friendships.spliterator(), false));
    }

 */

    @Override
    public Iterable<Friendship> findAll() {
        String query = "SELECT * FROM public.\"Friends\"";
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

    public Optional<Friendship> delete(Tuple<Long, Long> fId) {
        String deleteQuery = "DELETE FROM public.\"Friends\" WHERE \"userId1\"=? AND \"userId2\"=?";
        try (PreparedStatement statement = getConnection().prepareStatement(deleteQuery)) {
            statement.setLong(1, fId.getLeft());
            statement.setLong(2, fId.getRight());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete friendship", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }

    @Override
    protected void setEntityId(Friendship entity, Object id) {

    }

    public Optional<Friendship> save(Friendship friendship) {
        String saveQuery = "INSERT INTO public.\"Friends\" (\"friendsFrom\",\"userId1\", \"userId2\") VALUES (?, ?, ?)";
        return saveOrUpdate(saveQuery, friendship);
    }
}

/*
 public List<Friendship> findAllFbyMonth(String year, String month) {
        List<Friendship> friendships = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from public.\"Friends\" where extract (year from \"friendsFrom\") = ? and extract(month from \"friendsFrom\")= ?");)
        {
            statement.setInt(1, Integer.parseInt(year));
            statement.setInt(2, Integer.parseInt(month));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                LocalDateTime dateTime=resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                Long id1= resultSet.getLong("userId1");
                Long id2= resultSet.getLong("userId2");
                Friendship friendship=new Friendship(id1,id2, dateTime);
                friendships.add(friendship);
                //findOne(id1).get().addFriend(findOne(id2).get());
                //findOne(id2).get().addFriend(findOne(id1).get());
            }
            //if(!friendships.isEmpty())
            return (List<Friendship>) friendships;
            //else return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
 */