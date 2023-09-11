package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(long id);
    UserDto create(UserDto userDto);

    boolean existsById(long id);

    void deleteUserById(long id);
}
