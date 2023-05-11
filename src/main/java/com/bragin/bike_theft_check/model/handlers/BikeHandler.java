package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.BotState;
import com.bragin.bike_theft_check.services.BikeService;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BikeHandler {
    private final BikeCash bikeCash;
    private final BotStateCash botStateCash;
    private final BikeService bikeService;
    private final MenuService menuService;

    private static final String BIKE_MESSAGE_PATTERN =
            """
            Frame number: %s
            Vendor: %s
            Model name: %s
            Description: %s
            Wanted: %b
            """;
    public BotApiMethod<?> enterFrameNumber(Message message, long userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String frameNumber = message.getText();
        if (Strings.isBlank(frameNumber)) {
            sendMessage.setText("Frame number mustn't be empty");
            return sendMessage;
        }
        botStateCash.saveBotState(userId, BotState.ENTER_VENDOR);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setFrameNumber(frameNumber);
        bikeCash.saveBikeCash(userId, bike);
        sendMessage.setText("Enter vendor name");
        return sendMessage;
    }

    public BotApiMethod<?> enterVendorName(Message message, long userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String vendor = message.getText();
        if (Strings.isBlank(vendor)) {
            sendMessage.setText("Vendor mustn't be empty");
            return sendMessage;
        }
        botStateCash.saveBotState(userId, BotState.ENTER_MODEL_NAME);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setVendor(vendor);
        bikeCash.saveBikeCash(userId, bike);
        sendMessage.setText("Enter model name");
        return sendMessage;
    }

    public BotApiMethod<?> enterModelName(Message message, long userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        String modelName = message.getText();
        if (Strings.isBlank(modelName)) {
            sendMessage.setText("Model name mustn't be empty");
            return sendMessage;
        }
        botStateCash.saveBotState(userId, BotState.ENTER_DESCRIPTION);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setModelName(modelName);
        bikeCash.saveBikeCash(userId, bike);
        sendMessage.setText("Enter description or other information");
        return sendMessage;
    }

    public BotApiMethod<?> enterDescription(Message message, long userId) {
        String description = message.getText();
        botStateCash.saveBotState(userId, BotState.ENTER_WANTED);
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        bike.setDescription(description);
        bikeCash.saveBikeCash(userId, bike);
        return menuService.getWantedMenu(message.getChatId(), "chose wanted status");
    }

    public BotApiMethod<?> saveBike(Message message, long userId) {
        List<PhotoSize> photo = message.getPhoto();
        String fileId = photo.get(0).getFileId();
        BikeDto bike = bikeCash.getBikeMap().get(userId);
        BikeDto report = bikeService.createReport(bike);
        bikeCash.saveBikeCash(userId, new BikeDto());
        botStateCash.saveBotState(userId, BotState.START);
        if (Objects.nonNull(report)) {
            return menuService.getMainMenuMessage(message.getChatId(), "Report was saved", userId);
        } else {
            return menuService.getMainMenuMessage(message.getChatId(), "Report was not saved", userId);
        }
    }

    public BotApiMethod<?> findBikeByNumber(Message message, long userId) {
        String frameNumber = message.getText();
        BikeDto bike = bikeService.findBikeByFrameNumber(frameNumber);
        botStateCash.saveBotState(userId, BotState.START);
        StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(
                BIKE_MESSAGE_PATTERN,
                bike.getFrameNumber(),
                bike.getVendor(),
                bike.getModelName(),
                bike.getDescription(),
                bike.getWanted()
        );
        return menuService.getMainMenuMessage(message.getChatId(), stringFormattedMessage.toString(), userId);
    }
}
