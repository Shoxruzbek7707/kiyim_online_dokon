package uz.pdp.kiyim_online_dokon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.kiyim_online_dokon.bot.XaridBot;

// Bu Spring konfiguratsiya class'i
@Configuration
public class BotInitializer {

    private final XaridBot xaridBot;

    // Spring avtomatik ravishda XaridBot obyektini inject (kiritadi)
    public BotInitializer(XaridBot xaridBot) {
        this.xaridBot = xaridBot;
    }

    // Spring ilovasi to'liq yuklangandan keyin ishga tushadi
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            // XaridBotni Telegram tizimiga ro'yxatdan o'tkazish
            telegramBotsApi.registerBot(xaridBot);
            System.out.println("✅ Telegram bot muvaffaqiyatli ishga tushirildi!");
        } catch (TelegramApiException e) {
            System.err.println("❌ Telegram botni ro'yxatdan o'tkazishda xato: " + e.getMessage());
            // Agar tokenda xato bo'lsa, bu yerda ko'rinadi.
        }
    }
}