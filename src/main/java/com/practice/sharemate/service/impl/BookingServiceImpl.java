package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.BookingDTO;
import com.practice.sharemate.model.BookingRequest;
import com.practice.sharemate.mapper.BookingMapper;
import com.practice.sharemate.exceptions.*;
import com.practice.sharemate.model.Booking;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.BookingRepository;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingDTO> findAll(Long userId, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неправильный запрос");
        }

        List<Booking> bookings;

        if (userId == null) {
            bookings = bookingRepository.findAll(PageRequest.of(from, size)).getContent();
        } else {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
            }

            bookings = bookingRepository.findAllByBookerIdPagination(userId, from, size);
        }

        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования не найдены!");
        }

        return bookingMapper.listToDto(bookings);
    }

    @Override
    public List<BookingDTO> findAllBookingsToOwner(Long userId, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неправильный запрос");
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        List<Booking> bookings = bookingRepository.findAllBookingsToOwnerPagination(userId, from, size);

        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("Бронирования не найдены!");
        }

        return bookingMapper.listToDto(bookings);
    }

    @Override
    public BookingDTO findBookingById(Long userId, Long bookingId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Booking> findBooking = bookingRepository.findById(bookingId);
        if (findBooking.isEmpty()) {
            throw new BookingNotFoundException("Бронирование с id " + bookingId + " не найдено!");
        }

        Booking booking = findBooking.get();
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Доступ воспрещён!");
        }

        return bookingMapper.entityToDto(booking);
    }

    @Override
    public BookingDTO addBooking(Long userId, BookingRequest bookingRequest) {
        if (bookingRequest.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Дата и время начала бронирования не может быть в прошлом!");
        }

        if (bookingRequest.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Дата и время конца бронирования не может быть в прошлом!");
        }

        if (bookingRequest.getEnd().isBefore(bookingRequest.getStart())) {
            throw new BadRequestException("Дата и время конца бронирования не должно быть раньше начала!");
        }

        if (bookingRequest.getStart().equals(bookingRequest.getEnd())) {
            throw new BadRequestException("Дата и время начала и конца бронирования не должны быть равны!");
        }

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Item> item = itemRepository.findById(bookingRequest.getItemId());
        if (item.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + bookingRequest.getItemId() + " не найден!");
        }

        if (item.get().getAvailable() == false) {
            throw new BadRequestException("Предмет недоступен!");
        }

        Booking booking = bookingMapper.requestToEntity(bookingRequest);

        booking.setItem(item.get());
        booking.setBooker(user.get());
        booking.setStatus(Booking.BookingStatus.WAITING);

        return bookingMapper.entityToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDTO patchBooking(Long userId, Long bookingId, Boolean approved) {
        Optional<Booking> findBooking = bookingRepository.findById(bookingId);
        if (findBooking.isEmpty()) {
            throw new BookingNotFoundException("Бронирование с id " + bookingId + " не найдено!");
        }

        Long itemId = findBooking.get().getItem().getId();
        Optional<Item> findItem = itemRepository.findById(itemId);
        if (findItem.isEmpty()) {
            throw new ItemNotFoundException("Предмет с id " + itemId + " не найден!");
        }

        if (!userId.equals(findItem.get().getOwner().getId())) {
            throw new ForbiddenException("Доступ воспрещён!");
        }

        Booking updateBooking = findBooking.get();

        if (approved) {
            updateBooking.setStatus(Booking.BookingStatus.APPROVED);
        } else {
            updateBooking.setStatus(Booking.BookingStatus.REJECTED);
        }

        return bookingMapper.entityToDto(bookingRepository.save(updateBooking));
    }

    @Override
    public void deleteBooking(Long userId, Long bookingId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new BookingNotFoundException("Бронирование с id " + bookingId + " не найдено!");
        }

        if (!booking.get().getBooker().getId().equals(userId)) {
            throw new ForbiddenException("Доступ воспрещён!");
        }

        bookingRepository.deleteById(bookingId);
    }
}
