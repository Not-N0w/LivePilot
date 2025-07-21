package com.github.not.n0w.livepilot.service;


import org.springframework.web.multipart.MultipartFile;

public interface WhisperService {
    public String voiceToText(MultipartFile voiceFile);
}
