package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.CartItemDTO;
import uz.pdp.kiyim_online_dokon.dto.CartsDTO;
import uz.pdp.kiyim_online_dokon.entity.CartItem;
import uz.pdp.kiyim_online_dokon.entity.Carts;
import uz.pdp.kiyim_online_dokon.entity.Products;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.CartItemRepository;
import uz.pdp.kiyim_online_dokon.repository.CartsRepository;
import uz.pdp.kiyim_online_dokon.repository.ProductsRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.CartService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartsRepository cartRepository;
    private final UsersRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductsRepository productsRepository;

    private CartsDTO toDTO(Carts cart) {
        List<CartItemDTO> items = cart.getItems()
                .stream()
                .map(this::toDTO)
                .toList();

        return new CartsDTO(
                cart.getId(),
                cart.getUser().getId(),
                items
        );
    }

    private CartItemDTO toDTO(CartItem item) {
        return new CartItemDTO(
                item.getId(),
                item.getCart().getId(),
                item.getProduct().getId(),
                item.getQuantity()
        );
    }

    @Override
    public CartsDTO getCartByUserId(Integer userId) {
        Carts carts = cartRepository.findByUserId(userId);
        return toDTO(carts);
    }

    @Override
    public void deleteCart(Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public CartItemDTO addItemToCart(Integer cartId, CartItemDTO cartItemDTO) {
        Carts cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Products product = productsRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());

        CartItem saved = cartItemRepository.save(cartItem);
        return  toDTO(saved);

    }

    @Override
    public CartsDTO createCart(CartsDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Carts cart = new Carts();
        cart.setUser(user);

        Carts saved;
        saved = cartRepository.save(cart);
        return toDTO(saved);
    }

    @Override
    public CartsDTO updateCart(Integer cartId, CartsDTO dto) {
        Carts cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        cart.setUser(user);

        Carts updated = cartRepository.save(cart);
        return toDTO(updated);
    }

    @Override
    public CartItemDTO updateCartItem(Integer itemId, CartItemDTO cartItemDTO) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Products products = productsRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        item.setProduct(products);
        item.setQuantity(cartItemDTO.getQuantity());

        CartItem saved = cartItemRepository.save(item);
        return toDTO(saved);
    }

    @Override
    public void deleteCartItem(Integer itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(item);
    }

    @Override
    public List<CartItemDTO> getCartItems(Integer cartId) {
        Carts cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getItems()
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
