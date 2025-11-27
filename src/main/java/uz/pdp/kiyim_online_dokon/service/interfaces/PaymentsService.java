package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.PaymentsDTO;

public interface PaymentsService {
    PaymentsDTO createPayment(PaymentsDTO dto);

    PaymentsDTO getPayment(Integer id);

    PaymentsDTO updatePayment(Integer id, PaymentsDTO dto);

    void deletePayment(Integer id);
}
