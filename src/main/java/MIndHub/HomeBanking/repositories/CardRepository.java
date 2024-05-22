package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Card;
import MIndHub.HomeBanking.models.CardColor;
import MIndHub.HomeBanking.models.CardType;
import MIndHub.HomeBanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {

    boolean existsByClientAndColorAndTypeAndIsActive(Client client, CardColor color, CardType type, boolean isActive);

    boolean existsByNumber(String  number);

    boolean existsByNumberAndClient(String number, Client client);

    Card findByNumber(String number);

}
