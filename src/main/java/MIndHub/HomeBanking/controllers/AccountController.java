package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.Enums.AccountType;
import MIndHub.HomeBanking.dtosAndRecords.AccountDTO;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.repositories.AccountRepository;
import MIndHub.HomeBanking.repositories.ClientRepository;
import MIndHub.HomeBanking.services.AccountService;
import MIndHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;


    //---------------------------------------- GETS ------------------------------------

    //all accounts
    @GetMapping("/get/accounts")
    public List<AccountDTO> getAllAccount(){
        List<Account> accountList = accountService.findAllAccounts();
        return accountList.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }


    //account for id
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable String id, Authentication authentication){

        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        Account account = accountService.findAccountById(id);

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
        Set<AccountDTO> accountDTOS = client.getAccountSet().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());


        return accountDTOS;
    }

    //--------------------------------------- POST ------------------------------------

    //new Account for the  current client
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createdAccount(Authentication authentication, @RequestParam AccountType accountType) {

        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        Set<Account> accounts = client.getAccountSet();

        Set<Account> accountsFilter = accounts.stream()
                .filter(account -> account.isActive() == true)
                .collect(Collectors.toSet());


        if (accountsFilter.size() >= 3) {
            return new ResponseEntity<>("you cant have 3 or more Accounts", HttpStatus.FORBIDDEN);
        }

        // metodos de service y demas
        String accountNumber = accountService.generateAccountNumber();;

        //creacion y guardado
        Account account = new Account(LocalDate.now(), 00.00, accountNumber, accountType, true);

        accountService.save(account);
        return new ResponseEntity<>("Account created success!", HttpStatus.CREATED);

    }


}
