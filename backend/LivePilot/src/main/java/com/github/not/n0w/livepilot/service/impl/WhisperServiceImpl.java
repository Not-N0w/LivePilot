package com.github.not.n0w.livepilot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.not.n0w.livepilot.service.WhisperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Slf4j
@RequiredArgsConstructor
public class WhisperServiceImpl implements WhisperService {

    @Qualifier("whisperClient")
    @Autowired
    private WebClient whisperWebClient;

    @Override
    public String voiceToText(MultipartFile voiceFile) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", voiceFile.getResource())
                .filename("voice.ogg")
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        String response = whisperWebClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Whisper API response: {}", response);
        return response;
    }

}
