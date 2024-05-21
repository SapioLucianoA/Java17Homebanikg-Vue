package MIndHub.HomeBanking.dtosAndRecords;

import MIndHub.HomeBanking.models.Card;
import MIndHub.HomeBanking.models.CardColor;
import MIndHub.HomeBanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private String id;
    private CardType type;
    private CardColor color;
    private String cvv;
    private String cardHolder;
    private String number;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private boolean isActive;
    private String clientName;

    public CardDTO() {
    }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.color = card.getColor();
        this.cvv = card.getCvv();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.isActive = card.isActive();
        this.clientName = card.getClient().getLastName() + " " + card.getClient().getName();
    }

    public String getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getCvv() {
        return cvv;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getClientName() {
        return clientName;
    }
}
