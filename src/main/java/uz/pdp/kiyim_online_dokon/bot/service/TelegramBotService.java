
package uz.pdp.kiyim_online_dokon.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Component
public class TelegramBotService extends TelegramLongPollingBot {

    private final TelegramUpdateHandler updateHandler;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public TelegramBotService(TelegramUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            BotApiMethod<? extends Serializable> response = updateHandler.handle(update);
            if (response != null) {
                execute(response);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace(); // Productionda logger ishlat
        }
    }
}