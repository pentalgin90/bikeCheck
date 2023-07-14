package com.bragin.bike_theft_check.converter;

import com.bragin.bike_theft_check.configuration.MapStructConfig;
import com.bragin.bike_theft_check.dto.UserDto;
import com.bragin.bike_theft_check.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = BikeConverter.class)
public interface UserConverter {
    @Mapping(target = "id", source = "userId")
    @Mapping(target = "bikeList", source = "bikeEntityList")
    UserDto entityToDto(UserEntity entity);
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "bikeEntityList", source = "bikeList")
    UserEntity dtoToEntity(UserDto dto);
}
