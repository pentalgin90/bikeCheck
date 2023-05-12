package com.bragin.bike_theft_check.utils;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.entities.BikeEntity;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class BikeMapper {
    public static BikeDto bikeToDto(BikeEntity bikeEntity) {
        if (Objects.nonNull(bikeEntity)) {
            return new BikeDto(
                    Optional.of(bikeEntity.getFrameNumber()).orElse(null),
                    Optional.of(bikeEntity.getVendor()).orElse(null),
                    Optional.of(bikeEntity.getModelName()).orElse(null),
                    Optional.of(bikeEntity.getDescription()).orElse(null),
                    Optional.of(bikeEntity.getWanted()).orElse(null)
            );
        } else {
            return null;
        }
    }

    public static BikeEntity bikeToEntity (BikeDto bikeDto) {
        if (Objects.nonNull(bikeDto)) {
            var entity = new BikeEntity();
            entity.setFrameNumber(bikeDto.getFrameNumber());
            entity.setVendor(bikeDto.getVendor());
            entity.setModelName(bikeDto.getModelName());
            entity.setDescription(bikeDto.getDescription());
            entity.setWanted(bikeDto.getWanted());
            return entity;
        } else {
            return null;
        }
    }
}
