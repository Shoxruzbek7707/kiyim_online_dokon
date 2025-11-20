package uz.pdp.kiyim_online_dokon.dto.orderdto;

import uz.pdp.kiyim_online_dokon.dto.addressdto.AddressDto;
import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;

import java.util.List;

public record OrderDetailDto(String orderId, Double totalPrice, OrderStatus status, AddressDto address,
                             List<OrderItemDto> items, PaymentDto payment) {}