package MIndHub.HomeBanking.services.Implements;

import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.repositories.AccountRepository;
import MIndHub.HomeBanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void save(Account account) {
      accountRepository.save(account);
    }

    @Override
    public Account findAccountByNumber(String number) {
        return accountRepository.findAccountByNumber(number);
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account findAccountById(String Id) {
        return accountRepository.findAccountById(Id);
    }

    @Override
    public String generateAccountNumber() {
        SecureRandom rand = new SecureRandom();
        String num1;
        int num = rand.nextInt(999999999) + 1;
        num1 = String.format("%03d", num);
        while(accountRepository.existsByNumber("VIN-" + num1)){
            num = rand.nextInt(999999999) + 1;
            num1 = String.format("%03d", num);
        }
        return "VIN-" + num1;
    }

    @Override
    public boolean accountExistByNumber(String number) {
        return !accountRepository.existsByNumber(number);
    }

    @Override
    public boolean accountExistByNumberAndClient(String number, Client client) {
        return accountRepository.existsByNumberAndClient(number,client);
    }

    @Override
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }


}
