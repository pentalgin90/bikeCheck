package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.configuration.TelegramBot;
import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.BotState;
import com.bragin.bike_theft_check.services.BikeService;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BikeHandler {
    private final BikeCash bikeCash;
    private final BotStateCash botStateCash;
    private final BikeService bikeService;
    private final MenuService menuService;
    private final MessageSource messageSource;
    private final TelegramBot telegramBot;
    private static final String BIKE_MESSAGE_PATTERN =
            """
                    Frame number: %s
                    Vendor: %s
                    Model name: %s
                    Description: %s
                    Wanted: %b
                    %s
                    """;//TODO добавить в локализацию
    private static final String BIKE_MESSAGE =
            """
                    Frame number: %s
                    Vendor: %s
                    Model name: %s
                    Description: %s
                    Wanted: %b
                    """;//TODO добавить в локализацию
    private static final String NOT_FIND = EmojiParser.parseToUnicode(":white_check_mark:");
    private static final String FIND = EmojiParser.parseToUnicode(":x:");
    public BikeHandler(
            BikeCash bikeCash,
            BotStateCash botStateCash,
            BikeService bikeService,
            MenuService menuService,
            MessageSource messageSource,
            @Lazy TelegramBot telegramBot
    ) {
        this.bikeCash = bikeCash;
        this.botStateCash = botStateCash;
        this.bikeService = bikeService;
        this.menuService = menuService;
        this.messageSource = messageSource;
        this.telegramBot = telegramBot;
    }

    public BotApiMethod<?> enterFrameNumber(Message message, long userId, Locale locale) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String frameNumber = message.getText();
        if (Strings.isBlank(frameNumber)) {
            sendMessage.setText("Frame number mustn't be empty");//TODO добавить в локализацию
            return sendMessage;
        }
        botStateCash.saveBotState(userId, BotState.ENTER_VENDOR);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setFrameNumber(frameNumber);
        bikeCash.saveBikeCash(userId, bike);
        sendMessage.setText(messageSource.getMessage("msg.vendor", new Object[]{}, locale));
        return sendMessage;
    }

    public BotApiMethod<?> enterVendorName(Message message, long userId, Locale locale) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String vendor = message.getText();
        if (Strings.isBlank(vendor)) {
            sendMessage.setText("Vendor mustn't be empty");//TODO добавить в локализацию
            return sendMessage;
        }
        botStateCash.saveBotState(userId, BotState.ENTER_MODEL_NAME);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setVendor(vendor);
        bikeCash.saveBikeCash(userId, bike);
        sendMessage.setText(messageSource.getMessage("msg.model", new Object[]{}, locale));
        return sendMessage;
    }

    public BotApiMethod<?> enterModelName(Message message, long userId, Locale locale) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String modelName = message.getText();
        if (Strings.isBlank(modelName)) {
            sendMessage.setText("Model name mustn't be empty");//TODO добавить в локализацию
            return sendMessage;
        }
        botStateCash.saveBotState(userId, BotState.ENTER_DESCRIPTION);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setModelName(modelName);
        bikeCash.saveBikeCash(userId, bike);
        sendMessage.setText(messageSource.getMessage("msg.description", new Object[]{}, locale));
        return sendMessage;
    }

    public BotApiMethod<?> enterDescription(Message message, long userId, Locale locale) {
        String description = message.getText();
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setDescription(description);
        bikeCash.saveBikeCash(userId, bike);
        return menuService.getWantedMenu(message.getChatId(), messageSource.getMessage("msg.status", new Object[]{}, locale), locale);
    }

    public BotApiMethod<?> findBikeByNumber(Message message, long userId, Locale locale) {
        String frameNumber = message.getText();
        BikeDto bike = bikeService.findBikeByFrameNumber(frameNumber);
        if (Objects.nonNull(bike)) {
            botStateCash.saveBotState(userId, BotState.START);
            StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(
                    BIKE_MESSAGE_PATTERN,
                    bike.getFrameNumber(),
                    bike.getVendor(),
                    bike.getModelName(),
                    bike.getDescription(),
                    bike.getWanted(),
                    FIND
            );
            return menuService.getMainMenuMessage(message.getChatId(), stringFormattedMessage.toString(), userId);
        } else {
            return menuService.getMainMenuMessage(message.getChatId(), NOT_FIND, userId);
        }
    }

    public void getAllBikes(Long chatId, long userId) {
        List<BikeDto> allBikesByUserId = bikeService.getAllBikesByUserId(userId);
        List<String> messages = allBikesByUserId.stream()
                .map(bike -> {
                    StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(
                            BIKE_MESSAGE,
                            bike.getFrameNumber(),
                            bike.getVendor(),
                            bike.getModelName(),
                            bike.getDescription(),
                            bike.getWanted()
                    );
                    return stringFormattedMessage.toString();
                })
                .collect(Collectors.toList());
        List<SendMessage> updateMenu = menuService.getUpdateMenu(chatId, messages);
        updateMenu.forEach(s -> {
            telegramBot.sendMessage(s);
        });
    }
}
