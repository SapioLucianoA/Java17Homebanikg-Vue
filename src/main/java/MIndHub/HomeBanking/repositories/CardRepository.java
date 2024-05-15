package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {
}
