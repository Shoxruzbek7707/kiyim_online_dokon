package uz.pdp.kiyim_online_dokon; // Loyihaning asosiy paketi

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class KiyimOnlineDokonApplication { // Sizning loyihangiz nomi

    public static void main(String[] args) {
        // Spring Boot ilovasini ishga tushirish
        // Bu bosqichda Spring avtomatik ravishda barcha @Component'larni (jumladan XaridBotni) yuklaydi.
        SpringApplication.run(KiyimOnlineDokonApplication.class, args);
    }
}