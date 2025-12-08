package uz.pdp.kiyim_online_dokon.bot.session;

import lombok.Data;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserSession {
    private final Long chatId;
    private UserState state;

    // Foydalanuvchi savati (DTO o'rniga haqiqiy CartDTO ishlatish yaxshiroq)
    private List<ProductsDTO> cart = new ArrayList<>();

    // Oxirgi qidiruv yoki kategoriya mahsulotlari ro'yxati (Reply Tugmalar orqali tanlash uchun)
    private List<ProductsDTO> lastDisplayedProducts = new ArrayList<>();

    public UserSession(Long chatId) {
        this.chatId = chatId;
        this.state = UserState.MAIN_MENU;
    }

    public void addToCart(ProductsDTO product) {
        this.cart.add(product);
    }
}