package com.bragin.bike_theft_check.model.handlers;

import com.bragin.bike_theft_check.dto.BikeDto;
import com.bragin.bike_theft_check.model.BotState;
import com.bragin.bike_theft_check.model.Status;
import com.bragin.bike_theft_check.services.BikeService;
import com.bragin.bike_theft_check.services.MenuService;
import com.bragin.bike_theft_check.services.cash.BikeCash;
import com.bragin.bike_theft_check.services.cash.BotStateCash;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Locale;
import java.util.Objects;

import static com.bragin.bike_theft_check.utils.CreateLocale.getLocale;

@Service
@RequiredArgsConstructor
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final BikeCash bikeCash;
    private final BikeService bikeService;
    private final MenuService menuService;
    private final MessageSource messageSource;

    public static final String YES = "buttonYes";
    public static final String NO = "buttonNo";
    public static final String DELETE = "buttonDelete";
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();
        final Locale locale = getLocale(callbackQuery.getFrom().getLanguageCode());
        String data = callbackQuery.getData();
        return switch (data) {
            case YES -> {
                yield savedChange(chatId, userId, locale, true);
            }
            case NO -> {
                yield savedChange(chatId, userId, locale, false);
            }
            case DELETE -> {
                String message = messageSource.getMessage("msg.deleted", new Object[]{}, locale);
                String frameNumber = getFrameNumber(callbackQuery.getMessage().getText());
                bikeService.deleteBike(frameNumber);
                yield menuService.getMainMenuMessage(chatId, message, userId);
            }
            default -> throw new IllegalStateException("Unexpected value: " + botStateCash.getBotStateMap().get(userId));
        };
    }

    public String getFrameNumber(String text) {
        String[] split = text.split("\\s");
        return split[2];
    }

    private SendMessage savedChange(long chatId, long userId, Locale locale, boolean yes) {
        String message = messageSource.getMessage("msg.saved", new Object[]{}, locale);
        BikeDto bikeDto = bikeCash.getBikeMap().get(userId);
        bikeDto.setUserId(userId);
        if (yes) {
            bikeDto.setStatus(Status.Stolen);
        } else {
            bikeDto.setStatus(Status.Not_stolen);
        }
        if (Objects.isNull(bikeDto.getId())) {
            try {
                bikeService.createReport(bikeDto);
                bikeCash.saveBikeCash(userId, new BikeDto());
                botStateCash.saveBotState(userId, BotState.START);
                return menuService.getMainMenuMessage(chatId, message, userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            bikeService.update(bikeDto);
            bikeCash.saveBikeCash(userId, new BikeDto());
            botStateCash.saveBotState(userId, BotState.START);
            return menuService.getMainMenuMessage(chatId, message, userId);
        }
    }
}
