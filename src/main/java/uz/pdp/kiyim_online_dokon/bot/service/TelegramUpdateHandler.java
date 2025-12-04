
package uz.pdp.kiyim_online_dokon.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.kiyim_online_dokon.bot.command.BotCommands;
import uz.pdp.kiyim_online_dokon.bot.handlers.CallbackHandler;
import uz.pdp.kiyim_online_dokon.bot.handlers.CommandHandler;
import uz.pdp.kiyim_online_dokon.bot.util.MessageBuilder;

@Component
@RequiredArgsConstructor
public class TelegramUpdateHandler {

    private final CommandHandler commandHandler;
    private final CallbackHandler callbackHandler;

    public BotApiMethod<?> handle(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            return handleText(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            return handleCallback(update.getCallbackQuery());
        }

        return null;
    }

    private BotApiMethod<?> handleText(Message message) {
        String chatId = message.getChatId().toString();
        String text = message.getText().trim();

        if (text.startsWith("/")) {
            String command = text.split(" ")[0];
            return commandHandler.handle(chatId, command, text);
        }

        // Agar oddiy text bo'lsa, masalan, login uchun (lekin hozir /login bilan)
        return MessageBuilder.text(chatId, "❗ Buyruqni / bilan boshlang.");
    }

    private BotApiMethod<?> handleCallback(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData();
        return callbackHandler.handle(chatId, data);
    }
}