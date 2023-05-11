package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.entities.BikeEntity;
import com.bragin.bike_theft_check.repositories.BikeRepo;
import com.bragin.bike_theft_check.utils.BikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BikeServiceImpl implements BikeService{

    private final BikeRepo repo;
    @Override
    public BikeDto createReport(BikeDto bikeDto) {
        BikeEntity bikeEntity = BikeMapper.bikeToEntity(bikeDto);
        bikeEntity.setId(UUID.randomUUID());
        bikeEntity.setDateCreated(LocalDateTime.now());
        BikeEntity saved = repo.save(bikeEntity);
        return BikeMapper.bikeToDto(saved);
    }

    @Override
    public BikeDto findBikeByFrameNumber(String frameNumber) {
        BikeEntity entity = repo.findByFrameNumber(frameNumber);
        return BikeMapper.bikeToDto(entity);
    }
}
