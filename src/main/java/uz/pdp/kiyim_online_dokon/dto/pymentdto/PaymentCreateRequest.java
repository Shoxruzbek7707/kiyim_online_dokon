package uz.pdp.kiyim_online_dokon.dto.pymentdto;

import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;

public record PaymentCreateRequest(PaymentMethod method) {}