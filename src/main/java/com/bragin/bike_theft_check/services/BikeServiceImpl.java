package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.converter.BikeConverter;
import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.entities.BikeEntity;
import com.bragin.bike_theft_check.entities.UserEntity;
import com.bragin.bike_theft_check.repositories.BikeRepo;
import com.bragin.bike_theft_check.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BikeServiceImpl implements BikeService{

    private final BikeRepo bikeRepo;
    private final UserRepo userRepo;
    private final BikeConverter converter;
    @Override
    public BikeDto createReport(BikeDto bikeDto) throws Exception {
        BikeEntity bikeEntity = converter.dtoToEntity(bikeDto);
        UserEntity userEntity = userRepo.findById(bikeDto.getUserId()).orElse(null);
        if (Objects.nonNull(userEntity)) {
            bikeEntity.setUserEntity(userEntity);
            bikeEntity.setId(UUID.randomUUID());
            bikeEntity.setDateCreated(LocalDateTime.now());
            BikeEntity saved = bikeRepo.save(bikeEntity);
            return converter.entityToDto(saved);
        } else {
            throw new Exception("Can't save bike");
        }
    }

    @Override
    public BikeDto findBikeByFrameNumber(String frameNumber) {
        if (Strings.isNotBlank(frameNumber)) {
            BikeEntity entity = bikeRepo.findByFrameNumber(frameNumber);
            return converter.entityToDto(entity);
        } else {
            return null;
        }
    }

    @Override
    public List<BikeDto> getAllBikesByUserId(long userId) {
        List<BikeEntity> byUserEntity = bikeRepo.findByUserEntity_UserId(userId);
        List<BikeDto> bikeDtos = byUserEntity.stream()
                .map(entity -> converter.entityToDto(entity))
                .collect(Collectors.toList());
        return bikeDtos;
    }

    @Override
    public BikeDto update(BikeDto bikeDto) {
        BikeEntity bikeEntity = converter.dtoToEntity(bikeDto);
        BikeEntity bikeFromDB = bikeRepo.findById(bikeEntity.getId()).orElse(null);
        bikeFromDB.setFrameNumber(bikeEntity.getFrameNumber());
        bikeFromDB.setVendor(bikeEntity.getVendor());
        bikeFromDB.setModelName(bikeEntity.getModelName());
        bikeFromDB.setDescription(bikeEntity.getDescription());
        bikeFromDB.setWanted(bikeEntity.getWanted());
        bikeFromDB.setDateUpdate(LocalDateTime.now());
        BikeEntity newBike = bikeRepo.save(bikeFromDB);
        return converter.entityToDto(newBike);
    }

    @Override
    public void deleteBike(String frameNumber) {
        BikeEntity entity = bikeRepo.findByFrameNumber(frameNumber);
        bikeRepo.deleteById(entity.getId());
    }
}
