package uz.pdp.kiyim_online_dokon.dto.admindto;

import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderAdminDto(String orderId, String customerName, Double totalPrice, OrderStatus status, LocalDateTime createdAt) {}