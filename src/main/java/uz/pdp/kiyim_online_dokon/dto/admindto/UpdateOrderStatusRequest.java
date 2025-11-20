package uz.pdp.kiyim_online_dokon.dto.admindto;

import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;

public record UpdateOrderStatusRequest(OrderStatus status) {}