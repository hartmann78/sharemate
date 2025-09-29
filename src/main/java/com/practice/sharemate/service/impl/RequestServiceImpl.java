package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.exceptions.ForbiddenException;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.exceptions.RequestNotFoundException;
import com.practice.sharemate.exceptions.UserNotFoundException;
import com.practice.sharemate.mapper.RequestMapper;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.model.User;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    @Override
    public List<RequestDTO> findAllUserRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequestorId(userId);

        if (requests.isEmpty()) {
            throw new RequestNotFoundException("Запросы не найдены");
        }

        return requestMapper.listToDto(requests);
    }

    @Override
    public void findAllPagination(int from, int size) {
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
        if (request.isEmpty()) {
            throw new ItemNotFoundException("Запрос с id " + request + " не найден!");
        }

        if (!request.get().getRequestor().getId().equals(userId)) {
            throw new ForbiddenException("id пользователя");
        }

        return requestMapper.entityToDto(request.get());
    }

    @Override
    public RequestDTO addRequest(Long userId, Request request) {
        Optional<User> requestor = userRepository.findById(userId);
        if (requestor.isEmpty()) {
            throw new UserNotFoundException("Пользователь с id" + userId + " не найден!");
        }

        request.setRequestor(requestor.get());
        request.setAnswers(new ArrayList<>());
        request.setCreated(LocalDateTime.now());

        return requestMapper.entityToDto(requestRepository.save(request));
    }
}
