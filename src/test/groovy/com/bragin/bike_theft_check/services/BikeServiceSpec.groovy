package com.bragin.bike_theft_check.services

import com.bragin.bike_theft_check.converter.BikeConverterImpl
import com.bragin.bike_theft_check.dto.BikeDto
import com.bragin.bike_theft_check.entities.BikeEntity
import com.bragin.bike_theft_check.entities.UserEntity
import com.bragin.bike_theft_check.model.states.Status
import com.bragin.bike_theft_check.repositories.BikeRepo
import com.bragin.bike_theft_check.repositories.UserRepo
import spock.lang.Specification

import java.time.LocalDateTime

class BikeServiceSpec extends Specification{
    def userRepo = Mock(UserRepo)
    def bikeRepo = Mock(BikeRepo)
    def converter = new BikeConverterImpl();
    def bikeService = new BikeServiceImpl(bikeRepo, userRepo, converter)

    def "Should return bike by frame number" () {
        given:
            def frameNumber = "123"
            def bikeEntity = new BikeEntity(
                    UUID.randomUUID(),
                    "123",
                    "Marin",
                    "FS",
                    LocalDateTime.now(),
                    "test",
                    Status.Stolen,
                    "http://test",
                    null
            )
            def user = new UserEntity();
            user.getBikeEntityList().add(bikeEntity)
            bikeEntity.setUserEntity(user)
            def bike = converter.entityToDto(bikeEntity)
            bikeRepo.findByFrameNumber(frameNumber) >> bikeEntity
        when:
            def result = bikeService.findBikeByFrameNumber(frameNumber)
        then:
            result.equals(bike)
    }

    def "Should return exception if frameName equals blank" () {
        when:
            def result = bikeService.findBikeByFrameNumber(null)
        then:
            thrown(Exception)
    }

    def "Should return list bikes by user_id" () {
        given:
            def id = 1L
            def bikeEntity = new BikeEntity(
                    UUID.randomUUID(),
                    "123",
                    "Marin",
                    "FS",
                    LocalDateTime.now(),
                    "test",
                    Status.Stolen,
                    "http://test",
                    null
            )
            def user = new UserEntity()
            user.setUserId(id)
            user.getBikeEntityList().add(bikeEntity)
            bikeEntity.setUserEntity(user)
            def listBikes = [bikeEntity]
            bikeRepo.findByUserEntity_UserId(id) >> listBikes
        when:
            def result = bikeService.getAllBikesByUserId(id)
        then:
            listBikes.size() == result.size()
    }

    def "Should save and return new bike" () {
        given:
            def userId = 1L
            def bike = new BikeDto(
                    UUID.randomUUID(),
                    "123",
                    "Marin",
                    "FS",
                    "test",
                    Status.Stolen,
                    "http://test",
                    1L
            )
            def bikeEntity = converter.dtoToEntity(bike)
            def user = new UserEntity()
            user.setUserId(userId)
            user.getBikeEntityList().add(bikeEntity)
            bikeEntity.setUserEntity(user)
            userRepo.findById(userId) >> Optional.of(user)
            bikeRepo.save(_) >> bikeEntity
            bikeRepo.existsByFrameNumber(_) >> false
        when:
            def result = bikeService.createReport(bike)
        then:
            result.getFrameNumber().equals(bike.frameNumber)
    }

    def "Should not save and return exception" () {
        given:
            def userId = 1L
            def bike = new BikeDto(
                    UUID.randomUUID(),
                    "123",
                    "Marin",
                    "FS",
                    "test",
                    Status.Stolen,
                    "http://test",
                    1L
            )
            def bikeEntity = converter.dtoToEntity(bike)
            def user = new UserEntity()
            user.setUserId(userId)
            user.getBikeEntityList().add(bikeEntity)
            bikeEntity.setUserEntity(user)
            userRepo.findById(userId) >> Optional.of(user)
            bikeRepo.save(_) >> bikeEntity
            bikeRepo.existsByFrameNumber(_) >> true
        when:
            def result = bikeService.createReport(bike)
        then:
            thrown(Exception)
    }

    def "Should return exception" () {
        given:
            def userId = 1L
            def bike = new BikeDto(
                    UUID.randomUUID(),
                    "123",
                    "Marin",
                    "FS",
                    "test",
                    Status.Stolen,
                    "http://test",
                    1L
            )
            def bikeEntity = converter.dtoToEntity(bike)
            def user = new UserEntity()
            user.setUserId(userId)
            user.getBikeEntityList().add(bikeEntity)
            bikeEntity.setUserEntity(user)
            userRepo.findById(userId) >> Optional.empty()
        when:
            def result = bikeService.createReport(bike)
        then:
            thrown(Exception)
    }
}
