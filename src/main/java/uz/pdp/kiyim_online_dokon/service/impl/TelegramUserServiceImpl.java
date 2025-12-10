package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.kiyim_online_dokon.entity.TelegramUser;
import uz.pdp.kiyim_online_dokon.repository.TelegramUserRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.TelegramUserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    @Override
    @Transactional
    public Integer createOrGetUser(Long chatId, String firstName, String lastName, String username) {
        // Avval mavjudligini tekshirish
        return telegramUserRepository.findByChatId(chatId)
                .map(TelegramUser::getId)
                .orElseGet(() -> {
                    // Yangi foydalanuvchi yaratish
                    TelegramUser newUser = TelegramUser.builder()
                            .chatId(chatId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .username(username)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .active(true)
                            .build();

                    TelegramUser saved = telegramUserRepository.save(newUser);
                    return saved.getId();
                });
    }

    @Override
    public TelegramUser findByChatId(Long chatId) {
        return telegramUserRepository.findByChatId(chatId)
                .orElse(null);
    }

    @Override
    @Transactional
    public void updatePhoneNumber(Long chatId, String phoneNumber) {
        telegramUserRepository.findByChatId(chatId)
                .ifPresent(user -> {
                    user.setPhoneNumber(phoneNumber);
                    user.setUpdatedAt(LocalDateTime.now());
                    telegramUserRepository.save(user);
                });
    }
}