package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.dto.BikeDto;

import java.util.List;

public interface BikeService {
    BikeDto createReport(BikeDto bikeDto) throws Exception;
    BikeDto findBikeByFrameNumber(String frameNumber);
    List<BikeDto> getAllBikesByUserId(long userId);
    BikeDto update(BikeDto bikeDto);
    void deleteBike(String frameNumber);
}
