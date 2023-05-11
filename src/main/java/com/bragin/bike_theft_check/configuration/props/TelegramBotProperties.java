package com.bragin.bike_theft_check.configuration.props;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramBotProperties {
    @Value("${telegram.botPath}")
    String botPath;
    @Value("${telegram.botUsername}")
    String botUsername;
    @Value("${telegram.botToken}")
    String botToken;
}
