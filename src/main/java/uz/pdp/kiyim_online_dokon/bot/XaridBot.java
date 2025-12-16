package uz.pdp.kiyim_online_dokon.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.kiyim_online_dokon.bot.controllers.CallbackQueryHandler;
import uz.pdp.kiyim_online_dokon.bot.controllers.MessageHandler;
import uz.pdp.kiyim_online_dokon.bot.session.UserSession;
import uz.pdp.kiyim_online_dokon.service.interfaces.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class XaridBot extends TelegramLongPollingBot {

    private final ProductsService productsService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final TelegramUserService telegramUserService;

    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    private MessageHandler messageHandler;
    private CallbackQueryHandler callbackQueryHandler;

    @Override
    public void onUpdateReceived(Update update) {
        if (messageHandler == null) {
            messageHandler = new MessageHandler(
                    this,
                    sessions,
                    productsService,
                    categoryService,
                    orderService,
                    telegramUserService
            );
        }

        if (callbackQueryHandler == null) {
            callbackQueryHandler = new CallbackQueryHandler(
                    this,
                    sessions,
                    productsService
            );
        }

        // LOKATSIYA yoki TEXT - ikkalasini ham handle qilish
        if (update.hasMessage()) {
            messageHandler.handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.handleCallbackQuery(update.getCallbackQuery());
        }
    }
    // Klaviatura bilan xabar yuborish
    public void sendMessage(Long chatId, String text, ReplyKeyboard keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .replyMarkup(keyboard)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Xabar yuborishda xatolik: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Klaviaturasiz xabar yuborish (faqat text)
    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public Message sendPhoto(SendPhoto sendPhoto) {
        try {
            return execute(sendPhoto);
        } catch (TelegramApiException e) {
            System.err.println("Rasm yuborishda xatolik: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void answerCallbackQuery(String callbackQueryId, String text) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .text(text)
                .showAlert(false)
                .build();
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            System.err.println("Callback javobida xatolik: " + e.getMessage());
        }
    }

    public void editMessageReplyMarkup(EditMessageReplyMarkup editMarkup) {
        try {
            execute(editMarkup);
        } catch (TelegramApiException e) {
            System.err.println("Keyboard yangilashda xatolik: " + e.getMessage());
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