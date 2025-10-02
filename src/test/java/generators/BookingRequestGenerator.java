package generators;

import com.practice.sharemate.model.BookingRequest;

import java.time.LocalDateTime;
import java.util.Random;

public class BookingRequestGenerator {
    private final Random random = new Random();
    private final DateGenerator dateGenerator = new DateGenerator();

    public BookingRequest generateBookingRequest(Long itemId) {
        LocalDateTime start = dateGenerator.generateRandomDate();
        LocalDateTime end = start.plusDays(random.nextLong(1, 8));

        return BookingRequest.builder()
                .itemId(itemId)
                .start(start)
                .end(end)
                .build();
    }
}
