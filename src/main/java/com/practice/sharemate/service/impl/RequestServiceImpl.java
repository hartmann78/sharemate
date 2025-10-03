package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.exceptions.*;
import com.practice.sharemate.mapper.RequestMapper;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    @Override
    public List<RequestDTO> findAll(Long userId) {
        List<Request> requests;

        if (userId == null) {
            Sort sortByDate = Sort.by(Sort.Direction.DESC, "created");
            requests = requestRepository.findAll(sortByDate);
        } else {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
            }

            requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        }

        if (requests.isEmpty()) {
            throw new RequestNotFoundException("Запросы не найдены");
        }

        return requestMapper.listToDto(requests);
    }

    @Override
    public List<RequestDTO> findAllPagination(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Неправильный запрос");
        }

        Sort sortByDate = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sortByDate);
        List<Request> requests = requestRepository.findAll(page).getContent();

        if (requests.isEmpty()) {
            throw new RequestNotFoundException("Запросы не найдены");
        }

        return requestMapper.listToDto(requests);
    }

    @Override
    public RequestDTO findUserRequestById(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new RequestNotFoundException("Запрос с id " + request + " не найден!");
        }

        if (!request.get().getRequestor().getId().equals(userId)) {
            throw new ForbiddenException("id пользователя не совпадает с id автора запроса");
        }

        return requestMapper.entityToDto(request.get());
    }

    @Override
    public RequestDTO addRequest(Long userId, Request request) {
        Optional<User> requestor = userRepository.findById(userId);
        if (requestor.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        request.setRequestor(requestor.get());
        request.setAnswers(new ArrayList<>());
        request.setCreated(LocalDateTime.now());

        return requestMapper.entityToDto(requestRepository.save(request));
    }

    @Override
    public void deleteRequest(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id " + userId + " не найден!");
        }

        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new RequestNotFoundException("Запрос с id " + request + " не найден!");
        }

        if (!request.get().getRequestor().getId().equals(userId)) {
            throw new ForbiddenException("Доступ воспрещён!");
        }

        requestRepository.deleteById(requestId);
    }
}
