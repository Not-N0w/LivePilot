package com.github.not.n0w.livepilot.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto {
    private String text;
    private byte[] audio;
    private byte[] photo;
}