package com.bragin.bike_theft_check.services

import com.bragin.bike_theft_check.dto.UserDto
import org.springframework.context.MessageSource
import spock.lang.Specification

class MenuServiceSpec extends Specification{
    def userService = Mock(UserServiceImpl)
    def messageSource = Mock(MessageSource)
    def menuService = new MenuService(userService, messageSource)
    def locale = Locale.ENGLISH
    def chatId = 1L
    def textMessage = "Test"

    def "Should return SendMessage object with keyBoard user not exists" () {
        given:
            def userId = 1L
            def user = new UserDto()
            user.setId(userId)
            userService.existsById(userId) >> false
            userService.create(user) >> user
        when:
            def result = menuService.getMainMenuMessage(chatId, textMessage, userId)
        then:
            Objects.nonNull(result)
    }

    def "Should return SendMessage object with keyBoard user already exists" () {
        given:
            def userId = 1L
            def user = new UserDto()
            user.setId(userId)
            userService.existsById(userId) >> true
            userService.create(user) >> user
        when:
            def result = menuService.getMainMenuMessage(chatId, textMessage, userId)
        then:
            Objects.nonNull(result)
    }

    def "Should return SendMessage with inline keyBoard" () {
        given:
            messageSource.getMessage("button.yes", new Object[]{}, locale) >> "yes"
            messageSource.getMessage("button.no", new Object[]{}, locale) >> "no"
        when:
            def result = menuService.getWantedMenu(chatId, textMessage, locale)
        then:
            Objects.nonNull(result)
    }

    def "Should return list SendMessage with update keyBoard" () {
        given:
            def listMessage = [textMessage]
            messageSource.getMessage("button.delete", new Object[]{}, locale) >> "Delete"
            messageSource.getMessage("button.stolen", new Object[]{}, locale) >> "Stolen"
        messageSource.getMessage("button.notstolen", new Object[]{}, locale) >> "Not Stolen"
        when:
            def result = menuService.getUpdateMenu(chatId, listMessage, locale)
        then:
            Objects.nonNull(result)
    }
}
