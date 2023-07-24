package com.bragin.bike_theft_check.converter;

import com.bragin.bike_theft_check.configuration.MapStructConfig;
import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.entities.BikeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface BikeConverter {
    @Mapping(target = "userId", source = "userEntity.userId")
    BikeDto entityToDto(BikeEntity entity);
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "userEntity", ignore = true)
    BikeEntity dtoToEntity(BikeDto dto);

    List<BikeDto> listToDto (List<BikeEntity> entityList);
    List<BikeEntity> listToEntity(List<BikeDto> dtoList);
}
