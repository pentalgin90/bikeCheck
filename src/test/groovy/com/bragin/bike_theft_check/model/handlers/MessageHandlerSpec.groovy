package com.bragin.bike_theft_check.model.handlers


import com.bragin.bike_theft_check.model.states.EnterDescription
import com.bragin.bike_theft_check.model.states.InfoState
import com.bragin.bike_theft_check.model.states.MyBikesState
import com.bragin.bike_theft_check.model.states.StartState
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

    def "Should return message with info block, with BotState equals INFO"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        final def state = new InfoState(messageHandler);
        messageSource.getMessage("info", new Object[]{}, locale) >> text
        when:
        final def result = messageHandler.sendInfo(message, state)
        then:
        sendMessage == result
    }

    def "Should return message with main menu"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        final def state = new StartState(messageHandler);
        messageSource.getMessage("msg.use", new Object[]{}, locale) >> text
        menuService.getMainMenuMessage(chatId, text, userId) >> sendMessage
        when:
        final def result = messageHandler.sendMenu(message, state)
        then:
        sendMessage == result
    }

    def "Should return message with text, BotState equals CREATE"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        messageSource.getMessage("msg.frame", new Object[]{}, locale) >> text
        when:
        final def result = messageHandler.createBike(message)
        then:
        result == sendMessage
    }

    def "Should return message with text, BotState equals FIND_BIKE"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        messageSource.getMessage("msg.search", new Object[]{}, locale) >> text
        when:
        final def result = messageHandler.checkBike(message)
        then:
        result == sendMessage
    }

    def "Should return message with text, BotState equals ENTER_FRAME_NUMBER_FOR_SEARCH"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        bikeHandler.findBikeByNumber(message, userId, locale) >> sendMessage
        when:
        final def result = messageHandler.findBikeByNumber(message)
        then:
        result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_FRAME_NUMBER"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        bikeHandler.enterFrameNumber(message, userId, locale) >> sendMessage
        when:
        final def result = messageHandler.enterFrame(message)
        then:
        result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_VENDOR"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        bikeHandler.enterVendorName(message, userId, locale) >> sendMessage
        when:
        final def result = messageHandler.enterVendor(message)
        then:
        result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_MODEL_NAME"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        bikeHandler.enterModelName(message, userId, locale) >> sendMessage
        when:
        final def result = messageHandler.enterModelName(message)
        then:
        result == sendMessage

    }

    def "Should return message with text, BotState equals ENTER_DESCRIPTION"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def text = "test"
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def sendMessage = createSendMessage(String.valueOf(chatId), text)
        final def locale = Locale.ENGLISH
        final def state = new EnterDescription(messageHandler)
        bikeHandler.enterDescription(message, userId, locale) >> sendMessage
        when:
        final def result = messageHandler.enterDescription(message, state)
        then:
        result == sendMessage

    }

    def "Should return message with text, BotState equals MY_BIKES"() {
        given:
        final def chatId = 1L
        final def userId = 1L
        final def message = createMessage(chatId, userId)
        message.setMessageId(1)
        final def locale = Locale.ENGLISH
        final def state = new MyBikesState(messageHandler)
        bikeHandler.getAllBikes(message.getChatId(), userId, locale) >> {}
        when:
        final def result = messageHandler.returnBikes(message, state)
        then:
        result == null
    }

    def Message createMessage(final long chatId, final long userId) {
        final def message = new Message()
        final def user = new User(userId, "Test", false)
        user.setLanguageCode("en")
        message.setFrom(user)
        message.setChat(new Chat(chatId, "private"))
        message.setText("test")
        return message
    }

    def SendMessage createSendMessage(final String chatId, final String text) {
        final def sendMessage = new SendMessage()
        sendMessage.setChatId(chatId)
        sendMessage.setText(text)
        return sendMessage
    }
}
