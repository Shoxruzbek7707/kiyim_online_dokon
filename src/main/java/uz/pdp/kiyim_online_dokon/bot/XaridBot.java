package uz.pdp.kiyim_online_dokon.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.kiyim_online_dokon.bot.controllers.MessageHandler; // Nomi CallbackQueryHandler dan o'zgartirildi
import uz.pdp.kiyim_online_dokon.bot.session.UserSession;
import uz.pdp.kiyim_online_dokon.service.interfaces.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class XaridBot extends TelegramLongPollingBot {

    // Service Injection - Bu qism Spring tomonidan avtomatik kiritiladi
    private final ProductsService productsService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final OrderService orderService;
    private final UsersService usersService;
    private final AddressesService addressesService;

    // Session Map
    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    // Handlerlar
    private MessageHandler messageHandler;

    @Override
    public void onUpdateReceived(Update update) {
        // Initialization (Faqat bitta MessageHandler kerak, chunki u Command va Textni boshqaradi)
        if (messageHandler == null) {
            messageHandler = new MessageHandler(this, sessions, productsService,
                    categoryService, cartService, orderService, usersService);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            // Text va Command xabarlarini MessageHandlerga yuboramiz
            messageHandler.handleMessage(update.getMessage());
        }

        // TODO: Agar Inline tugmalar ishlatilsa, update.hasCallbackQuery() logikasini qo'shing
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

    // token va username'ni config dan olish yaxshiroq, lekin hozircha shu holatda qoldiramiz
    @Override
    public String getBotUsername() {
        return "Xarid_24Bot";
    }

    @Override
    public String getBotToken() {
        return "7173310353:AAG1EbynnRf9-CKZoPqopUUV4Rx-t6V7xaE";
    }
}