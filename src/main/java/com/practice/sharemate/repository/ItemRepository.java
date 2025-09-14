package com.practice.sharemate.repository;

import com.practice.sharemate.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByNameIgnoreCaseAndAvailableTrueOrDescriptionIgnoreCaseAndAvailableTrue(String name, String description);

    List<Item> findAllByOwnerId(Long userId);
}
