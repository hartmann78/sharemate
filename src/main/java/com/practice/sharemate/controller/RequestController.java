package com.practice.sharemate.controller;

import com.practice.sharemate.dto.RequestDTO;
import com.practice.sharemate.model.Request;
import com.practice.sharemate.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDTO> findAll(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        return requestService.findAll(userId);
    }

    @GetMapping("/all")
    public List<RequestDTO> findAllPagination(@RequestParam int from,
                                              @RequestParam int size) {
        return requestService.findAllPagination(from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDTO findUserRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId) {
        return requestService.findUserRequestById(userId, requestId);
    }

    @PostMapping
    public RequestDTO addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody Request request) {
        return requestService.addRequest(userId, request);
    }

    @DeleteMapping("/{requestId}")
    public void deleteRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestParam Long requestId) {
        requestService.deleteRequest(userId, requestId);
    }
}
