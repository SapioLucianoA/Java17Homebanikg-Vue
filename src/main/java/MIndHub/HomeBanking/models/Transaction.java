package MIndHub.HomeBanking.models;

import MIndHub.HomeBanking.Enums.TransactionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Double currentBalance;
    private boolean isActive;

    @ManyToOne
    private Account account;

    public Transaction() {
    }

    public Transaction(TransactionType type, Double amount, String description, LocalDateTime date, boolean isActive, Double currentBalance) {
        this.currentBalance = currentBalance;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
