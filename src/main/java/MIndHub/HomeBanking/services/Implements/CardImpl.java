package MIndHub.HomeBanking.services.Implements;

import MIndHub.HomeBanking.models.Card;
import MIndHub.HomeBanking.models.CardColor;
import MIndHub.HomeBanking.models.CardType;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.repositories.CardRepository;
import MIndHub.HomeBanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CardImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public boolean existCardByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public boolean existCardByNumberAndClient(String number, Client client) {
        return cardRepository.existsByNumberAndClient(number, client);
    }

    @Override
    public Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public boolean existCardByClientColorTypeAndIsActive(Client client, CardColor color, CardType type, boolean isActive) {
        return cardRepository.existsByClientAndColorAndTypeAndIsActive(client, color, type, isActive);
    }


}
