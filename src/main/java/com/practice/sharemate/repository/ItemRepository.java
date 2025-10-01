package com.practice.sharemate.repository;

import com.practice.sharemate.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select id, is_available, description, name, owner_id, request_id " +
            "from items " +
            "where upper(name) like upper('%' || ?1 || '%') and is_available " +
            "or upper(description) like upper('%' || ?1 || '%') and is_available",
            nativeQuery = true)
    List<Item> search(String text);

    List<Item> findAllByOwnerId(Long userId);

    @Query(value = "select id, is_available, description, name, owner_id, request_id " +
            "from items " +
            "where upper(name) like upper('%' || ?1 || '%') and is_available " +
            "or upper(description) like upper('%' || ?1 || '%') and is_available " +
            "limit ?3 " +
            "offset ?2",
            nativeQuery = true)
    List<Item> searchPagination(String text, int from, int size);

    @Query(value = "select id, is_available, description, name, owner_id, request_id " +
            "from items " +
            "where owner_id = ?1 " +
            "limit ?3 " +
            "offset ?2",
            nativeQuery = true)
    List<Item> findAllByOwnerIdPagination(Long userId, int from, int size);
}
