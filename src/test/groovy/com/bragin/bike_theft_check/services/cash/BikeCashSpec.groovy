package com.bragin.bike_theft_check.services.cash

import com.bragin.bike_theft_check.dto.BikeDto;
import spock.lang.Specification;

class BikeCashSpec extends Specification {
    def bikeCash = new BikeCash()

    def "Should saved bikeDto and user id" () {
        given:
            def bike = new BikeDto()
            bike.setFrameNumber("123")
            def userId = 1L
        when:
            bikeCash.saveBikeCash(userId, bike)
        then:
            def result = bikeCash.bikeMap.get(userId)
            bike.equals(result)
    }
}