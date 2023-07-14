package com.bragin.bike_theft_check.configuration;

import com.bragin.bike_theft_check.configuration.props.TelegramBotProperties;
import com.bragin.bike_theft_check.model.TelegramFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final TelegramBotProperties properties;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(properties.getBotPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot bot = new TelegramBot(telegramFacade, setWebhook);
        bot.setBotToken(properties.getBotToken());
        bot.setBotUsername(properties.getBotUsername());
        bot.setBotPath(properties.getBotPath());

        return bot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
