package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.dto.BikeDto;

public interface BikeService {
    BikeDto createReport(BikeDto bikeDto);
    BikeDto findBikeByFrameNumber(String frameNumber);
}
