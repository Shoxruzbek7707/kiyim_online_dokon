package uz.pdp.kiyim_online_dokon.bot.session;

import lombok.Getter;
import lombok.Setter;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;

import java.util.*;

@Getter
@Setter
public class UserSession {
    private Long chatId;
    private Integer userId;
    private UserState state;
    private List<ProductsDTO> lastDisplayedProducts;
    private String lastLocation;

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    private Map<Integer, CartItem> cart;

    public UserSession(Long chatId) {
        this.chatId = chatId;
        this.state = UserState.MAIN_MENU;
        this.lastDisplayedProducts = new ArrayList<>();
        this.cart = new HashMap<>();
    }

    public void addToCart(ProductsDTO product) {
        if (cart.containsKey(product.getId())) {
            CartItem item = cart.get(product.getId());
            item.incrementQuantity();
        } else {
            cart.put(product.getId(), new CartItem(product, 1));
        }
    }

    public void updateCartQuantity(Integer productId, int quantity) {
        if (cart.containsKey(productId)) {
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                cart.get(productId).setQuantity(quantity);
            }
        }
    }

    public void incrementQuantity(Integer productId) {
        if (cart.containsKey(productId)) {
            cart.get(productId).incrementQuantity();
        }
    }

    public void decrementQuantity(Integer productId) {
        if (cart.containsKey(productId)) {
            CartItem item = cart.get(productId);
            if (item.getQuantity() > 1) {
                item.decrementQuantity();
            } else {
                cart.remove(productId);
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cart.values());
    }

    public void clearCart() {
        cart.clear();
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public double getTotalPrice() {
        return cart.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public int getTotalItems() {
        return cart.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    // Legacy compatibility
    public List<ProductsDTO> getCart() {
        List<ProductsDTO> products = new ArrayList<>();
        for (CartItem item : cart.values()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                products.add(item.getProduct());
            }
        }
        return products;
    }

    @Getter
    @Setter
    public static class CartItem {
        private ProductsDTO product;
        private int quantity;

        public CartItem(ProductsDTO product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public void incrementQuantity() {
            this.quantity++;
        }

        public void decrementQuantity() {
            if (this.quantity > 0) {
                this.quantity--;
            }
        }

        public double getTotalPrice() {
            return product.getPrice() * quantity;
        }
    }
}