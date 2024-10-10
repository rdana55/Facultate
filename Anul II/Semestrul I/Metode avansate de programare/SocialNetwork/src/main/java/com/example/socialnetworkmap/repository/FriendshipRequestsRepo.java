package com.example.socialnetworkmap.repository;

import com.example.socialnetworkmap.controller.MessageAlert;
import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.FriendshipStatus;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.paging.PagingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FriendshipRequestsRepo extends AbstractDbRepo<Long, FriendshipRequest> implements PagingRepository<Long, FriendshipRequest> {

    private PreparedStatement statement;
    private FriendshipRequest entity;

    private UserDbRepo userDbRepo;

    public FriendshipRequestsRepo(String url, String username, String password, UserDbRepo userDbRepo) {
        super(url, username, password);
        this.userDbRepo = userDbRepo;
    }

    @Override
    protected void setEntityParametersForInsertUpdate(PreparedStatement statement, FriendshipRequest entity) throws SQLException {
        statement.setString(1, String.valueOf(entity.getStatus()));
        statement.setLong(2, entity.getSender().getId());
        statement.setLong(3, entity.getReceiver().getId());
    }

    @Override
    protected FriendshipRequest createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Long senderId = resultSet.getLong("sender");
        Long receiverId = resultSet.getLong("receiver");
        FriendshipStatus status = FriendshipStatus.valueOf(resultSet.getString("status"));

        try {
            // Codul care poate arunca o exceptie
            Optional<User> sender = userDbRepo.findOne(senderId);
            Optional<User> receiver = userDbRepo.findOne(receiverId);

            if (sender.isPresent() && receiver.isPresent()) {
                return new FriendshipRequest(sender.get(), receiver.get(), status);
            } else {
                throw new NoSuchElementException("Sender or receiver not found");
            }
        } catch (Exception e) {
            // Afișează o notificare
            MessageAlert.showErrorMessage(null,"Sender sau receiver nu exista!");
            // În loc să oprești programul, returnează, sau poți să faci și altceva în funcție de nevoi
            return null;
        }
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    @Override
    public Optional<FriendshipRequest> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        return null;
    }


//    @Override
//    protected FriendshipRequest createEntityFromResultSet(ResultSet resultSet) throws SQLException {
//        return null;
//    }

    @Override
    public Optional<FriendshipRequest> findOne(Long senderId, Long receiverId) {
        String query = "SELECT * FROM public.\"Requests\" WHERE \"sender\"=? AND \"receiver\"=?";
        return findOne(query, senderId, receiverId);
    }
/*
    @Override
    public Iterable<FriendshipRequest> findAll(Long id) {
        return null;
    }


 */
    /*
    @Override
    protected Iterable<FriendshipRequest> findAll(String query, Pageable pageable, Object... params) {
        List<FriendshipRequest> entities = new ArrayList<>();

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

    public Page<FriendshipRequest> findAll(Pageable pageable) {
        String query = "SELECT * FROM public.\"Requests\"";
        Iterable<FriendshipRequest> friendshipRequests = findAll(query, pageable);
        return new PageImplementation<>(pageable, StreamSupport.stream(friendshipRequests.spliterator(), false));
    }


     */

    @Override
    public Iterable<FriendshipRequest> findAll(Long id) {
        String query = "SELECT * FROM public.\"Requests\" WHERE \"receiver\"=?";

        return findAll(query, id);
    }

    @Override
    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        String saveQuery = "INSERT INTO public.\"Requests\" (\"status\",\"sender\",\"receiver\") VALUES (?, ?, ?)";
        return saveOrUpdate(saveQuery, entity);
    }

    @Override
    public Optional<FriendshipRequest> delete(Long id) {
        String deleteQuery = "DELETE FROM public.\"Requests\" WHERE \"id\"=?";
        try (PreparedStatement statement = getConnection().prepareStatement(deleteQuery)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete friendship request", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        Optional<FriendshipRequest> existing = findOne(entity.getSender().getId(),entity.getReceiver().getId());

        if (existing.isPresent()) {
            String query = "UPDATE public.\"Requests\" SET \"status\"=? WHERE  \"sender\"=? AND  \"receiver\"=?";
            return saveOrUpdate(query, entity);
        } else {
            return Optional.empty();
        }
    }

    @Override
    protected void setEntityId(FriendshipRequest entity, Object id) {

    }

    @Override
    public Page<FriendshipRequest> findAllOnPage(Pageable pageable){
        List<FriendshipRequest> friendshipRequests=new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(getUrl(),getUsername(),getPassword());
            PreparedStatement pageStatement=connection.prepareStatement("SELECT * FROM public.\"Requests\" LIMIT ? OFFSET ?");
            PreparedStatement countStatement= connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Requests\"")
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
                    friendshipRequests.add(createEntityFromResultSet(pageResultSet));
                }
                return new Page<>(friendshipRequests,count);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
