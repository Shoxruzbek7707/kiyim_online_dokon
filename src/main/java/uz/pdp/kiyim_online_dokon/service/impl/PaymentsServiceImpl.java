package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.PaymentsDTO;
import uz.pdp.kiyim_online_dokon.entity.Orders;
import uz.pdp.kiyim_online_dokon.entity.Payments;
import uz.pdp.kiyim_online_dokon.entity.Transactions;
import uz.pdp.kiyim_online_dokon.repository.OrdersRepository;
import uz.pdp.kiyim_online_dokon.repository.PaymentsRepository;
import uz.pdp.kiyim_online_dokon.repository.TransactionsRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.PaymentsService;
@Service
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {
    private final PaymentsRepository paymentsRepository;
    private final OrdersRepository orderRepository;
    private final TransactionsRepository transactionRepository;

    @Override
    public PaymentsDTO createPayment(PaymentsDTO dto) {
        Orders order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Transactions transaction = null;
        if (dto.getTransactionId() != null) {
            transaction = transactionRepository.findById(dto.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
        }

        Payments payment = new Payments();
        payment.setOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setStatus(dto.getStatus());
        payment.setTransaction(transaction);

        return toDTO(paymentsRepository.save(payment));
    }

    @Override
    public PaymentsDTO getPayment(Integer id) {
        return paymentsRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public PaymentsDTO updatePayment(Integer id, PaymentsDTO dto) {
        Payments payment = paymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setStatus(dto.getStatus());

        if (dto.getTransactionId() != null) {
            Transactions tx = transactionRepository.findById(dto.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
            payment.setTransaction(tx);
        }

        return toDTO(paymentsRepository.save(payment));
    }

    @Override
    public void deletePayment(Integer id) {
        paymentsRepository.deleteById(id);
    }

    private PaymentsDTO toDTO(Payments p) {
        return new PaymentsDTO(
                p.getId(),
                p.getOrder().getId(),
                p.getAmount(),
                p.getMethod(),
                p.getStatus(),
                p.getTransaction() != null ? p.getTransaction().getId() : null,
                p.getCreatedAt()
        );
    }

}
