package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.kiyim_online_dokon.dto.OrderDTO;
import uz.pdp.kiyim_online_dokon.dto.OrderItemDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.entity.*;
import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;
import uz.pdp.kiyim_online_dokon.repository.*;
import uz.pdp.kiyim_online_dokon.service.interfaces.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UsersRepository userRepository;
    private final ProductsRepository productsRepository;
    private final TelegramUserRepository telegramUserRepository;

    private OrderDTO toDTO(Orders order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getPriceAtPurchase(),
                        i.getQuantity()))
                .toList();

        // Telegram yoki Website user ID ni aniqlash
        Integer userId = null;
        if (order.getUser() != null) {
            userId = order.getUser().getId();
        } else if (order.getTelegramUser() != null) {
            userId = order.getTelegramUser().getId();
        }

        return new OrderDTO(
                order.getId(),
                userId,
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getPaymentMethod() != null ? order.getPaymentMethod().name() : "CASH",
                items,
                order.getCreatedAt(),
                order.getPayment() == null ? null : order.getPayment().getId()
        );
    }

    @Override
    @Transactional
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

    // ==========================================
    // TELEGRAM BOT UCHUN YANGI METODLAR
    // ==========================================

    @Override
    @Transactional
    public void createOrderForTelegramUser(Integer telegramUserId, List<ProductsDTO> cartItems, Double totalPrice) {
        TelegramUser telegramUser = telegramUserRepository.findById(telegramUserId)
                .orElseThrow(() -> new RuntimeException("Telegram user not found with ID: " + telegramUserId));

        Orders order = new Orders();
        order.setTelegramUser(telegramUser);
        order.setUser(null); // Website user yo‘q
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setTotalPrice(totalPrice);

        Orders savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cartItems.stream().map(productDTO -> {
            Products product = productsRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productDTO.getId()));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setPriceAtPurchase(productDTO.getPrice());
            item.setQuantity(1);
            return item;
        }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);
    }


    @Override
    public List<OrderDTO> getOrdersByTelegramUserId(Integer telegramUserId) {
        // TelegramUser mavjudligini tekshirish
        if (!telegramUserRepository.existsById(telegramUserId)) {
            throw new RuntimeException("Telegram user not found with ID: " + telegramUserId);
        }

        List<Orders> orders = orderRepository.findByTelegramUserId(telegramUserId);

        return orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // MAVJUD METODLAR
    // ==========================================

    @Override
    @Transactional
    public OrderDTO updateOrder(Integer id, OrderDTO dto) {
        Orders order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        if (dto.getPaymentMethod() != null) {
            order.setPaymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()));
        }
        order.setUpdatedAt(LocalDateTime.now());

        return toDTO(orderRepository.save(order));
    }

    @Override
    @Transactional
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