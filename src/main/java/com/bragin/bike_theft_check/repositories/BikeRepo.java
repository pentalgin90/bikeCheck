package com.bragin.bike_theft_check.repositories;

import com.bragin.bike_theft_check.entities.BikeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BikeRepo extends CrudRepository<BikeEntity, UUID> {
    BikeEntity findByFrameNumber(String frameNumber);
    List<BikeEntity> findByUserEntity_UserId(long id);
    Boolean existsByFrameNumber (String frameNumber);
}
