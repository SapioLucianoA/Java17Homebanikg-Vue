package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.Enums.TransactionType;

import MIndHub.HomeBanking.models.Transaction;

import java.time.LocalDateTime;

public class TransactionDTO {

    private String id;

    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private String accountNumber;

    public TransactionDTO() {
    }

    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.accountNumber = transaction.getAccount().getNumber();
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
