package com.bragin.bike_theft_check.services

import com.bragin.bike_theft_check.converter.BikeConverterImpl
import com.bragin.bike_theft_check.converter.UserConverterImpl
import com.bragin.bike_theft_check.dto.BikeDto
import com.bragin.bike_theft_check.dto.UserDto
import com.bragin.bike_theft_check.repositories.UserRepo
import spock.lang.Specification

class UserServiceSpec extends Specification{
    def userRepo = Mock(UserRepo)
    def converter = new UserConverterImpl(new BikeConverterImpl())
    def userService = new UserServiceImpl(userRepo, converter)

    def "Should return true if exist user"() {
        given:
            def id = 1L
        when:
            def result = userRepo.existsById(_) > true
        then:
            result == userService.existsById(id)
    }

    def "Should return DtoUser"() {
        given:
            def user = new UserDto(1L, new ArrayList<BikeDto>())
            def userEntity = converter.dtoToEntity(user)
            userRepo.save(userEntity) >> userEntity
        when:
            def result = userService.create(user)
        then:
            user.equals(result)
    }

    def "Should return UserDto by user_id equals 1l"() {
        given:
            def id = 1L
            def user = new UserDto(id, new ArrayList<BikeDto>())
            def userEntity = converter.dtoToEntity(user)
            userRepo.findById(id) >> Optional.of(userEntity)
        when:
            def result = userService.getById(id)
        then:
            result.equals(user)
    }

    def "Should return null by user_id equals 2l"() {
        given:
            def id = 2L
            def user = new UserDto(id, new ArrayList<BikeDto>())
            def userEntity = converter.dtoToEntity(user)
            userRepo.findById(id) >> Optional.empty()
        when:
            def result = userService.getById(id)
        then:
            Objects.isNull(result)
    }
}
