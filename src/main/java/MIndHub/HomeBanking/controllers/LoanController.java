package MIndHub.HomeBanking.controllers;


import MIndHub.HomeBanking.Enums.TransactionType;
import MIndHub.HomeBanking.dtosAndRecords.ClientLoanDTO;
import MIndHub.HomeBanking.dtosAndRecords.LoanApplicationDTO;
import MIndHub.HomeBanking.dtosAndRecords.LoanDTO;
import MIndHub.HomeBanking.dtosAndRecords.NewLoanDTO;
import MIndHub.HomeBanking.models.*;
import MIndHub.HomeBanking.services.*;
import MIndHub.HomeBanking.services.Math.CardUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class LoanController {

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


    @GetMapping("/loans")
    public List<LoanDTO> getAllLoad() {

        List<Loan> loans = loanService.findAllLoans();

        Stream<Loan> loanStream = loans.stream();

        Stream<LoanDTO> loanDTOStream = loanStream.map(loan -> new LoanDTO(loan));

        List<LoanDTO> loanDTOS = loanDTOStream.collect(Collectors.toList());


        return loanDTOS;
    }
    @GetMapping("/loans/{clientLoanId}")
    public ResponseEntity<?> getClientLoan(@PathVariable String clientLoanId, Authentication authentication){
        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        ClientLoan clientLoan = clientLoanService.findClientLoanByClientIdAndLoanId(client.getId(), clientLoanId);

        if (!client.getLoans().contains(clientLoan)) {
            return new ResponseEntity<>("this loan is not yours", HttpStatus.FORBIDDEN);        }

        return new ResponseEntity<>(new ClientLoanDTO(clientLoan), HttpStatus.I_AM_A_TEAPOT);
    }

    @PostMapping("/loan/new")
    public ResponseEntity<Object> newLoan (Authentication authentication, @RequestBody NewLoanDTO newLoanDTO){
        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        if (!client.isAdmin()){
            return new ResponseEntity<>("method only for ADMIN",HttpStatus.FORBIDDEN);
        }
        if (newLoanDTO.getName().isBlank()){
            return new ResponseEntity<>("please complete the name", HttpStatus.FORBIDDEN);
        }
        if (newLoanDTO.getMaxAmount().isNaN()){
            return new ResponseEntity<>("please complete the amount", HttpStatus.FORBIDDEN);
        }
        if (newLoanDTO.getMaxAmount() <= 0){
            return new ResponseEntity<>("The Max Amount cant be 0 or less", HttpStatus.FORBIDDEN);
        }
        if (newLoanDTO.getPayment().isEmpty()){
            return new ResponseEntity<>("Please complete the list od payment", HttpStatus.FORBIDDEN);
        }
        Loan loan = new Loan(newLoanDTO.getName(), newLoanDTO.getMaxAmount(), newLoanDTO.getPayment());

        loanService.saveLoan(loan);
        return new ResponseEntity<>("New Loan created", HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/loan")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Optional<Loan> optionalLoan = loanService.findLoanById(loanApplicationDTO.getLoanId());

        if (optionalLoan.isEmpty()) {
           return new ResponseEntity<>("The Loan does not exist", HttpStatus.FORBIDDEN);
        }

        Loan loan = optionalLoan.get();

        if (accountService.accountExistByNumber(loanApplicationDTO.getAccountNumber())) {
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }


        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Your amount cant be more than the Loan Max Amount", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("The amount can`t be 0 or less", HttpStatus.FORBIDDEN);
        }
        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);

        Account loanAccount = accountService.findAccountByNumber(loanApplicationDTO.getAccountNumber());

        if (!loanAccount.getClient().equals(client)){
            return new ResponseEntity<>("The client is not the proprietary of the account", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayment().contains(loanApplicationDTO.getPayment())){
            return new ResponseEntity<>("The payment does not exist ", HttpStatus.FORBIDDEN);
        }

        if (clientLoanService.existClientLoanByClientLoanAndIsActive(client, loan, true)){
            return new ResponseEntity<>("The client already has a loan of this type", HttpStatus.FORBIDDEN);
        }



        double amount1 = CardUtils.calculatedAmount(loanApplicationDTO.getPayment(), loanApplicationDTO.getAmount());

        Double balanceLoan = loanAccount.getBalance() + loanApplicationDTO.getAmount();

        Transaction loanTransaction  = new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(),"Loan approved",LocalDateTime.now(),true,balanceLoan);

        ClientLoan clientLoan1 = new ClientLoan(amount1, loanApplicationDTO.getPayment(), 0,loan.getName(),true);

        loanAccount.setBalance(loanAccount.getBalance() + loanApplicationDTO.getAmount());

        //adds
        client.addLoan(clientLoan1);
        loanAccount.addTransaction(loanTransaction);
        loan.addClientLoan(clientLoan1);

        //saves
        transactionService.saveTransaction(loanTransaction);

        clientLoanService.saveClientLoan(clientLoan1);

        accountService.save(loanAccount);

        clientService.save(client);

        return new ResponseEntity<>("Loan approved", HttpStatus.CREATED);

    }

    @Transactional
    @PostMapping("/client/pay")
    public ResponseEntity<Object> payLoan(Authentication authentication,@RequestParam String number,
                                          @RequestParam Double amount,
                                          @RequestParam String loanName) {

        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);
        Loan loan = loanService.findLoanByName(loanName);
        ClientLoan clientLoan = clientLoanService.findClientLoanByClientIdAndLoanId(client.getId(), loan.getId());
        Account account = accountService.findAccountByNumber(number);

        if (!clientService.clientExistsByEmail(email)){
            return new ResponseEntity<>("The client dosent exist", HttpStatus.FORBIDDEN);
        }
        if (accountService.accountExistByNumber(number)){
            return new ResponseEntity<>("The account dosent exist", HttpStatus.FORBIDDEN);
        }
        if (amount <= 0){
            return new ResponseEntity<>("The amount cant be 0 nor less", HttpStatus.FORBIDDEN);
        }
        if (clientLoan == null){
            return new ResponseEntity<>("The loan dosent exist", HttpStatus.FORBIDDEN);
        }
        if ( account.getBalance() < amount){
            return  new ResponseEntity<>("The account balance is less than the payment", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getPayments().equals(clientLoan.getSold())){
            clientLoan.setActive(false);
            return new ResponseEntity<>("The Loan is fully paid", HttpStatus.FORBIDDEN);
        }

        Double balanceTransaction = account.getBalance() - amount;
        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction(TransactionType.DEBIT,-amount, "Loan payment",LocalDateTime.now(),true,balanceTransaction);


        clientLoan.setSold(clientLoan.getSold() + 1);

        if (clientLoan.getPayments().equals(clientLoan.getSold())){
            clientLoan.setActive(false);

            transactionService.saveTransaction(transaction);
            clientLoanService.saveClientLoan(clientLoan);
            accountService.save(account);
            return new ResponseEntity<>("client Loan is fully paid", HttpStatus.CREATED);
        }

        transactionService.saveTransaction(transaction);
        clientLoanService.saveClientLoan(clientLoan);
        accountService.save(account);

        return new ResponseEntity<>("client Loan Update", HttpStatus.CREATED);
    }

}
