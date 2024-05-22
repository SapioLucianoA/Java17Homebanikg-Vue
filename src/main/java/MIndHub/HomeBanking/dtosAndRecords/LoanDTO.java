package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.models.Loan;

import java.util.List;

public class LoanDTO {

    private String id;
    private String name;
    private Double maxAmount;

    private List<Integer> payment;


    public LoanDTO(Loan loan){
        id= loan.getId();
        name = loan.getName();
        maxAmount = loan.getMaxAmount();
        payment = loan.getPayment();
    }

    public String getId() {
        return id;
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
