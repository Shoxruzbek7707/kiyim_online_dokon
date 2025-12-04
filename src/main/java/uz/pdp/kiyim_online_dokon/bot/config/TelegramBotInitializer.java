package uz.pdp.kiyim_online_dokon.bot.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.pdp.kiyim_online_dokon.bot.service.TelegramBotService;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramBotInitializer {

    private final TelegramBotService bot;

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // 1-qadam: avval webhookni tozalaymiz (404 bo‘lsa ham xato bermaydi)
            try {
                bot.execute(DeleteWebhook.builder().build());
                log.info("Eski webhook muvaffaqiyatli o‘chirildi");
            } catch (Exception e) {
                if (e.getMessage().contains("404")) {
                    log.info("Webhook yo‘q edi, polling rejimida ishlaymiz");
                } else {
                    log.warn("Webhook o‘chirishda muammo: {}", e.getMessage());
                }
            }

            // 2-qadam: endi botni ro‘yxatdan o‘tkazamiz
            botsApi.registerBot(bot);

            System.out.println("\n" +
                    "╔══════════════════════════════════════════╗\n" +
                    "║        BOT 100% ISHLADI!                 ║\n" +
                    "║   Username: @" + bot.getBotUsername() + " ".repeat(20) + "║\n" +
                    "╚══════════════════════════════════════════╝\n");

        } catch (TelegramApiException e) {
            log.error("BOT ISHGA TUSHMADI! Xato: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}