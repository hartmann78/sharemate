package com.practice.sharemate.repository;

import com.practice.sharemate.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequestorIdOrderByCreatedDesc(Long userId);

    Request findByIdAndRequestorId(Long requestId, Long userId);
}
