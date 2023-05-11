package com.bragin.bike_theft_check.utils;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.entities.BikeEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BikeMapper {
    public static BikeDto bikeToDto(BikeEntity bikeEntity) {
        return new BikeDto(
                bikeEntity.getFrameNumber(),
                bikeEntity.getVendor(),
                bikeEntity.getModelName(),
                bikeEntity.getDescription(),
                bikeEntity.getWanted(),
                bikeEntity.getImage()
                );
    }

    public static BikeEntity bikeToEntity (BikeDto bikeDto) {
        var entity = new BikeEntity();
        entity.setFrameNumber(bikeDto.getFrameNumber());
        entity.setVendor(bikeDto.getVendor());
        entity.setModelName(bikeDto.getModelName());
        entity.setDescription(bikeDto.getDescription());
        entity.setWanted(bikeDto.getWanted());
        entity.setImage(bikeDto.getImage());
        return entity;
    }
}
