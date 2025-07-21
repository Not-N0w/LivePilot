package com.github.not.n0w.telegrambot.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class RequestDto {
    private String chatId;
    private String text;
    private byte[] audio;
    private byte[] photo;
}
