package com.example.socialnetworkmap.paging;

import com.example.socialnetworkmap.domain.Entity;
import com.example.socialnetworkmap.repository.Repository;

public interface PagingRepository<ID,E extends Entity<ID>> extends Repository<ID,E> {
    Page<E> findAllOnPage(Pageable pageable);
}
