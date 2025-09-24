package generators;

import repositoryTests.model.Booking;
import repositoryTests.model.BookingRequest;
import repositoryTests.model.Item;
import repositoryTests.model.User;

public class BookingGenerator {
    public Booking generateBooking(BookingRequest bookingRequest, User booker, Item item) {
        return Booking.builder()
                .start(bookingRequest.getStart())
                .end(bookingRequest.getEnd())
                .status(Booking.BookingStatus.WAITING)
                .booker(booker)
                .item(item)
                .build();
    }
}
