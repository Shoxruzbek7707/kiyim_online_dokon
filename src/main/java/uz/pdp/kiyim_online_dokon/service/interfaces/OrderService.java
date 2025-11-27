package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO dto);

    OrderDTO updateOrder(Integer id, OrderDTO dto);

    void deleteOrder(Integer id);

    OrderDTO getOrderById(Integer id);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByUserId(Integer userId);
}
