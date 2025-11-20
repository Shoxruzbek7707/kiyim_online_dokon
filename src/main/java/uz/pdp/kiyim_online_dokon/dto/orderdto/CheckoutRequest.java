package uz.pdp.kiyim_online_dokon.dto.orderdto;

import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;

public record CheckoutRequest(Long addressId, PaymentMethod paymentMethod, String note) {}