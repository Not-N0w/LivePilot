package com.github.not.n0w.telegrambot;

import com.github.not.n0w.telegrambot.dto.RequestDto;
import com.github.not.n0w.telegrambot.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class Transmitter {
    private final WebClient transmitterWebClient;

    public ResponseDto sendToServer(RequestDto dto) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        if (dto.getChatId() != null) {
            builder.part("chatId", dto.getChatId());
        }

        if (dto.getText() != null) {
            builder.part("text", dto.getText());
        }

        if (dto.getAudio() != null) {
            builder.part("audio", new ByteArrayResource(dto.getAudio()) {
                @Override
                public String getFilename() {
                    return "voice.ogg";
                }
            }).contentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        if (dto.getPhoto() != null) {
            builder.part("photo", new ByteArrayResource(dto.getPhoto()) {
                @Override
                public String getFilename() {
                    return "image.jpg";
                }
            }).contentType(MediaType.APPLICATION_OCTET_STREAM);
        }

        return transmitterWebClient.post()
                .uri("/api/message")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(ResponseDto.class)
                .onErrorResume(e -> {
                    log.error("Error sending data to server", e);
                    return Mono.just(new ResponseDto());
                })
                .block();
    }

    private ByteArrayResource toByteArrayResource(org.springframework.web.multipart.MultipartFile multipartFile, String filename) {
        try {
            return new ByteArrayResource(multipartFile.getBytes()) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };
        } catch (Exception e) {
            log.error("Error converting multipart file", e);
            throw new RuntimeException("Failed to read multipart file", e);
        }
    }
}
