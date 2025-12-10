package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.entity.TelegramUser;

public interface TelegramUserService {

    /**
     * Telegram foydalanuvchisini yaratadi yoki mavjud bo'lsa qaytaradi
     */
    Integer createOrGetUser(Long chatId, String firstName, String lastName, String username);

    /**
     * Chat ID bo'yicha foydalanuvchini topadi
     */
    TelegramUser findByChatId(Long chatId);

    /**
     * Telefon raqamini yangilaydi
     */
    void updatePhoneNumber(Long chatId, String phoneNumber);
}