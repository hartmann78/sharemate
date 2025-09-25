package com.practice.sharemate.service;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.model.Request;

import java.util.List;

public interface RequestService {
    List<RequestDTO> findAll(Long userId);

    void findAll(int from, int size);

    RequestDTO findUserRequestById(Long userId, Long requestId);

    List<RequestDTO> findUserRequests(Long userId);

    RequestDTO addRequest(Long userId, Request request);
}
