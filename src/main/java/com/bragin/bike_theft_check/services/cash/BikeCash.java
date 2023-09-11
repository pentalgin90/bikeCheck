package com.bragin.bike_theft_check.services.cash;

import com.bragin.bike_theft_check.dto.BikeDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@Setter
public class BikeCash {
    private final Map<Long, BikeDto> bikeMap = new HashMap<>();

    public void saveBikeCash(long userId, BikeDto bike) {
        bikeMap.put(userId, bike);
    }
    public void deleteBikeCash(long userId) {
        bikeMap.remove(userId);
    }
}
