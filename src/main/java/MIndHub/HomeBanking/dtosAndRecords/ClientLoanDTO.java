package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.models.ClientLoan;

public class ClientLoanDTO {
    private String id;
    private Double amount;
    private Integer payments;
    private Integer sold;
    private String loanName;
    private String clientName;
    private String loan_ID;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.sold = clientLoan.getSold();
        this.clientName = clientLoan.getClient().getLastName()+" "+clientLoan.getClient().getName();
        this.loan_ID = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
    }

    public String getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public Integer getSold() {
        return sold;
    }

    public String getClientName() {
        return clientName;
    }

    public String getLoan_ID() {
        return loan_ID;
    }

    public String getLoanName() {
        return loanName;
    }
}
