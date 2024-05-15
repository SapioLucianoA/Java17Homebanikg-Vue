package MIndHub.HomeBanking.models;

import MIndHub.HomeBanking.Enums.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate currentDateValue;
    private Double balance;
    private String number;
    private AccountType accountType;
    private boolean isActive;

    @ManyToOne
    private Client client;

    @JsonIgnore
    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();


    public Account() {
    }

    public Account(LocalDate currentDateValue, Double balance, String number, AccountType accountType, boolean isActive) {
        this.currentDateValue = currentDateValue;
        this.balance = balance;
        this.number = number;
        this.accountType = accountType;
        this.isActive = isActive;

    }

    public String getId() {
        return id;
    }

    public LocalDate getCurrentDateValue() {
        return currentDateValue;
    }

    public void setCurrentDateValue(LocalDate currentDateValue) {
        this.currentDateValue = currentDateValue;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }


    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction (Transaction transaction){
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
