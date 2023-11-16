package com.bragin.bike_theft_check.model.states;

import com.bragin.bike_theft_check.model.handlers.MessageHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MyBikesState extends State{

    public MyBikesState(MessageHandler messageHandler){
        super(messageHandler);
    }

    @Override
    public BotApiMethod<?> onClick(Message message, State state) {
        messageHandler.changeState(new MyBikesState(messageHandler));
        return messageHandler.returnBikes(message, state);
    }
}
