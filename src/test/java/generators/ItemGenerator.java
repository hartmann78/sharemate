package generators;

import com.practice.sharemate.model.Item;
import net.bytebuddy.utility.RandomString;

import java.util.ArrayList;
import java.util.Random;

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

    public String generateName() {
        return RandomString.make(8);
    }

    public String generateDescription() {
        return RandomString.make(32);
    }
}
