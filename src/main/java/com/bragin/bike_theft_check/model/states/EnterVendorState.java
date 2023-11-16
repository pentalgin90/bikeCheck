package com.bragin.bike_theft_check.model.states;

import com.bragin.bike_theft_check.model.handlers.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public class EnterVendorState extends State {

    public EnterVendorState(MessageHandler messageHandler) {
        super(messageHandler);
    }

    @Override
    public BotApiMethod<?> onClick(Message message, State state) {
        messageHandler.changeState(new EnterVendorState(messageHandler));
        return messageHandler.enterVendor(message);
    }
}
