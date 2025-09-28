package generators;

import repositoryTests.model.Item;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ItemGenerator {
    private final Random random = new Random();

    public Item generateItem(Long ownerId) {
        return Item.builder()
                .name(generateName())
                .description(generateDescription())
                .available(random.nextBoolean())
                .ownerId(ownerId)
                .bookings(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public Item generateItem(Long ownerId, Long requestId) {
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

    private String generateName() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateDescription() {
        return UUID.randomUUID().toString();
    }
}
