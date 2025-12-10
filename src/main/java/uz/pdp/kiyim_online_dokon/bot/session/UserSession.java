package uz.pdp.kiyim_online_dokon.bot.session;

import lombok.Data;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserSession {
    private Long chatId;
    private Integer userId;  // Database dagi User ID
    private UserState state;
    private List<ProductsDTO> cart;
    private List<ProductsDTO> lastDisplayedProducts;

    public UserSession(Long chatId) {
        this.chatId = chatId;
        this.state = UserState.MAIN_MENU;
        this.cart = new ArrayList<>();
        this.lastDisplayedProducts = new ArrayList<>();
        this.userId = null; // Keyin o'rnatiladi
    }

    public void addToCart(ProductsDTO product) {
        this.cart.add(product);
    }

    public void clearCart() {
        this.cart.clear();
    }

    public void setLastDisplayedProducts(List<ProductsDTO> products) {
        this.lastDisplayedProducts = products;
    }

    public List<ProductsDTO> getCart() {
        return new ArrayList<>(cart);
    }

    public List<ProductsDTO> getLastDisplayedProducts() {
        return new ArrayList<>(lastDisplayedProducts);
    }
}