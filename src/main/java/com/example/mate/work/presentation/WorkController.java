package com.example.mate.work.presentation;

import com.example.mate.work.application.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/works")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;
}
