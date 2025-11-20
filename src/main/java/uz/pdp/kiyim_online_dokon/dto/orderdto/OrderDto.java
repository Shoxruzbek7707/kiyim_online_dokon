package uz.pdp.kiyim_online_dokon.dto.orderdto;

import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderDto(String orderId, Double totalPrice, OrderStatus status, LocalDateTime createdAt) {}