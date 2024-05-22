package MIndHub.HomeBanking.dtosAndRecords;

public class LoanApplicationDTO {
    private String loanId;
    private Double amount;
    private Integer payment;
    private String accountNumber;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(Double amount, Integer payment, String accountNumber,String loanId) {
        this.amount = amount;
        this.payment = payment;
        this.accountNumber = accountNumber;
        this.loanId = loanId;
    }

    public String getLoanId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayment() {
        return payment;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
