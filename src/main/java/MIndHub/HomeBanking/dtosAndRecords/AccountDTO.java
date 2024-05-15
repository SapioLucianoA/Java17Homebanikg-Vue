package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.Enums.AccountType;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.Transaction;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private String id;
    private LocalDate currentDateValue;
    private Double balance;
    private String number;
    private String clientFullName;
    private AccountType accountType;
    private Set<TransactionDTO> transactions;


    public AccountDTO(Account account) {
        this.id = account.getId();
        this.currentDateValue = account.getCurrentDateValue();
        this.balance = account.getBalance();
        this.number = account.getNumber();
        this.clientFullName = account.getClient().getLastName() + account.getClient().getName();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.accountType = account.getAccountType();
    }

    public String getId() {
        return id;
    }

    public LocalDate getCurrentDateValue() {
        return currentDateValue;
    }

    public Double getBalance() {
        return balance;
    }

    public String getNumber() {
        return number;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}
