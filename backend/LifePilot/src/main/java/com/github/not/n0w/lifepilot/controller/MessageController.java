package com.github.not.n0w.lifepilot.controller;


import com.github.not.n0w.lifepilot.dto.RequestDto;
import com.github.not.n0w.lifepilot.dto.ResponseDto;
import com.github.not.n0w.lifepilot.model.User;
import com.github.not.n0w.lifepilot.repository.UserRepository;
import com.github.not.n0w.lifepilot.service.JwtService;
import com.github.not.n0w.lifepilot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto handleMultipartMessage(
            @RequestPart(value = "text", required = false) String text,
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AuthenticationException("User not found") {}
        );

        RequestDto dto = RequestDto.builder()
                .userId(user.getId())
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
