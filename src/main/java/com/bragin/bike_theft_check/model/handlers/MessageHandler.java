package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.BotState;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class MessageHandler {

    private final BotStateCash botStateCash;
    private final MenuService menuService;
    private final BikeCash bikeCash;
    private final BikeHandler bikeHandler;

    private static final String MESSAGE_INFO =
            """
            This bot was created to check the bike for theft. You can also report stolen bikes.
            The check is based on the frame number.
            For cooperation, you can write here: dmitrii.bragin90@gmail.com
            !!!Do not leave your bike unattended!!!
            """;

    public BotApiMethod<?> handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        botStateCash.saveBotState(userId, botState);
        return switch (botState){
            case INFO ->  {
                sendMessage.setText(MESSAGE_INFO);
                yield sendMessage;
            }
            case START -> menuService.getMainMenuMessage(message.getChatId(),
                    "Use general menu", userId);
            case CREATE -> {
                botStateCash.saveBotState(userId, BotState.ENTER_FRAME_NUMBER);
                bikeCash.saveBikeCash(userId, new BikeDto());
                sendMessage.setText("Enter frame number");
                yield sendMessage;
            }
            case FIND_BIKE -> {
                botStateCash.saveBotState(userId, BotState.ENTER_FRAME_NUMBER_FOR_SEARCH);
                sendMessage.setText("Enter frame number for search");
                yield sendMessage;
            }
            case ENTER_FRAME_NUMBER_FOR_SEARCH -> bikeHandler.findBikeByNumber(message, userId);
            case ENTER_FRAME_NUMBER -> bikeHandler.enterFrameNumber(message, userId);
            case ENTER_VENDOR -> bikeHandler.enterVendorName(message, userId);
            case ENTER_MODEL_NAME -> bikeHandler.enterModelName(message, userId);
            case ENTER_DESCRIPTION -> bikeHandler.enterDescription(message, userId);
            case ENTER_WANTED -> bikeHandler.saveBike(message, userId);
            default -> throw new IllegalStateException("Unexpected value: " + botState);
        };
    }
}
