package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.models.Loan;

import java.util.List;

public class NewLoanDTO {
    private String name;
    private Double maxAmount;
    private List<Integer> payment;

    public NewLoanDTO() {
    }

    public NewLoanDTO(String name, Double  maxAmount, List<Integer> payment){
        this.maxAmount = maxAmount;
        this.name = name;
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayment() {
        return payment;
    }
}
