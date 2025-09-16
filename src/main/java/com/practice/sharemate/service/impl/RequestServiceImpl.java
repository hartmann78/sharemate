package com.practice.sharemate.service.impl;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.exceptions.ItemNotFoundException;
import com.practice.sharemate.mapper.RequestMapper;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.repository.RequestRepository;
import com.practice.sharemate.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<RequestDTO> findAll() {
        List<Request> requests = requestRepository.findAll();

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
    public RequestDTO findRequestById(Long requestId) {
        Optional<Request> request = requestRepository.findById(requestId);

        if (request.isPresent()) {
            return requestMapper.entityToDto(request.get());
        }

        throw new ItemNotFoundException("Запрос с id " + request + " не найден!");
    }

    @Override
    public RequestDTO addRequest(Request request) {
        request.setCreated(LocalDateTime.now());
        return requestMapper.entityToDto(requestRepository.save(request));
    }
}
