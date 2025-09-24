package repositoryTests.repository;

import repositoryTests.model.Booking;
import repositoryTests.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select b.id, b.start_date, b.end_date, b.booker_id, b.item_id, b.status " +
            "from bookings as b " +
            "where b.booker_id = ?1 " +
            "  and b.item_id = ?2",
            nativeQuery = true)
    Booking findBooking(Long userId, Long itemId);
}
