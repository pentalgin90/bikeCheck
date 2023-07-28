package com.bragin.bike_theft_check.services.schedulers;

import com.bragin.bike_theft_check.dto.BikeDto;

import java.util.List;

public interface Parser {
    List<BikeDto> parse();
}
