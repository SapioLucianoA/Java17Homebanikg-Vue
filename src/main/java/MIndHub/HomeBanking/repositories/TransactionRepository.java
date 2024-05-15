package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
