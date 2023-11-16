package com.bragin.bike_theft_check.services.cash


import com.bragin.bike_theft_check.model.handlers.MessageHandler
import com.bragin.bike_theft_check.model.states.InfoState
import spock.lang.Specification

class BotStateCashSpec extends Specification {
    def botStateCash = new BotStateCash()

    def "Should saved state with user id"() {
        given:
            final def userId = 1L
            final def state = new InfoState(new MessageHandler(null, null, null, null, null))
        when:
            botStateCash.saveBotState(userId, state)
        then:
            final def result = botStateCash.botStateMap.get(userId)
            state == result
    }
}