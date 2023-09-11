package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.converter.UserConverter;
import com.bragin.bike_theft_check.dto.UserDto;
import com.bragin.bike_theft_check.entities.UserEntity;
import com.bragin.bike_theft_check.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repo;
    private final UserConverter converter;

    @Override
    public UserDto getById(long id) {
        var userById = repo.findById(id);
        return userById.map(converter::entityToDto).orElse(null);
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

    @Override
    public void deleteUserById(long id) {
        Optional<UserEntity> byId = repo.findById(id);
        byId.ifPresentOrElse(userEntity -> {
                    repo.delete(userEntity);
                },
                () -> {
                    throw new RuntimeException("User did not find");
                }
        );
    }
}
