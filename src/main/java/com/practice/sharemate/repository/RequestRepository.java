package com.practice.sharemate.repository;

import com.practice.sharemate.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
