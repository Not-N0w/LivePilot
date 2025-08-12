package com.github.not.n0w.lifepilot.aiEngine.scheduledNotifier;

import com.github.not.n0w.lifepilot.aiEngine.AiManager;
import com.github.not.n0w.lifepilot.aiEngine.model.AiRequest;
import com.github.not.n0w.lifepilot.aiEngine.model.AiResponse;
import com.github.not.n0w.lifepilot.aiEngine.model.Message;
import com.github.not.n0w.lifepilot.model.AiTaskType;
import com.github.not.n0w.lifepilot.model.Chat;
import com.github.not.n0w.lifepilot.model.SavedMessage;
import com.github.not.n0w.lifepilot.repository.ChatRepository;
import com.github.not.n0w.lifepilot.repository.SavedMessagesRepository;
import com.github.not.n0w.lifepilot.service.AIService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

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


        List<Chat> chats = chatRepository.findAll();
        for (Chat chat : chats) {
            List<SavedMessage> savedMessages = savedMessagesRepository.findAllByChatId(chat.getId());
            List<Message> messageList = new ArrayList<>(savedMessages
                    .stream()
                    .map(msg -> new Message(msg.getRole(), msg.getMessage()))
                    .toList());

            if(chat.getTask() == AiTaskType.GET_METRICS) {
                chat.setExtraState(2);
            } else {
                chat.setExtraState(1);
            }
            chat.setTask(AiTaskType.GET_METRICS);
            chatRepository.save(chat);
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
