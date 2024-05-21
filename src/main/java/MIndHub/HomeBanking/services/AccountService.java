package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.dtosAndRecords.AccountDTO;
import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;

import java.util.List;
import java.util.Set;

public interface AccountService {

    void save(Account account);

    Account findAccountByNumber(String number);
    List<Account> findAllAccounts();
    Account findAccountById(String Id);
    String generateAccountNumber();
    boolean accountExistByNumber(String number);
}
