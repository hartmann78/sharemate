package com.practice.sharemate.service;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.model.Request;

import java.util.List;

public interface RequestService {
    List<RequestDTO> findAll();

    void findAll(int from, int size);

    RequestDTO findRequestById(Long requestId);

    RequestDTO addRequest(Request request);
}
