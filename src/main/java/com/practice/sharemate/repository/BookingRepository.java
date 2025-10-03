package com.practice.sharemate.repository;

import com.practice.sharemate.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long userId);

    @Query(value = "select b.id, b.start_date, b.end_date, b.booker_id, b.item_id, b.status " +
            "from bookings b " +
            "where booker_id = ?1 " +
            "limit ?3 " +
            "offset ?2",
            nativeQuery = true)
    List<Booking> findAllByBookerIdPagination(Long userId, int from, int size);

    @Query(value = "select b.id, b.start_date, b.end_date, b.booker_id, b.item_id, b.status " +
            "from bookings b " +
            "inner join items i on b.item_id = i.id " +
            "where i.owner_id = ?1",
            nativeQuery = true)
    List<Booking> findAllBookingsToOwner(Long userId);

    @Query(value = "select b.id, b.start_date, b.end_date, b.booker_id, b.item_id, b.status " +
            "from bookings b " +
            "inner join items i on b.item_id = i.id " +
            "where i.owner_id = ?1 " +
            "limit ?3 " +
            "offset ?2",
            nativeQuery = true)
    List<Booking> findAllBookingsToOwnerPagination(Long userId, int from, int size);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker u " +
            "JOIN FETCH b.item i " +
            "where u.id = ?1 " +
            "and i.id = ?2")
    Booking findBookingByBookerIdAndItemId(Long userId, Long itemId);
}
