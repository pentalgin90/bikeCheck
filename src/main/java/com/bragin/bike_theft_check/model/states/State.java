package com.bragin.bike_theft_check.model.states;

import com.bragin.bike_theft_check.model.handlers.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class State {
    MessageHandler messageHandler;

    public State(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public abstract BotApiMethod<?> onClick(Message message, State state);
}
