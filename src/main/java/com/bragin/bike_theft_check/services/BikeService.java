package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.states.Status;

import java.util.List;

public interface BikeService {
    BikeDto createReport(BikeDto bikeDto) throws Exception;
    BikeDto findBikeByFrameNumber(String frameNumber) throws Exception;
    List<BikeDto> getAllBikesByUserId(long userId);
    boolean ifExist(String frameNumber);
    BikeDto update(BikeDto bikeDto);
    void updateStatus(String frameNumber, Status status);
    void deleteBike(String frameNumber);
}
