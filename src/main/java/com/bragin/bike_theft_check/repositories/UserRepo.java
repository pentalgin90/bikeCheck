package com.bragin.bike_theft_check.repositories;

import com.bragin.bike_theft_check.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
}
