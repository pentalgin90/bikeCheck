package com.bragin.bike_theft_check.model;

import com.bragin.bike_theft_check.model.handlers.CallbackQueryHandler;
import com.bragin.bike_theft_check.model.handlers.MessageHandler;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TelegramFacade {

    final BotStateCash botStateCash;
    final CallbackQueryHandler callbackQueryHandler;
    final MessageHandler messageHandler;

    public BotApiMethod<?> handleUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {

            Message message = update.getMessage();
            if ((message != null && message.hasText()) || (message != null && !message.getPhoto().isEmpty())) {
                return handleInputMessage(message);
            }
        }
        return null;
    }

    private BotApiMethod<?> handleInputMessage(Message message) {
        BotState botState;
        String inputMsg = message.getText();
        botState = switch (inputMsg) {
            case "/info" -> BotState.INFO;
            case "/start" -> BotState.START;
            case "Add" -> BotState.CREATE;
            case "Find" -> BotState.FIND_BIKE;
            case "My bikes" -> BotState.MY_BIKES;
            default -> botStateCash.getBotStateMap().get(message.getFrom().getId()) == null ?
                    BotState.START : botStateCash.getBotStateMap().get(message.getFrom().getId());
        };
        //we pass the corresponding state to the handler
        //the corresponding method will be called
        return messageHandler.handle(message, botState);

    }

}
