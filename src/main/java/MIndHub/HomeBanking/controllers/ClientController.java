package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.Enums.AccountType;
import MIndHub.HomeBanking.dtosAndRecords.AdminRecord;
import MIndHub.HomeBanking.dtosAndRecords.ClientDTO;
import MIndHub.HomeBanking.dtosAndRecords.ClientRecord;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    //----------------------------------- GETS --------------------------------------

    //all clients
    @GetMapping("/clients")
    public List<ClientDTO> getAllClient(){
        List<Client> clientList = clientService.findAll();
        return clientList.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    // current Client
    @GetMapping("/clients/current")
    public ClientDTO currentClient(Authentication authentication){
        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        return new ClientDTO(client);

    }

    @GetMapping("/client/admin/{clientEmail}")
    public ClientDTO clientMod(@PathVariable String clientEmail){

        Client client = clientService.findClientByEmail(clientEmail);
        return new ClientDTO(client);
    }

    //------------------------------ POST -------------------------------------------

    //New Client
    @PostMapping("/client")
    public ResponseEntity<?> register(@RequestBody ClientRecord clientRecord){

        if (clientService.clientExistsByEmail(clientRecord.email())) {
            return new ResponseEntity<>("email already in use", HttpStatus.FORBIDDEN);
        }

        if (clientRecord.name().isBlank()){
            return new ResponseEntity<>("Missing Name or have spaces", HttpStatus.FORBIDDEN);
        }
        if (clientRecord.lastName().isBlank()){
            return new ResponseEntity<>("Missing last name or have spaces",HttpStatus.FORBIDDEN);
        }
        if (clientRecord.password().isBlank()){
            return new ResponseEntity<>("Missing password or have spaces", HttpStatus.FORBIDDEN);
        }
        if (!clientService.passwordValid(clientRecord.password())) {
            return new ResponseEntity<>("The password needs 8 characters minimum, 1 (one) Uppercase, 1 (one) Number", HttpStatus.FORBIDDEN );
        }
        if (clientRecord.email().isBlank()){
            return new ResponseEntity<>("Missing email or have spaces", HttpStatus.FORBIDDEN);
        }



        Client client = new Client(clientRecord.name(), clientRecord.lastName(), passwordEncoder.encode(clientRecord.password()) , clientRecord.email(), false);
        String accountNumber = accountService.generateAccountNumber();

        Account account = new Account(LocalDate.now(), 00.00, accountNumber, AccountType.NORMAL, true);

        client.addAccount(account);

        clientService.save(client);
        accountService.save(account);


        return new ResponseEntity<>("client created success!!!!!!",HttpStatus.CREATED);
    }

    // New Client ADMIN
    @PostMapping("/client/admin")
    public ResponseEntity<Object> createAdmin(@RequestBody AdminRecord adminRecord, Authentication authentication) {

        Client clientAdmin = clientService.findClientByEmail(authentication.getName());

        if (!clientAdmin.isAdmin()){
            return new ResponseEntity<>("method only for ADMIN",HttpStatus.FORBIDDEN);
        }

        if (clientService.clientExistsByEmail(adminRecord.email())) {
            return new ResponseEntity<>("email already in use", HttpStatus.FORBIDDEN);
        }

        if (adminRecord.name().isBlank()) {
            return new ResponseEntity<>("Missing Name", HttpStatus.FORBIDDEN);
        }
        if (adminRecord.lastName().isBlank()) {
            return new ResponseEntity<>("Missing Last Name", HttpStatus.FORBIDDEN);
        }
        if (adminRecord.email().isBlank()) {
            return new ResponseEntity<>("Missing Email", HttpStatus.FORBIDDEN);
        }
        if (adminRecord.password().isBlank()) {
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }
        if ( !clientService.passwordValid(adminRecord.password())){
            return new ResponseEntity<>("The password needs 8 characters minimum, 1 (one) Uppercase, 1 (one) Number", HttpStatus.FORBIDDEN );
        }



        Client client = new Client(adminRecord.name(), adminRecord.lastName(), passwordEncoder.encode(adminRecord.password()), adminRecord.email(), adminRecord.isAdmin());
        clientService.save(client);

        return new ResponseEntity<>("new Client created", HttpStatus.CREATED);
    };

    @PatchMapping("/client/admin")
    public ResponseEntity<?> patchClient(@RequestBody ClientRecord clientRecord){

        if (clientRecord.name().isBlank()){
            return new ResponseEntity<>("Missing Name or have spaces", HttpStatus.FORBIDDEN);
        }
        if (clientRecord.lastName().isBlank()){
            return new ResponseEntity<>("Missing last name or have spaces",HttpStatus.FORBIDDEN);
        }
        if (clientRecord.password().isBlank()){
            return new ResponseEntity<>("Missing password or have spaces", HttpStatus.FORBIDDEN);
        }
        if (!clientService.passwordValid(clientRecord.password())) {
            return new ResponseEntity<>("The password needs 8 characters minimum, 1 (one) Uppercase, 1 (one) Number", HttpStatus.FORBIDDEN );
        }
        if (clientRecord.email().isBlank()){
            return new ResponseEntity<>("Missing email or have spaces", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findClientByEmail(clientRecord.email());

        client.setEmail(clientRecord.email());
        client.setName(clientRecord.name());
        client.setLastName(clientRecord.lastName());
        client.setPassword(clientRecord.password());

        clientService.save(client);
        return new ResponseEntity<>("CLient Update succes", HttpStatus.OK);
    }

    @PostMapping("/delete/client/admin")
    public ResponseEntity<?> deleteClient(@RequestParam String email, Authentication authentication){
        Client client = clientService.findClientByEmail(email);
        Set<Account> clientAccounts = client.getAccountSet();
        if (authentication.getName().equals(email)){
            return new ResponseEntity<>("cant delete yourself", HttpStatus.FORBIDDEN);
        }
        for (Account account : clientAccounts) {
            if (account.getBalance() > 0) {
                // Saldo mayor que 0: denegar eliminar cliente
                return new ResponseEntity<>("The client cant be delete, have accounts with money", HttpStatus.FORBIDDEN);
            }
        }

// Todos los saldos son 0: proceder con la eliminación del cliente
        clientService.deleteClient(client);
        return new ResponseEntity<>("Client delete succes", HttpStatus.OK);

    }
}
