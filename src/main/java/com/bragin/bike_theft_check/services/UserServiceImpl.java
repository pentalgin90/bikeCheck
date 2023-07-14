package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.converter.UserConverter;
import com.bragin.bike_theft_check.dto.UserDto;
import com.bragin.bike_theft_check.entities.UserEntity;
import com.bragin.bike_theft_check.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final UserConverter converter;
    @Override
    public UserDto getById(long id) {
        var userById = repo.findById(id);
        if (userById.isPresent()) {
            return converter.entityToDto(userById.get());
        } else {
            return null;
        }
    }

    @Override
    public UserDto create(UserDto userDto) {
        UserEntity userEntity = converter.dtoToEntity(userDto);
        UserEntity save = repo.save(userEntity);
        return converter.entityToDto(save);
    }

    @Override
    public boolean existsById(long id) {
        return repo.existsById(id);
    }
}
