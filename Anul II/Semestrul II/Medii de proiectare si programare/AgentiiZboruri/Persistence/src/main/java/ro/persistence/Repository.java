package ro.persistence;

import ro.model.Entity;

import java.util.Map;
import java.util.Optional;

public interface Repository <ID, E extends Entity<ID>>{
    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    Optional<E> save(E entity);

    Optional<E> delete(ID id);

    Optional<E> update(E entity);

    Iterable<E> changeEntities(Map<ID, E> entities);
}

