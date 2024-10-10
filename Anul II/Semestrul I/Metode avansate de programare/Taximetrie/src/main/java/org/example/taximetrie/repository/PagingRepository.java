package org.example.taximetrie.repository;

import org.example.taximetrie.domain.Entity;
import org.example.taximetrie.paging.Page;
import org.example.taximetrie.paging.Pageable;

public interface PagingRepository<ID,E extends Entity<ID>> extends Repository<ID,E> {
    Page<E> findAllOnPage(Pageable pageable);
}
