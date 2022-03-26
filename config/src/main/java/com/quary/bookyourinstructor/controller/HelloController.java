package com.quary.bookyourinstructor.controller;

import bookyourinstructor.usecase.util.time.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
@Slf4j
public class HelloController {

    private final TimeUtils timeUtils;

    @GetMapping
    public String hello() {
        Instant nowInstant = timeUtils.nowInstant();
        LocalDateTime nowLocalDateTime = timeUtils.nowLocalDateTime();
        log.info(nowInstant.toString());
        log.info(nowLocalDateTime.toString());
        log.info(timeUtils.toInstant(nowLocalDateTime).toString());
        log.info(timeUtils.toLocalDateTime(nowInstant).toString());
        return "Hello world";
    }

}
