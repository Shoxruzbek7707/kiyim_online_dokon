
package uz.pdp.kiyim_online_dokon.bot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.kiyim_online_dokon.bot.service.BotResponseService;
import uz.pdp.kiyim_online_dokon.bot.security.AuthService;
import uz.pdp.kiyim_online_dokon.bot.util.MessagerBuilder;

@Component
@RequiredArgsConstructor
public class CallbackHandler {

    private final BotResponseService responseService;
    private final AuthService authService;

    public SendMessage handle(String chatId, String data) {
        String token = authService.getToken(chatId);

        if (data.equals("products")) {
            return responseService.getProductsMessage(chatId);
        }

        if (data.startsWith("product:")) {
            Long id = Long.valueOf(data.split(":")[1]);
            return responseService.getProductDetailMessage(chatId, id);
        }

        if (data.startsWith("addcart:")) {
            Long id = Long.valueOf(data.split(":")[1]);
            if (token == null) {
                return MessagerBuilder.text(chatId, "⚠️ Avval /login qiling!");
            }
            return responseService.addToCartMessage(chatId, token, id);
        }

        return MessagerBuilder.text(chatId, "⚠️ Callback topilmadi: " + data);
    }
}