package uz.pdp.kiyim_online_dokon.dto.admindto;

import uz.pdp.kiyim_online_dokon.dto.addressdto.AddressDto;
import uz.pdp.kiyim_online_dokon.dto.orderdto.OrderItemDto;
import uz.pdp.kiyim_online_dokon.dto.pymentdto.PaymentDto;
import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;

import java.util.List;

public record OrderAdmindetailDto(String orderId, String customerName, String phone, AddressDto address,
                                  List<OrderItemDto> items, PaymentDto payment, OrderStatus status) {}