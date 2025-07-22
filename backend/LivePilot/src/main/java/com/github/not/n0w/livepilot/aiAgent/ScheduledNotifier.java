package com.github.not.n0w.livepilot.aiAgent;

import com.github.not.n0w.livepilot.aiAgent.model.AiAnswer;
import com.github.not.n0w.livepilot.aiAgent.task.AiTask;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledNotifier {

    private final ChatRepository chatRepository;
    private final AiAgent aiAgent;
    private final RestTemplate restTemplate = new RestTemplate(); // можно внедрить как @Bean

    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    public void sendDailyMessage() {
        log.info("Sending Daily Message");

        List<Chat> chats = chatRepository.findAll();

        for (Chat chat : chats) {
            chat.setCurrentTask(AiTask.GET_METRICS);
            chatRepository.save(chat);

            AiAnswer metricsRequest = aiAgent.noContextAsk(chat.getId(), null)
                    .orElseGet(() -> new AiAnswer("", null));

            String message = metricsRequest.getAnswerToUser();

            if (message != null && !message.isBlank()) {
                String url = "http://0.0.0.0:8080/api/send";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("chat_id", chat.getId());
                map.add("text", message);

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

                try {
                    restTemplate.postForObject(url, request, String.class);
                    log.info("Sent message to chat {}", chat.getId());
                } catch (Exception e) {
                    log.error("Failed to send message to chat {}: {}", chat.getId(), e.getMessage());
                }
            }
        }
    }
}
