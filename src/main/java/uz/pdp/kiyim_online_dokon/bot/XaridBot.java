package uz.pdp.kiyim_online_dokon.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.kiyim_online_dokon.bot.controllers.MessageHandler;
import uz.pdp.kiyim_online_dokon.bot.session.UserSession;
import uz.pdp.kiyim_online_dokon.service.interfaces.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class XaridBot extends TelegramLongPollingBot {

    // Service Injection
    private final ProductsService productsService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final OrderService orderService;
    private final UsersService usersService;
    private final TelegramUserService telegramUserService; // <-- To‘g‘ri interface
    private final AddressesService addressesService;

    // Session Map
    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    // Handler
    private MessageHandler messageHandler;

    @Override
    public void onUpdateReceived(Update update) {
        // MessageHandler faqat bir marta initialize qilinadi
        if (messageHandler == null) {
            messageHandler = new MessageHandler(this, sessions, productsService,
                    categoryService, cartService, orderService, telegramUserService); // <-- To‘g‘ri service
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            messageHandler.handleMessage(update.getMessage());
        }

        // Agar inline tugmalar ishlatilsa, update.hasCallbackQuery() logikasini qo‘shish mumkin
    }

    public void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Xarid_24Bot";
    }

    @Override
    public String getBotToken() {
        return "7173310353:AAG1EbynnRf9-CKZoPqopUUV4Rx-t6V7xaE";
    }
}
