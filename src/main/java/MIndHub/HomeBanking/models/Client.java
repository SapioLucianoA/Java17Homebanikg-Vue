package MIndHub.HomeBanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private  String name, lastName, password, email;

    private boolean isAdmin;


    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private Set<Account> accountSet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private Set<ClientLoan> loans = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private Set<Card> cards = new HashSet<>();


    public Client() {
    }

    public Client(String name, String lastName, String password, String email, boolean isAdmin) {
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccountSet() {
        return accountSet;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAccountSet(Set<Account> accountSet) {
        this.accountSet = accountSet;
    }

    public Set<ClientLoan> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoan> loans) {
        this.loans = loans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void addAccount (Account account){
        account.setClient(this);
        this.accountSet.add(account);
    }
    public void addLoan (ClientLoan clientLoan){
        clientLoan.setClient(this);
        this.loans.add(clientLoan);
    }

    public void addCard (Card card){
        card.setClient(this);
        this.cards.add(card);
    }
}
