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

//    @Query("select b " +
//            "from Booking b " +
//            "JOIN FETCH b.booker u " +
//            "where u.id = ?1 " +
//            "limit ?3 " +
//            "offset ?2")
//    List<Booking> findAllByBookerIdPagination(Long userId, int from, int size);

    @Query("select b " +
            "from Booking b " +
            "JOIN FETCH b.booker u " +
            "JOIN FETCH b.item i " +
            "where u.id = ?1 " +
            "and i.id = ?2")
    Booking findBookingByBookerIdAndItemId(Long userId, Long itemId);
}
