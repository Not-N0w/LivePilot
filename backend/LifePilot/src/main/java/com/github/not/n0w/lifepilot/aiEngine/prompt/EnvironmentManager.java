package com.github.not.n0w.lifepilot.aiEngine.prompt;

import com.github.not.n0w.lifepilot.aiEngine.model.Message;
import com.github.not.n0w.lifepilot.model.Chat;
import com.github.not.n0w.lifepilot.model.DialogStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class EnvironmentManager {
    private final PromptLoader promptLoader;

    private String getGlobalEnvironment() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm:ss", new Locale("ru"));
        String formatted = now.format(formatter);
        String dateTime = "Сейчас у пользователя: " + formatted + '\n';

        return dateTime;
    } // todo depends on user location etc

    private String getUserEnvironment(Chat chat) {
        String result = "Информация о пользователе: ";

        result += "Имя: " + (chat.getName() == null ? "Неизвестно" : chat.getName()) + '\n';
        result += "Пол: " + (chat.getGender() == null ? "Неизвестно" : chat.getGender()) + '\n';

        return result;
    } // todo user info filing (using tools)

    private String getDialogStyle(DialogStyle dialogStyle) {
        String stylePrompt = promptLoader.loadPromptText(
          "dialogStylePrompt/" + dialogStyle.getStylePromptName() + ".txt"
        ) + '\n';

        return stylePrompt;
    }

    public List<Message> getEnvironmentalMessages(Chat chat) {
        List<Message> messages = new ArrayList<>();

        messages.add(
                new Message(
                        "system",
                            getGlobalEnvironment() +
                                    getUserEnvironment(chat) +
                                    getDialogStyle(chat.getUsualDialogStyle())
                )
        );

        return messages;
    }
}
