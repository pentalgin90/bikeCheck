package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.BotState;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Locale;

import static com.bragin.bike_theft_check.utils.CreateLocale.getLocale;

@Service
@RequiredArgsConstructor
public class MessageHandler {

    private final BotStateCash botStateCash;
    private final MenuService menuService;
    private final BikeCash bikeCash;
    private final BikeHandler bikeHandler;
    private final MessageSource messageSource;

    public BotApiMethod<?> handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        botStateCash.saveBotState(userId, botState);
        return switch (botState){
            case INFO ->  {
                sendMessage.setText(messageSource.getMessage("info", new Object[]{}, locale));
                yield sendMessage;
            }
            case START -> {
                String text = messageSource.getMessage("msg.use", new Object[]{}, locale);
                yield menuService.getMainMenuMessage(message.getChatId(), text, userId);
            }
            case CREATE -> {
                botStateCash.saveBotState(userId, BotState.ENTER_FRAME_NUMBER);
                bikeCash.saveBikeCash(userId, new BikeDto());
                sendMessage.setText(messageSource.getMessage("msg.frame", new Object[]{}, locale));
                yield sendMessage;
            }
            case CHECK_BIKE -> {
                botStateCash.saveBotState(userId, BotState.ENTER_FRAME_NUMBER_FOR_CHECK);
                sendMessage.setText(messageSource.getMessage("msg.search", new Object[]{}, locale));
                yield sendMessage;
            }
            case MY_BIKES -> {
                bikeHandler.getAllBikes(message.getChatId(), userId);
                yield null;
            }
            case ENTER_FRAME_NUMBER_FOR_CHECK -> bikeHandler.findBikeByNumber(message, userId, locale);
            case ENTER_FRAME_NUMBER -> bikeHandler.enterFrameNumber(message, userId, locale);
            case ENTER_VENDOR -> bikeHandler.enterVendorName(message, userId, locale);
            case ENTER_MODEL_NAME -> bikeHandler.enterModelName(message, userId, locale);
            case ENTER_DESCRIPTION -> bikeHandler.enterDescription(message, userId, locale);
            default -> throw new IllegalStateException("Unexpected value: " + botState);
        };
    }
}
