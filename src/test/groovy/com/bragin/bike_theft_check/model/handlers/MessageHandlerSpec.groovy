package com.bragin.bike_theft_check.model.handlers

import com.bragin.bike_theft_check.model.BotState
import com.bragin.bike_theft_check.services.MenuService
import com.bragin.bike_theft_check.services.cash.BikeCash
import com.bragin.bike_theft_check.services.cash.BotStateCash
import org.springframework.context.MessageSource
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import spock.lang.Specification

class MessageHandlerSpec extends Specification {
    def botStateCash = new BotStateCash()
    def menuService = Mock(MenuService)
    def bikeCash = new BikeCash()
    def bikeHandler = Mock(BikeHandler)
    def messageSource = Mock(MessageSource)
    def messageHandler = new MessageHandler(botStateCash, menuService, bikeCash, bikeHandler, messageSource)

    def "Should return message with info block, with BotState equals INFO" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.INFO
            messageSource.getMessage("info", new Object[]{}, locale) >> text
        when:
            def result = messageHandler.handle(message, botState)
        then:
            sendMessage == result
    }

    def "Should return message with main menu" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.START
            messageSource.getMessage("msg.use", new Object[]{}, locale) >> text
            menuService.getMainMenuMessage(chatId, text, userId) >> sendMessage
        when:
            def result = messageHandler.handle(message, botState)
        then:
            sendMessage == result
    }

    def "Should return message with text, BotState equals CREATE" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.CREATE
            messageSource.getMessage("msg.frame", new Object[]{}, locale) >> text
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage
    }

    def "Should return message with text, BotState equals FIND_BIKE" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.FIND_BIKE
            messageSource.getMessage("msg.search", new Object[]{}, locale) >> text
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage
    }

    def "Should return message with text, BotState equals ENTER_FRAME_NUMBER_FOR_SEARCH" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.ENTER_FRAME_NUMBER_FOR_SEARCH
            bikeHandler.findBikeByNumber(message, userId, locale) >> sendMessage
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_FRAME_NUMBER" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.ENTER_FRAME_NUMBER
            bikeHandler.enterFrameNumber(message, userId, locale) >> sendMessage
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_VENDOR" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.ENTER_VENDOR
            bikeHandler.enterVendorName(message, userId, locale) >> sendMessage
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_MODEL_NAME" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.ENTER_MODEL_NAME
            bikeHandler.enterModelName(message, userId, locale) >> sendMessage
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_DESCRIPTION" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.ENTER_DESCRIPTION
            bikeHandler.enterDescription(message, userId, locale) >> sendMessage
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == sendMessage

    }

    def "Should return message with text, BotState equals MY_BIKES" () {
        given:
            def chatId = 1L
            def userId = 1L
            def text = "test"
            def message = createMessage(chatId, userId)
            message.setMessageId(1)
            def sendMessage = createSendMessage(String.valueOf(chatId), text)
            def locale = Locale.ENGLISH
            def botState = BotState.MY_BIKES
            bikeHandler.getAllBikes(message.getChatId(), userId, locale) >> {}
        when:
            def result = messageHandler.handle(message, botState)
        then:
            result == null
    }

    def Message createMessage(long chatId, long userId) {
        def message = new Message()
        def user = new User(userId, "Test", false)
        user.setLanguageCode("en")
        message.setFrom(user)
        message.setChat(new Chat(chatId, "private"))
        message.setText("test")
        return message
    }

    def SendMessage createSendMessage(String chatId, String text) {
        def sendMessage = new SendMessage()
        sendMessage.setChatId(chatId)
        sendMessage.setText(text)
        return sendMessage
    }
}
