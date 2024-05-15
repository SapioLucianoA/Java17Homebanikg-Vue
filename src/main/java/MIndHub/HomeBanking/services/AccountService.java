package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Account;

import java.util.List;

public interface AccountService {

    void save(Account account);

    Account findAccountByNumber(String number);
    List<Account> findAllAccounts();
    Account findAccountById(String Id);
    String generateAccountNumber();
}
