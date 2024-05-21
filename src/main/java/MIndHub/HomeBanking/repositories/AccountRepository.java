package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Account;
import MIndHub.HomeBanking.models.Client;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AccountRepository extends JpaRepository <Account, String> {

    Account findAccountByNumber (String number);

    Account findAccountById (String id);


     boolean existsByNumber(String number);

     Set<Account> findAccountsByClientAndIsActive(Client client, boolean isActive);
}
