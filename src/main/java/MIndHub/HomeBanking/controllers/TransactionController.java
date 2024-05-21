package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.Enums.TransactionType;
import MIndHub.HomeBanking.dtosAndRecords.TransactionDTO;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.Transaction;
import MIndHub.HomeBanking.repositories.TransactionRepository;
import MIndHub.HomeBanking.services.AccountService;
import MIndHub.HomeBanking.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/get/transactions")
    public List<TransactionDTO> getAllTransactions(){
        List<Transaction> transactionList = transactionRepository.findAll();
        return transactionList.stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> sendTransaction (
            Authentication authentication, @RequestParam Double amount, @RequestParam String accountNumberOrigin,
            @RequestParam String accountNumberSend, @RequestParam String description
    ){
        if (amount.isNaN()){
            return new ResponseEntity<>("Please put a amount valid", HttpStatus.FORBIDDEN);
        }
        if (accountNumberOrigin.isBlank()){
            return new ResponseEntity<>("Missing origen account", HttpStatus.FORBIDDEN);
        }
        if (accountNumberSend.isBlank()){
            return new ResponseEntity<>("Missing account to send", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Missing description", HttpStatus.FORBIDDEN);
        }
        if (accountNumberOrigin.equals(accountNumberSend)){
            return new ResponseEntity<>("The originating account can`t be the same as the account of the receiver", HttpStatus.FORBIDDEN);
        }
        if (!accountService.accountExistByNumber(accountNumberOrigin)) {
            return new ResponseEntity<>("The origin account number does not exist", HttpStatus.FORBIDDEN);
        }
        if (!accountService.accountExistByNumber(accountNumberSend)){
            return new ResponseEntity<>("The account to you want to send not exist", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0){
            return new ResponseEntity<>("The amount cant be 0 or less", HttpStatus.FORBIDDEN);
        }

        //primero obtener el cliente
        //segundo buscar el cliente authenticado
        //buscar la cuenta de origen
        // ver si la cuenta de origen pertenece al cleinte
        //verificar el monto
        String email = authentication.getName();
        Client client = clientService.findClientByEmail(email);
        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
        Account originAccount = accountService.findAccountByNumber(accountNumberOrigin);
        if (!originAccount.getClient().equals(client)){
            return new ResponseEntity<>("The origin account does not belong to the authenticated client", HttpStatus.FORBIDDEN);
        }
        if (originAccount.getBalance() < amount){
            return new ResponseEntity<>("The Amount cant be more than the balance of the account", HttpStatus.FORBIDDEN);
        }
        // llamado a services y lo demas necesario
        Account accountSend = accountService.findAccountByNumber(accountNumberSend);

        //operacion para la transaction debo hacerlo antes por el set
        Double balanceOrigin  = originAccount.getBalance() - amount;
        Double balanceSend = accountSend.getBalance() + amount;
        //crear las weas de operacioness

        originAccount.setBalance(originAccount.getBalance() - amount);


        accountSend.setBalance(accountSend.getBalance() + amount);




        // crear dos transactiones number DEBIT y la number2 CREDIT
        Transaction transactionOriginDebit = new Transaction(TransactionType.DEBIT, -amount, description, LocalDateTime.now(), true, balanceOrigin);
        Transaction transactionSendCredit = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), true, balanceSend);


// Guarda los cambios en las cuentas y transacciones
        originAccount.addTransaction(transactionOriginDebit);
        accountSend.addTransaction(transactionSendCredit);


        transactionRepository.save(transactionOriginDebit);
        transactionRepository.save(transactionSendCredit);
        accountService.save(originAccount);
        accountService.save(accountSend);

        return new ResponseEntity<>("transaction created", HttpStatus.CREATED);
    }


}
