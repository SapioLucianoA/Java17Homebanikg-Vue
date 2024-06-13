package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.Enums.AccountType;
import MIndHub.HomeBanking.dtosAndRecords.AccountDTO;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.Transaction;
import MIndHub.HomeBanking.repositories.AccountRepository;
import MIndHub.HomeBanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;


    //---------------------------------------- GETS ------------------------------------

    //all accounts
    @GetMapping("/get/accounts")
    public List<AccountDTO> getAllAccount(){
        List<Account> accountList = accountService.findAllAccounts();
        return accountList.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }


    //account for id
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable String accountId, Authentication authentication){

        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        Account account = accountService.findAccountById(accountId);

        if (!client.getAccountSet().contains(account)){
            return new ResponseEntity<>("No proprietary of the account", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }

    //accounts of the current client
    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> currentAccounts(Authentication authentication) {
        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);
        Set<AccountDTO> accountDTOS = client.getAccountSet().stream().filter(account -> account.isActive()).map(account -> new AccountDTO(account)).collect(Collectors.toSet());


        return accountDTOS;
    }

    //--------------------------------------- POST ------------------------------------

    //new Account for the  current client
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createdAccount(Authentication authentication, @RequestParam AccountType accountType) {

        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        Set<Account> accounts = client.getAccountSet().stream().filter(account -> account.isActive()).collect(Collectors.toSet());

        if (accounts.size() >= 3) {
            return new ResponseEntity<>("you cant have 3 or more Accounts", HttpStatus.FORBIDDEN);
        }

        // metodos de service y demas
        String accountNumber = accountService.generateAccountNumber();;

        //creacion y guardado
        Account account = new Account(LocalDate.now(), 00.00, accountNumber, accountType, true);

        client.addAccount(account);
        accountService.save(account);
        clientService.save(client);

        return new ResponseEntity<>("Account created success!", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/remove/account")
    public ResponseEntity<?> removeAccount (Authentication authentication,
                                            @RequestParam String number,
                                            @RequestParam boolean isActive){
        String email = authentication.getName();
        Client client = clientService.findClientByEmail(email);
        if (!clientService.clientExistsByEmail(email)){
            return new ResponseEntity<>("The client  dose not exist", HttpStatus.FORBIDDEN);
        }
        if (!isActive){
            return new ResponseEntity<>("The Account is INACTIVE", HttpStatus.FORBIDDEN);
        }
        if (accountService.accountExistByNumber(number)){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!accountService.accountExistByNumberAndClient(number, client)){
            return new ResponseEntity<>("The client is not the owner of the account", HttpStatus.FORBIDDEN);
        }


        Account account = accountService.findAccountByNumber(number);
        if (account.getBalance() > 0 )
        {

            return new ResponseEntity<>("the balance be 0 to remove a account", HttpStatus.FORBIDDEN);
        }

        List<Transaction> transactions = transactionService.findAllTransactionsByAccount(account);

        for (Transaction transaction : transactions){
            transaction.setActive(false);

            transactionService.saveTransaction(transaction);
        }
        account.setActive(false);
        accountService.save(account);
        return new ResponseEntity<>("Account remove success", HttpStatus.CREATED);
    }
}
