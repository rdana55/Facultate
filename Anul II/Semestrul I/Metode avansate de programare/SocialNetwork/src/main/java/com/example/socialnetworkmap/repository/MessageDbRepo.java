package com.example.socialnetworkmap.repository;

import com.example.socialnetworkmap.domain.FriendshipRequest;
import com.example.socialnetworkmap.domain.Message;
import com.example.socialnetworkmap.domain.User;
import com.example.socialnetworkmap.paging.Page;
import com.example.socialnetworkmap.paging.Pageable;
import com.example.socialnetworkmap.paging.PagingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MessageDbRepo extends AbstractDbRepo<Long, Message> implements PagingRepository<Long, Message> {

    private UserDbRepo userDbRepo;

    public MessageDbRepo(String url, String username, String password, UserDbRepo userDbRepo) {
        super(url, username, password);
        this.userDbRepo=userDbRepo;
    }

    @Override
    protected Message createEntityFromResultSet(ResultSet resultSet) throws SQLException {
        Long senderId = resultSet.getLong("sender");
        Long receiverId = resultSet.getLong("receiver");
        String text = resultSet.getString("text");
        LocalDateTime dateTime=resultSet.getTimestamp("date").toLocalDateTime();
        Long reply=resultSet.getLong("reply");

        Optional<User> sender = userDbRepo.findOne(senderId);
        Optional<User> receiver = userDbRepo.findOne(receiverId);

        if (sender.isPresent()) {
            return new Message(sender.get(), receiver.get(), text,dateTime,reply);
        } else {
            // Puteți trata cazurile în care sender nu a fost găsit în funcție de logica aplicației
            throw new RuntimeException("Sender nu a fost găsit");
        }
    }

    private Set<User> convertReceiverIdsToSet(String receiverIdsString) {
        Set<Long> receiverIds = Arrays.stream(receiverIdsString.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        // Aici poți utiliza userDbRepo pentru a găsi obiectele User asociate ID-urilor
        return receiverIds.stream()
                .map(userDbRepo::findOne)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    protected void setEntityParametersForInsertUpdate(PreparedStatement statement, Message entity) throws SQLException {
        // Setarea parametrilor pentru operațiile de inserare și actualizare în baza de date
        statement.setLong(1, entity.getSender().getId());
        statement.setLong(2, entity.getReceiver().getId());
        statement.setString(3, entity.getText());
        statement.setTimestamp(4,Timestamp.valueOf(entity.getDate()));
        if (entity.getReply() != null) {
            statement.setLong(5, entity.getReply());
        } else {
            statement.setObject(5, null);
        }
    }

    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), getUsername(), getPassword());
    }

    @Override
    public Optional<Message> findOne(Long messageId) {
        String query = "SELECT * FROM public.\"Messages\" WHERE \"messageId\"=?";
        return findOne(query, messageId);
    }

    /*
    @Override
    protected Iterable<Message> findAll(String query, Pageable pageable, Object... params) {
        List<Message> entities = new ArrayList<>();

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

    public Page<Message> findAll(Pageable pageable) {
        String query = "SELECT * FROM public.\"Messages\"";
        Iterable<Message> messages = findAll(query, pageable);
        return new PageImplementation<>(pageable, StreamSupport.stream(messages.spliterator(), false));
    }

     */

    @Override
    public Iterable<Message> findAll() {
        String query = "SELECT * FROM public.\"Messages\"";
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
    public Optional<Message> delete(Long messageId) {
        String deleteQuery = "DELETE FROM public.\"Messages\" WHERE \"messageId\"=?";
        try (PreparedStatement statement = getConnection().prepareStatement(deleteQuery)) {
            statement.setLong(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete message", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        // Implementarea actualizării unui mesaj (dacă este necesar)
        return Optional.empty();
    }

    @Override
    protected void setEntityId(Message entity, Object id) {
        // Setarea ID-ului entității (dacă este necesar)
    }

    public Optional<Message> save(Message message) {
        String saveQuery = "INSERT INTO public.\"Messages\" (\"sender\",\"receiver\", \"text\", \"date\", \"reply\") VALUES (?, ?, ?,?,?)";
        return saveOrUpdate(saveQuery, message);
    }

    @Override
    public Page<Message> findAllOnPage(Pageable pageable){
        List<Message> messages=new ArrayList<>();
        try(Connection connection=DriverManager.getConnection(getUrl(),getUsername(),getPassword());
            PreparedStatement pageStatement=connection.prepareStatement("SELECT * FROM public.\"Messages\" LIMIT ? OFFSET ?");
            PreparedStatement countStatement= connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Messages\"")
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
                    messages.add(createEntityFromResultSet(pageResultSet));
                }
                return new Page<>(messages,count);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
