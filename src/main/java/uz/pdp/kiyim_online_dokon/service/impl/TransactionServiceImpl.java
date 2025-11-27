package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.TransactionDTO;
import uz.pdp.kiyim_online_dokon.entity.Orders;
import uz.pdp.kiyim_online_dokon.entity.Transactions;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.OrdersRepository;
import uz.pdp.kiyim_online_dokon.repository.TransactionsRepository;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.TransactionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionsRepository transactionRepository;
    private final OrdersRepository orderRepository;
    private final UsersRepository userRepository;

    @Override
    public TransactionDTO createTransaction(TransactionDTO dto) {
        Orders order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transactions tx = new Transactions();
        tx.setOrder(order);
        tx.setUser(user);
        tx.setAmount(dto.getAmount());
        tx.setPaymentMethod(dto.getPaymentMethod());
        tx.setStatus(dto.getStatus());
        tx.setProviderTransactionId(dto.getProviderTransactionId());
        tx.setRawResponse(dto.getRawResponse());
        tx.setUpdatedAt(dto.getUpdatedAt());

        return toDTO(transactionRepository.save(tx));
    }

    @Override
    public TransactionDTO getTransaction(Integer id) {
        return transactionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public TransactionDTO updateTransaction(Integer id, TransactionDTO dto) {
        Transactions tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setStatus(dto.getStatus());
        tx.setProviderTransactionId(dto.getProviderTransactionId());
        tx.setRawResponse(dto.getRawResponse());
        tx.setUpdatedAt(dto.getUpdatedAt());

        return toDTO(transactionRepository.save(tx));
    }

    @Override
    public void deleteTransaction(Integer id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<TransactionDTO> getTransactionsByOrder(Integer orderId) {
        return transactionRepository.findByOrderId(orderId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<TransactionDTO> getTransactionsByUser(Integer userId) {
        return transactionRepository.findByUserId(userId)
                .stream().map(this::toDTO).toList();
    }

    private TransactionDTO toDTO(Transactions t) {
        return new TransactionDTO(
                t.getId(),
                t.getOrder().getId(),
                t.getUser().getId(),
                t.getAmount(),
                t.getStatus(),
                t.getPaymentMethod(),
                t.getProviderTransactionId(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getRawResponse()
        );
    }
}
