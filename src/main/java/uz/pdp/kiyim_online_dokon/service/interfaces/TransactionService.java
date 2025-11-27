package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO dto);

    TransactionDTO getTransaction(Integer id);

    TransactionDTO updateTransaction(Integer id, TransactionDTO dto);

    void deleteTransaction(Integer id);

    List<TransactionDTO> getTransactionsByOrder(Integer orderId);

    List<TransactionDTO> getTransactionsByUser(Integer userId);
}
