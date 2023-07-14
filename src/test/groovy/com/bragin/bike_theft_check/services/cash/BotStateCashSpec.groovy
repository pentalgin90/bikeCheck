package com.bragin.bike_theft_check.services.cash

import com.bragin.bike_theft_check.model.BotState
import spock.lang.Specification;

class BotStateCashSpec extends Specification{
    def botStateCash = new BotStateCash()

    def "Sould saved state with user id" () {
        given:
            def userId = 1L
            def state = BotState.INFO
        when:
            botStateCash.saveBotState(userId, state)
        then:
            def result = botStateCash.botStateMap.get(userId)
            state == result
    }
}