package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.dtosAndRecords.CardDTO;
import MIndHub.HomeBanking.models.Card;
import MIndHub.HomeBanking.models.CardColor;
import MIndHub.HomeBanking.models.CardType;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.services.*;
import MIndHub.HomeBanking.services.Math.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CardService cardService;

    @Autowired
    private ClientLoanService clientLoanService;



    @GetMapping("/clients/current/cards")
    public Set<CardDTO> currentCards(Authentication authentication) {
        String email = authentication.getName();

        Client client = clientService.findClientByEmail(email);
        Set<Card> cardSet = client.getCards();

        return cardSet.stream().filter(Card::isActive).map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createdCard(Authentication authentication, @RequestParam CardType type,
                                              @RequestParam CardColor color){
        String email = authentication.getName();
        Client client = clientService.findClientByEmail(email);
        Set<Card> cards = client.getCards();

        //  all client cards
        long count = cards.stream().filter(card -> card.getType().equals(type)).filter(card -> card.isActive()).count();
       //exist another card?
        if (cardService.existCardByClientColorTypeAndIsActive(client,color,type,true)){
            return new ResponseEntity<>("you have a card active with this color and type", HttpStatus.FORBIDDEN);
        }
        // all ready have 3 of this type
        if (count >= 3) {
            return new ResponseEntity<>("Client already has 3 cards of this type", HttpStatus.FORBIDDEN);
        }
        // methods service and more

        String cvv = CardUtils.generateCVV();
        String number = CardUtils.generateCardNumber();
        String cardHolder = client.getLastName() + " " + client.getName();

        //created and saved
        Card card = new Card(type, color, cvv, cardHolder, number,LocalDate.now(),LocalDate.now().plusYears(5),true);

        client.addCard(card);
        clientService.save(client);
        cardService.saveCard(card);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/client/remove/card")
    public ResponseEntity<Object> removeCard(Authentication authentication, @RequestParam String number,
                                             @RequestParam boolean isActive){
        String email = authentication.getName();
        Client client = clientService.findClientByEmail(email);
        if (!cardService.existCardByNumber(number)){
            return new ResponseEntity<>("The Card dosent exist", HttpStatus.FORBIDDEN);
        }
        if (!clientService.clientExistsByEmail(email)){
            return new ResponseEntity<>("The CLient  dosent exist", HttpStatus.FORBIDDEN);
        }
        if (!cardService.existCardByNumberAndClient(number,client)){
            return new ResponseEntity<>("The client is not the CardHolder", HttpStatus.FORBIDDEN);
        }
        if (!isActive){
            return new ResponseEntity<>("The Card is INACTIVE", HttpStatus.FORBIDDEN);
        }
        Card cardR = cardService.findCardByNumber(number);

        cardR.setActive(false);

        cardService.saveCard(cardR);

        return new ResponseEntity<>("The Card is removed for your account", HttpStatus.CREATED);
    }
}
