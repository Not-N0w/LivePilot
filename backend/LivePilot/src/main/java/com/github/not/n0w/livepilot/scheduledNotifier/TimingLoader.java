package com.github.not.n0w.livepilot.scheduledNotifier;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TimingLoader {

    private final List<LocalTime> timings;

    public TimingLoader() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/NotifierTiming.txt")) {
            List<String> timeStrings = mapper.readValue(is, new TypeReference<>() {});
            this.timings = timeStrings.stream()
                    .map(LocalTime::parse)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to load timings", e);
            throw new RuntimeException(e);
        }
    }

    public List<LocalTime> getTimings() {
        return timings;
    }
}