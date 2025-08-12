package com.github.not.n0w.livepilot.aiEngine.prompt;

import com.github.not.n0w.livepilot.aiEngine.model.Message;
import com.github.not.n0w.livepilot.model.User;
import com.github.not.n0w.livepilot.model.DialogStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    private String getUserEnvironment(User user) {
        String result = "Информация о пользователе: ";

        result += "Имя: " + (user.getName() == null ? "Неизвестно" : user.getName()) + '\n';
        result += "Пол: " + (user.getGender() == null ? "Неизвестно" : user.getGender()) + '\n';

        return result;
    } // todo user info filing (using tools)

    private String getDialogStyle(DialogStyle dialogStyle) {
        String stylePrompt = promptLoader.loadPromptText(
          "dialogStylePrompt/" + dialogStyle.getStylePromptName() + ".txt"
        ) + '\n';

        return stylePrompt;
    }

    public List<Message> getEnvironmentalMessages(User user) {
        List<Message> messages = new ArrayList<>();

        messages.add(
                new Message(
                        "system",
                            getGlobalEnvironment() +
                                    getUserEnvironment(user) +
                                    getDialogStyle(user.getUsualDialogStyle())
                )
        );

        return messages;
    }
}
