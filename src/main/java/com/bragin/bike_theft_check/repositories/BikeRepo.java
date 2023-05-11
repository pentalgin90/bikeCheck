package com.bragin.bike_theft_check.repositories;

import com.bragin.bike_theft_check.entities.BikeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BikeRepo extends CrudRepository<BikeEntity, UUID> {
    BikeEntity findByFrameNumber(String frameNumber);
}
