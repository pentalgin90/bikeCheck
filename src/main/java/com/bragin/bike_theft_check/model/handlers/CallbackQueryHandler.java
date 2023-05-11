package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.BotState;
import com.bragin.bike_theft_check.services.BikeService;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@RequiredArgsConstructor
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final BikeCash bikeCash;
    private final BikeService bikeService;
    private final MenuService menuService;
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        return switch (data) {
            case "buttonYes" -> {
                BikeDto bikeDto = bikeCash.getBikeMap().get(userId);
                bikeDto.setWanted(true);
                bikeService.createReport(bikeDto);
                bikeCash.saveBikeCash(userId, new BikeDto());
                botStateCash.saveBotState(userId, BotState.START);
                yield menuService.getMainMenuMessage(chatId, "Report was saved", userId);
            }
            case "buttonNo" -> {
                BikeDto bikeDto = bikeCash.getBikeMap().get(userId);
                bikeDto.setWanted(false);
                bikeService.createReport(bikeDto);
                bikeCash.saveBikeCash(userId, new BikeDto());
                botStateCash.saveBotState(userId, BotState.START);
                yield menuService.getMainMenuMessage(chatId, "Report was saved", userId);
            }
            default -> throw new IllegalStateException("Unexpected value: " + botStateCash.getBotStateMap().get(userId));
        };
    }
}
