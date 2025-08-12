package com.github.not.n0w.lifepilot.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class RequestDto {
    private String chatId;
    private String text;
    private MultipartFile audio;
    private MultipartFile photo;
}
