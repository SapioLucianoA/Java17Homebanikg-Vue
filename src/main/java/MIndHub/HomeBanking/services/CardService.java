package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Card;
import MIndHub.HomeBanking.models.CardColor;
import MIndHub.HomeBanking.models.CardType;
import MIndHub.HomeBanking.models.Client;

import java.util.List;

public interface CardService {
    void saveCard (Card card);

    List<Card> findAllCards ();

    boolean existCardByNumber(String number);

    boolean existCardByNumberAndClient(String number, Client client);

    Card findCardByNumber (String number);
    boolean existCardByClientColorTypeAndIsActive (Client client, CardColor color, CardType type, boolean isActive );
}
