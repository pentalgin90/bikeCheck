package com.bragin.bike_theft_check.model.states;

import com.bragin.bike_theft_check.model.handlers.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public class EnterFrameNumberForCheckState extends State {

    public EnterFrameNumberForCheckState(MessageHandler messageHandler) {
        super(messageHandler);
    }

    @Override
    public BotApiMethod<?> onClick(Message message, State state) {
        messageHandler.changeState(new EnterFrameNumberForCheckState(messageHandler));
        return messageHandler.findBikeByNumber(message);
    }
}
