package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.states.EnterDescription;
import com.bragin.bike_theft_check.model.states.EnterFrameNumberForCheckState;
import com.bragin.bike_theft_check.model.states.EnterFrameNumberState;
import com.bragin.bike_theft_check.model.states.EnterModelNameState;
import com.bragin.bike_theft_check.model.states.EnterVendorState;
import com.bragin.bike_theft_check.model.states.StartState;
import com.bragin.bike_theft_check.model.states.State;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.Getter;
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
    @Getter
    private State state;

    public void changeState(State state) {
        this.state = state;
    }

    public BotApiMethod<?> sendInfo(Message message, State state) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(messageSource.getMessage("info", new Object[]{}, locale));
        botStateCash.saveBotState(userId, state);
        return sendMessage;
    }

    public BotApiMethod<?> sendMenu(Message message, State state) {
        long userId = message.getFrom().getId();
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        String text = messageSource.getMessage("msg.use", new Object[]{}, locale);
        botStateCash.saveBotState(userId, state);
        return menuService.getMainMenuMessage(message.getChatId(), text, userId);
    }

    public BotApiMethod<?> stopBot(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        botStateCash.deleteBotState(userId);
        bikeCash.deleteBikeCash(userId);
        menuService.remoteUser(userId);

        String text = messageSource.getMessage("msg.stop", new Object[]{}, locale);
        sendMessage.setText(text);
        return sendMessage;
    }

    public BotApiMethod<?> createBike(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(messageSource.getMessage("msg.frame", new Object[]{}, locale));
        botStateCash.saveBotState(userId, new EnterFrameNumberState(this));
        bikeCash.saveBikeCash(userId, new BikeDto());
        return sendMessage;
    }

    public BotApiMethod<?> checkBike(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(messageSource.getMessage("msg.search", new Object[]{}, locale));
        botStateCash.saveBotState(userId, new EnterFrameNumberForCheckState(this));
        return sendMessage;
    }

    public BotApiMethod<?> returnBikes(Message message, State state) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        botStateCash.saveBotState(userId, state);
        bikeHandler.getAllBikes(message.getChatId(), userId, locale);
        return null;
    }

    public BotApiMethod<?> findBikeByNumber(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        botStateCash.saveBotState(userId, new StartState(this));
        return bikeHandler.findBikeByNumber(message, userId, locale);
    }

    public BotApiMethod<?> enterFrame(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        botStateCash.saveBotState(userId, new EnterVendorState(this));
        return bikeHandler.enterFrameNumber(message, userId, locale);
    }

    public BotApiMethod<?> enterModelName(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        botStateCash.saveBotState(userId, new EnterDescription(this));
        return bikeHandler.enterModelName(message, userId, locale);
    }

    public BotApiMethod<?> enterVendor(Message message) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        botStateCash.saveBotState(userId, new EnterModelNameState(this));
        return bikeHandler.enterVendorName(message, userId, locale);
    }

    public BotApiMethod<?> enterDescription(Message message, State state) {
        Locale locale = getLocale(message.getFrom().getLanguageCode());
        long userId = message.getFrom().getId();
        botStateCash.saveBotState(userId, state);
        return bikeHandler.enterDescription(message, userId, locale);
    }


}
