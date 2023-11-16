package com.bragin.bike_theft_check.services.cash

import com.bragin.bike_theft_check.dto.BikeDto;
import spock.lang.Specification;

class BikeCashSpec extends Specification {
    def bikeCash = new BikeCash()

    def "Should saved bikeDto and user id" () {
        given:
            final def bike = new BikeDto()
            bike.setFrameNumber("123")
            final def userId = 1L
        when:
            bikeCash.saveBikeCash(userId, bike)
        then:
            final def result = bikeCash.bikeMap.get(userId)
            bike == result
    }
}