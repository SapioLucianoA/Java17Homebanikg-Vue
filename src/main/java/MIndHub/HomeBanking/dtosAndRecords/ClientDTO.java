package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.models.ClientLoan;


import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private  String name, lastName, email;
    private Set<AccountDTO> accountSet;
    private Set<ClientLoanDTO> clientLoanSet;

    public ClientDTO(Client client) {
        this.name = client.getName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accountSet = client.getAccountSet().stream().filter(account -> account.isActive() == true).map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.clientLoanSet = client.getLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getLastName() {
        return lastName;
    }
    public Set<AccountDTO> getAccountSet() {
        return accountSet;
    }
    public Set<ClientLoanDTO> getClientLoanSet() {
        return clientLoanSet;
    }
}
