package com.bragin.bike_theft_check.services;

import com.bragin.bike_theft_check.dto.UserDto;
import com.bragin.bike_theft_check.model.TelegramFacade;
import com.bragin.bike_theft_check.model.handlers.CallbackQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserService userService;
    private final MessageSource messageSource;

    public void remoteUser(long userId) {
        if (!userService.existsById(userId)) {
            userService.deleteUserById(userId);
        }
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage, final long userId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        final SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        if (!userService.existsById(userId)) {
            var userDto = new UserDto();
            userDto.setId(userId);
            userService.create(userDto);
        }
        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(TelegramFacade.ADD));
        row1.add(new KeyboardButton(TelegramFacade.CHECK));
        row1.add(new KeyboardButton(TelegramFacade.MY_BIKE));
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public SendMessage getWantedMenu(final long chatId, final String textMessage, Locale locale) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        sendMessage.setReplyMarkup(getInlineMessageWantedMenuButtons(locale));
        return sendMessage;
    }

    private InlineKeyboardMarkup getInlineMessageWantedMenuButtons(Locale locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        buttonYes.setText(messageSource.getMessage("button.yes", new Object[]{}, locale));
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        buttonNo.setText(messageSource.getMessage("button.no", new Object[]{}, locale));
        buttonYes.setCallbackData(CallbackQueryHandler.YES);
        buttonNo.setCallbackData(CallbackQueryHandler.NO);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public List<SendMessage> getUpdateMenu(final long chatId, final List<String> messages, Locale locale) {
        return messages.stream()
                .map(message -> {
                    final SendMessage sendMessage = new SendMessage();
                    sendMessage.enableMarkdown(true);
                    sendMessage.setChatId(String.valueOf(chatId));
                    sendMessage.setText(message);
                    sendMessage.setReplyMarkup(getInlineMessageUpdateBikes(locale));
                    sendMessage.setParseMode("html");
                    return sendMessage;
                })
                .collect(Collectors.toList());
    }

    private InlineKeyboardMarkup getInlineMessageUpdateBikes(Locale locale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonDelete = new InlineKeyboardButton();
        buttonDelete.setText(messageSource.getMessage("button.delete", new Object[]{}, locale));
        buttonDelete.setCallbackData(CallbackQueryHandler.DELETE);

        InlineKeyboardButton buttonStolen = new InlineKeyboardButton();
        buttonStolen.setText(messageSource.getMessage("button.stolen", new Object[]{}, locale));
        buttonStolen.setCallbackData(CallbackQueryHandler.STOLEN);

        InlineKeyboardButton buttonNotStolen = new InlineKeyboardButton();
        buttonNotStolen.setText(messageSource.getMessage("button.notstolen", new Object[]{}, locale));
        buttonNotStolen.setCallbackData(CallbackQueryHandler.NOT_STOLEN);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonDelete);
        keyboardButtonsRow1.add(buttonStolen);
        keyboardButtonsRow1.add(buttonNotStolen);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
