
package uz.pdp.kiyim_online_dokon.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.kiyim_online_dokon.bot.client.BackendClient;
import uz.pdp.kiyim_online_dokon.bot.command.BotCommands;
import uz.pdp.kiyim_online_dokon.bot.dto.LoginResponse;
import uz.pdp.kiyim_online_dokon.bot.security.AuthService2;
import uz.pdp.kiyim_online_dokon.bot.service.BotResponseService;
import uz.pdp.kiyim_online_dokon.bot.util.MessageBuilder;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final BackendClient backendClient;
    private final AuthService2 authService;
    private final BotResponseService responseService;

    public SendMessage handle(String chatId, String command, String fullMessage) {
        return switch (command) {
            case BotCommands.START -> startCommand(chatId);
            case BotCommands.LOGIN -> loginCommand(chatId, fullMessage);
            case BotCommands.LOGOUT -> logoutCommand(chatId);
            case BotCommands.PRODUCTS -> responseService.getProductsMessage(chatId);
            case BotCommands.CART -> cartCommand(chatId);
            case BotCommands.ORDER -> orderCommand(chatId);
            default -> MessageBuilder.text(chatId, "❗ Noto'g'ri buyruq: " + command);
        };
    }

    private SendMessage startCommand(String chatId) {
        return MessageBuilder.text(chatId,
                "👋 Assalomu alaykum! Bizning online kiyimlar do‘konimizga xush kelibsiz.\n\n" +
                        "⚡ Buyruqlar:\n" +
                        "/login - tizimga kirish\n" +
                        "/products - mahsulotlar\n" +
                        "/cart - savatcha\n" +
                        "/order - buyurtmalar\n" +
                        "/help - yordam");
    }

    private SendMessage loginCommand(String chatId, String fullMessage) {
        try {
            String[] parts = fullMessage.trim().split("\\s+");
            if (parts.length < 3) {
                return MessageBuilder.text(chatId, "❗ Foydalanish: /login username password");
            }
            String username = parts[1];
            String password = parts[2];

            LoginResponse resp = backendClient.login(username, password);
            authService.saveToken(chatId, resp.getToken());

            return MessageBuilder.text(chatId, "✅ Tizimga muvaffaqiyatli kirdingiz.");
        } catch (Exception e) {
            return MessageBuilder.text(chatId, "❌ Login xatosi: " + e.getMessage());
        }
    }

    private SendMessage logoutCommand(String chatId) {
        authService.removeToken(chatId);
        return MessageBuilder.text(chatId, "🔐 Tizimdan chiqdingiz.");
    }

    private SendMessage cartCommand(String chatId) {
        String token = authService.getToken(chatId);
        if (token == null) {
            return MessageBuilder.text(chatId, "❗ Avval /login qiling!");
        }
        return responseService.getCartMessage(chatId, token);
    }

    private SendMessage orderCommand(String chatId) {
        String token = authService.getToken(chatId);
        if (token == null) {
            return MessageBuilder.text(chatId, "❗ Avval /login qiling!");
        }
        return responseService.checkoutMessage(chatId, token);
    }
}