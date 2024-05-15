package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.Enums.AccountType;
import MIndHub.HomeBanking.dtosAndRecords.ClientDTO;
import MIndHub.HomeBanking.dtosAndRecords.ClientRecord;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.services.AccountService;
import MIndHub.HomeBanking.services.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
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
    public ResponseEntity<Object> createAdmin(@RequestBody ClientRecord clientRecord) {

        if (clientService.clientExistsByEmail(clientRecord.email())) {
            return new ResponseEntity<>("email already in use", HttpStatus.FORBIDDEN);
        }

        if (clientRecord.name().isBlank()) {
            return new ResponseEntity<>("Missing Name", HttpStatus.FORBIDDEN);
        }
        if (clientRecord.lastName().isBlank()) {
            return new ResponseEntity<>("Missing Last Name", HttpStatus.FORBIDDEN);
        }
        if (clientRecord.email().isBlank()) {
            return new ResponseEntity<>("Missing Email", HttpStatus.FORBIDDEN);
        }
        if (clientRecord.password().isBlank()) {
            return new ResponseEntity<>("Missing Password", HttpStatus.FORBIDDEN);
        }




        Client client = new Client(clientRecord.name(), clientRecord.lastName(), passwordEncoder.encode(clientRecord.password()), clientRecord.email(), true);
        clientService.save(client);
        return new ResponseEntity<>("new admin create", HttpStatus.CREATED);
    };
}
