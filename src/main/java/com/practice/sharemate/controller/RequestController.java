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
    public List<RequestDTO> findAll() {
        return requestService.findAll();
    }

    @GetMapping("/all")
    public void findAll(@RequestParam int from, @RequestParam int size) {
        requestService.findAll(from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDTO findRequestById(@PathVariable Long requestId) {
        return requestService.findRequestById(requestId);
    }

    @PostMapping
    public RequestDTO addRequest(@Valid @RequestBody Request request) {
        return requestService.addRequest(request);
    }
}
