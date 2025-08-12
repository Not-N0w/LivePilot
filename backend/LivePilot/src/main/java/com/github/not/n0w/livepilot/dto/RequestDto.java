package com.github.not.n0w.livepilot.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class RequestDto {
    private Long userId;
    private String text;
    private MultipartFile audio;
    private MultipartFile photo;
}
