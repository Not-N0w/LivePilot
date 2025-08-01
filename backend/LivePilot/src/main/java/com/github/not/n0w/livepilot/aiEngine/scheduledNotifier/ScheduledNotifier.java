package com.github.not.n0w.livepilot.aiEngine.scheduledNotifier;

import com.github.not.n0w.livepilot.aiEngine.AiManager;
import com.github.not.n0w.livepilot.aiEngine.model.AiRequest;
import com.github.not.n0w.livepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.model.AiTaskType;
import com.github.not.n0w.livepilot.model.Chat;
import com.github.not.n0w.livepilot.model.SavedMessage;
import com.github.not.n0w.livepilot.repository.ChatRepository;
import com.github.not.n0w.livepilot.repository.SavedMessagesRepository;
import com.github.not.n0w.livepilot.service.AIService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledNotifier {

    private final ChatRepository chatRepository;
    private final AiManager aiManager;
    private final SavedMessagesRepository savedMessagesRepository;
    private final AIService aiService;
    private final TimingLoader timingLoader;
    private final TaskScheduler taskScheduler = createScheduler();

    @PostConstruct
    public void scheduleDailyMessages() {
        List<LocalTime> times = timingLoader.getTimings();
        for (LocalTime time : times) {
            scheduleAt(time);
        }
    }

    private void scheduleAt(LocalTime time) {
        Runnable task = this::sendDailyMessage;

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime firstRun = now.with(time);
        if (now.compareTo(firstRun) > 0) {
            firstRun = firstRun.plusDays(1);
        }

        long initialDelay = Duration.between(now, firstRun).toMillis();
        long period = Duration.ofDays(1).toMillis();

        taskScheduler.scheduleAtFixedRate(task, new Date(System.currentTimeMillis() + initialDelay), period);
        log.info("Scheduled daily message at {}", time);
    }

    public void sendDailyMessage() {
        log.info("Sending Daily Message");

        chatRepository.globalSetTaskGetMetrics();

        List<Chat> chats = chatRepository.findAll();
        for (Chat chat : chats) {
            List<SavedMessage> savedMessages = savedMessagesRepository.findAllByChatId(chat.getId());
            List<Message> messageList = new ArrayList<>(savedMessages
                    .stream()
                    .map(msg -> new Message(msg.getRole(), msg.getMessage()))
                    .toList());
            chat.setExtraState(1);
            AiResponse metricsRequest = aiManager.process(new AiRequest(chat, messageList));
            String message = metricsRequest.getAnswerToUser();
            aiService.pushMessageToUser(chat.getId(), message);
        }
    }

    private TaskScheduler createScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("notifier-");
        scheduler.initialize();
        return scheduler;
    }
}
