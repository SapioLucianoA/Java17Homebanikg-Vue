package MIndHub.HomeBanking.controllers;

import MIndHub.HomeBanking.dtosAndRecords.CardDTO;
import MIndHub.HomeBanking.models.Card;
import MIndHub.HomeBanking.models.CardColor;
import MIndHub.HomeBanking.models.CardType;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.repositories.CardRepository;
import MIndHub.HomeBanking.services.AccountService;
import MIndHub.HomeBanking.services.ClientService;
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
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CardRepository cardRepository;



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

        // Obtén las tarjetas del cliente
        long count = cards.stream().filter(card -> card.getType().equals(type)).filter(card -> card.isActive()).count();

        // ya tenés 3 papu
        if (count >= 3) {
            return new ResponseEntity<>("Client already has 3 cards of this type", HttpStatus.FORBIDDEN);
        }
        // metodos de service y demas

        String cvv = CardUtils.generateCVV();
        String number = CardUtils.generateCardNumber();
        String cardHolder = client.getLastName() + " " + client.getName();

        //creacion y guardado
        Card card = new Card(type, color, cvv, cardHolder, number,LocalDate.now(),LocalDate.now().plusYears(5),true);

        client.addCard(card);
        clientService.save(client);
        cardRepository.save(card);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }


}
