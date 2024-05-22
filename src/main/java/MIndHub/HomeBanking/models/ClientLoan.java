package MIndHub.HomeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String loanName;
    private Double amount;
    private Integer payments;
    private Integer sold;
    private boolean isActive;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(Double amount, Integer payments, Integer sold, String loanName,boolean isActive ) {
        this.amount = amount;
        this.payments = payments;
        this.sold = sold;
        this.loanName = loanName;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
