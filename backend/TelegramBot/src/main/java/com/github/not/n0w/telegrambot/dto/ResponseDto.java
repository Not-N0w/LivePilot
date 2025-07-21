package com.github.not.n0w.telegrambot.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ResponseDto {
    private String text;
    private byte[] audio;
    private byte[] photo;
}