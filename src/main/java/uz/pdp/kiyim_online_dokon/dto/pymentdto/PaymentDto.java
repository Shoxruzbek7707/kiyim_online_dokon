package uz.pdp.kiyim_online_dokon.dto.pymentdto;

import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;

public record PaymentDto(Double amount, PaymentMethod method, OrderStatus.PaymentStatus status) {}