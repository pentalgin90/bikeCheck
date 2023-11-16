package com.bragin.bike_theft_check.model;

import com.bragin.bike_theft_check.model.handlers.CallbackQueryHandler;
import com.bragin.bike_theft_check.model.handlers.MessageHandler;
import com.bragin.bike_theft_check.model.states.CheckState;
import com.bragin.bike_theft_check.model.states.CreateState;
import com.bragin.bike_theft_check.model.states.InfoState;
import com.bragin.bike_theft_check.model.states.MyBikesState;
import com.bragin.bike_theft_check.model.states.StartState;
import com.bragin.bike_theft_check.model.states.State;
import com.bragin.bike_theft_check.model.states.StopState;
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

    public static final String INFO = "/info";
    public static final String START = "/start";

    public static final String STOP = "/stop";
    public static final String ADD = "Add";
    public static final String CHECK = "Check";
    public static final String MY_BIKE = "My bike";

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
        String inputMsg = message.getText();
        State state = switch (inputMsg) {
            case INFO -> new InfoState(messageHandler);
            case START -> new StartState(messageHandler);
            case STOP -> new StopState(messageHandler);
            case ADD -> new CreateState(messageHandler);
            case CHECK -> new CheckState(messageHandler);
            case MY_BIKE -> new MyBikesState(messageHandler);
            default -> botStateCash.getBotStateMap().get(message.getFrom().getId()) == null ?
                    new StartState(messageHandler) : botStateCash.getBotStateMap().get(message.getFrom().getId());
        };
        messageHandler.changeState(state);
        return messageHandler.getState().onClick(message, state);
    }

}
