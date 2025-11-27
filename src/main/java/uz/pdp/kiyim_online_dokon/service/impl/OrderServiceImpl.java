package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.OrderDTO;
import uz.pdp.kiyim_online_dokon.dto.OrderItemDTO;
import uz.pdp.kiyim_online_dokon.entity.OrderItem;
import uz.pdp.kiyim_online_dokon.entity.Orders;
import uz.pdp.kiyim_online_dokon.entity.Products;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;
import uz.pdp.kiyim_online_dokon.repository.OrderItemRepository;
import uz.pdp.kiyim_online_dokon.repository.OrdersRepository;
import uz.pdp.kiyim_online_dokon.repository.ProductsRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.OrderService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UsersRepository userRepository;
    private final ProductsRepository productsRepository;

    private OrderDTO toDTO(Orders order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getPriceAtPurchase(),
                        i.getQuantity()))
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getPaymentMethod().name(),
                items,
                order.getCreatedAt(),
                order.getPayment() == null ? null : order.getPayment().getId()
        );
    }

    @Override
    public OrderDTO createOrder(OrderDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        order.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));
        order.setTotalPrice(dto.getTotalPrice());

        Orders savedOrder = orderRepository.save(order);

        List<OrderItem> items = dto.getItems().stream().map(i -> {
            Products product = productsRepository.findById(i.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setPriceAtPurchase(i.getPriceAtPurchase());
            item.setQuantity(i.getQuantity());
            return item;
        }).toList();

        orderItemRepository.saveAll(items);

        savedOrder.setItems(items);

        return toDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(Integer id, OrderDTO dto) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        order.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));

        return toDTO(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Integer id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    @Override
    public OrderDTO getOrderById(Integer id) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
