package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Account;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository <Account, String> {

    Account findAccountByNumber (String number);

    Account findAccountById (String id);


     boolean existsByNumber(String number);
}
