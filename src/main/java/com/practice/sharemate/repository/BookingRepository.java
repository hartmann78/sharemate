package com.practice.sharemate.repository;

import com.practice.sharemate.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.item i " +
            "where i.ownerId = ?1")
    List<Booking> findAllBookingsToOwner(Long userId);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker u " +
            "where u.id = ?1")
    List<Booking> findAllByBookerId(Long userId);

    @Query(value = "select b.id, b.start_date, b.end_date, b.booker_id, b.item_id, b.status " +
            "from bookings as b " +
            "where b.booker_id = ?1 " +
            "  and b.item_id = ?2",
            nativeQuery = true)
    Booking findBookingByBookerIdAndItemId(Long userId, Long itemId);
}
