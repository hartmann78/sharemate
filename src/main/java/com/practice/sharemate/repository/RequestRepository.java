package com.practice.sharemate.repository;

import com.practice.sharemate.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequestorIdOrderByCreatedDesc(Long userId);

    @Query(value = "select r.id, r.requestor_id, r.description, r.created, r.updated " +
            "from requests r " +
            "where r.id = ?1 " +
            "and r.requestor_id = ?2",
            nativeQuery = true)
    Optional<Request> findByRequestIdAndRequestorId(Long requestId, Long requestorId);
}
