package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.OrderDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO dto);

    OrderDTO updateOrder(Integer id, OrderDTO dto);

    void deleteOrder(Integer id);

    OrderDTO getOrderById(Integer id);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByUserId(Integer userId);
    void createOrderForTelegramUser(Integer telegramUserId, List<ProductsDTO> cartItems, Double totalPrice);
    List<OrderDTO> getOrdersByTelegramUserId(Integer telegramUserId);
}
