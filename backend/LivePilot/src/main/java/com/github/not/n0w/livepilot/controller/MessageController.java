package com.github.not.n0w.livepilot.controller;


import com.github.not.n0w.livepilot.dto.RequestDto;
import com.github.not.n0w.livepilot.dto.ResponseDto;
import com.github.not.n0w.livepilot.service.JwtService;
import com.github.not.n0w.livepilot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final JwtService jwtService;

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto handleMultipartMessage(
            @RequestPart(value = "text", required = false) String text,
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Missing or invalid Authorization header") {};
        }

        String token = authHeader.substring(7);

        Long userId = jwtService.extractUserId(token);

        RequestDto dto = RequestDto.builder()
                .userId(userId)
                .text(text)
                .audio(audio)
                .photo(photo)
                .build();

        String gptResponse = messageService.handleMessage(dto);
        return ResponseDto.builder().text(gptResponse).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handle(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", ex.getMessage()));
    }
}
