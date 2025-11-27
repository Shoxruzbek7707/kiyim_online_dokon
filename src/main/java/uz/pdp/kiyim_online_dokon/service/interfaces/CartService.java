package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.CartItemDTO;
import uz.pdp.kiyim_online_dokon.dto.CartsDTO;

import java.util.List;

public interface CartService {
    CartsDTO getCartByUserId(Integer userId);

    void deleteCart(Integer cartId);

    CartItemDTO addItemToCart(Integer cartId, CartItemDTO cartItemDTO);

    CartsDTO createCart(CartsDTO dto);

    CartsDTO updateCart(Integer cartId, CartsDTO dto);

    CartItemDTO updateCartItem(Integer itemId, CartItemDTO cartItemDTO);

    void deleteCartItem(Integer itemId);

    List<CartItemDTO> getCartItems(Integer cartId);
}
