package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.kiyim_online_dokon.entity.TelegramUser;

import java.util.Optional;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Integer> {

    Optional<TelegramUser> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);
}