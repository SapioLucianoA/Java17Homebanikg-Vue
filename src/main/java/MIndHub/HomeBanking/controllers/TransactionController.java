package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.dtosAndRecords.TransactionDTO;
import MIndHub.HomeBanking.models.Transaction;
import MIndHub.HomeBanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @GetMapping("/get/transactions")
    public List<TransactionDTO> getAllTransactions(){
        List<Transaction> transactionList = transactionRepository.findAll();
        return transactionList.stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }



}
