package com.practice.sharemate.generators;

import com.practice.sharemate.model.Item;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
public class ItemGenerator {
    private final Random random = new Random();

    public Item generateItem() {
        return Item.builder()
                .name(generateName())
                .description(generateDescription())
                .available(random.nextBoolean())
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Item generateItemWithOwnerId(Long ownerId) {
        return Item.builder()
                .name(generateName())
                .description(generateDescription())
                .available(random.nextBoolean())
                .ownerId(ownerId)
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Item generateAvailableItem() {
        return Item.builder()
                .name(generateName())
                .description(generateDescription())
                .available(true)
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Item generateAvailableItemWithOwnerId(Long ownerId) {
        return Item.builder()
                .name(generateName())
                .description(generateDescription())
                .available(true)
                .ownerId(ownerId)
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Item generateItemWithOwnerIdAndRequestId(Long ownerId, Long requestId) {
        return Item.builder()
                .name(generateName())
                .description(generateDescription())
                .available(random.nextBoolean())
                .ownerId(ownerId)
                .requestId(requestId)
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public String generateName() {
        return RandomString.make(8);
    }

    public String generateDescription() {
        return RandomString.make(32);
    }
}
