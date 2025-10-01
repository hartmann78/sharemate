package repositoryTests.repository;

import org.springframework.data.jpa.repository.Query;
import repositoryTests.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select id, is_available, description, name, owner_id, request_id " +
            "from items " +
            "where upper(name) like upper('%' || ?1 || '%') and is_available " +
            "or upper(description) like upper('%' || ?1 || '%') and is_available",
            nativeQuery = true)
    List<Item> search(String text);

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

    List<Item> findAllByOwnerId(Long userId);

    Item findByIdAndOwnerId(Long itemId, Long userId);
}
