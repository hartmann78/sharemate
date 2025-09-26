package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.mapper.RequestMapper;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
            requests = requestRepository.findAll();
        } else {
            requests = requestRepository.findAllByRequestorId(userId);
        }

        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        return requestMapper.listToDto(requests);
    }

    @Override
    public void findAll(int from, int size) {
        Sort sortByDate = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sortByDate);
        do {
            Page<Request> requestPage = requestRepository.findAll(page);

            requestPage.getContent().forEach(System.out::println);

            if (requestPage.hasNext()) {
                page = PageRequest.of(requestPage.getNumber() + 1, requestPage.getSize(), requestPage.getSort());
            } else {
                page = null;
            }
        } while (page != null);
    }

    @Override
    public RequestDTO findUserRequestById(Long userId, Long requestId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id" + userId + " не найден!");
        }

        Optional<Request> request = requestRepository.findById(requestId);

        if (request.isPresent()) {
            return requestMapper.entityToDto(request.get());
        }

        throw new ItemNotFoundException("Запрос с id " + request + " не найден!");
    }

    @Override
    public RequestDTO addRequest(Long userId, Request request) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id" + userId + " не найден!");
        }

        request.setRequestorId(userId);
        request.setAnswers(new ArrayList<>());
        request.setCreated(LocalDateTime.now());

        return requestMapper.entityToDto(requestRepository.save(request));
    }
}
