package com.bragin.bike_theft_check.services.cash;

import com.bragin.bike_theft_check.model.states.State;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@Getter
@Setter
public class BotStateCash {
    private final Map<Long, State> botStateMap = new HashMap<>();

    public void saveBotState(long userId, State botState) {
        botStateMap.put(userId, botState);
    }
    public void deleteBotState(long userId) {
        botStateMap.remove(userId);
    }
}
