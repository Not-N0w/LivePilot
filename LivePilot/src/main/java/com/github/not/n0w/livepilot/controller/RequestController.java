package com.github.not.n0w.livepilot.controller;


import com.github.not.n0w.livepilot.dto.RequestDto;
import com.github.not.n0w.livepilot.dto.ResponseDto;
import com.github.not.n0w.livepilot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RequestController {
    private final MessageService messageService;

    @PostMapping(value = "/message", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto handleMultipartMessage(
            @RequestPart("chatId") String chatId,
            @RequestPart(value = "text", required = false) String text,
            @RequestPart(value = "audio", required = false) MultipartFile audio,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        RequestDto dto = RequestDto.builder()
                .chatId(chatId)
                .text(text)
                .audio(audio)
                .photo(photo)
                .build();

        String gptResponse = messageService.handleMessage(dto);
        return ResponseDto.builder().text(gptResponse).build();
    }

}
