package MIndHub.HomeBanking;

import MIndHub.HomeBanking.Enums.AccountType;
import MIndHub.HomeBanking.Enums.TransactionType;
import MIndHub.HomeBanking.models.*;
import MIndHub.HomeBanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomeBankingApplication {
		@Autowired
		private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomeBankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, CardRepository cardRepository, ClientLoanRepository clientLoanRepository){
		return (args) -> {
			System.out.println("application started");


			//clients
			Client client1 = new Client("Melba", "Morel", passwordEncoder.encode("123"), "melba@mindhub.com",false);

			Client client2 = new Client("Desi", "Duarte", passwordEncoder.encode("123"), "123@gmail.com",true);


			//accounts
			Account accountClient11 = new Account(LocalDate.now(), 5000.00,"account1", AccountType.NORMAL, true);
			Account accountClient21 = new Account(LocalDate.now(), 3000000.50, "account2",AccountType.NORMAL, true);

			client1.addAccount(accountClient11);
			client2.addAccount(accountClient21);

			Transaction transactionMelba1 = new Transaction(TransactionType.CREDIT, 200.00, "Lunch with my Hommies", LocalDateTime.now().minusHours(4));
			Transaction transactionMelba2 = new Transaction(TransactionType.DEBIT, 30000.00, "Fest with my Hommies", LocalDateTime.now().minusHours(3));
			Transaction transactionMelba3 = new Transaction(TransactionType.CREDIT, 50.00, "play with my Hommies", LocalDateTime.now().minusHours(2));
			Transaction transactionMelba4 = new Transaction(TransactionType.DEBIT, 100.00, "killing with my Hommies", LocalDateTime.now().minusHours(1));

			accountClient11.addTransaction(transactionMelba1);
			accountClient11.addTransaction(transactionMelba2);
			accountClient11.addTransaction(transactionMelba3);
			accountClient11.addTransaction(transactionMelba4);

			//Loans y Payments
			List<Integer> payment1 = List.of(12,24,36,48,60) ;
			List<Integer> payment2 = List.of(6,12,24);
			List<Integer> payment3 = List.of(6,12,24,36);

			Loan mortageLoan = new Loan("Mortgage", 500000.00, payment1);
			Loan personalLoan = new Loan("Personal", 100000.00, payment2);
			Loan autoLoan = new Loan("Auto", 300000.00, payment3);

			//ClientLoans

			ClientLoan clientLoan1MM = new ClientLoan(400000.00, 60);

			mortageLoan.addClientLoan(clientLoan1MM);
			client1.addLoan(clientLoan1MM);

			ClientLoan clientLoan2MM = new ClientLoan(500000.00, 12);

			personalLoan.addClientLoan(clientLoan2MM);
			client1.addLoan(clientLoan2MM);

			ClientLoan clientLoan1DD = new ClientLoan(100000.00, 24);

			personalLoan.addClientLoan(clientLoan1DD);
			client2.addLoan(clientLoan1DD);

			ClientLoan clientLoan2DD = new ClientLoan(200000.00, 36);

			autoLoan.addClientLoan(clientLoan2DD);
			client2.addLoan(clientLoan2DD);

			//Cards
			Card card1MM = new Card(CardType.DEBIT, CardColor.GOLD, "777", "MELBA MOREL", "7783-2231-1232-1233",LocalDate.now(), LocalDate.now().plusYears(5),true);



			Card card2MM = new Card(CardType.DEBIT, CardColor.TITANIUM, "888", "MELBA MOREL", "1231-1231-1231-1232", LocalDate.now(), LocalDate.now().plusYears(5),true);

			client1.addCard(card1MM);
			client1.addCard(card2MM);

			Card card1DD = new Card(CardType.CREDIT, CardColor.SILVER, "555", "DESIDERIO DUARTE", "4564-4564-4564-4564",LocalDate.now(), LocalDate.now().plusYears(5),true);

			client2.addCard(card1DD);

			//saves
			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(accountClient11);
			accountRepository.save(accountClient21);

			transactionRepository.save(transactionMelba1);
			transactionRepository.save(transactionMelba2);
			transactionRepository.save(transactionMelba3);
			transactionRepository.save(transactionMelba4);

			loanRepository.save(mortageLoan);
			loanRepository.save(personalLoan);
			loanRepository.save(autoLoan);

			clientLoanRepository.save(clientLoan1MM);
			clientLoanRepository.save(clientLoan2MM);
			clientLoanRepository.save(clientLoan1DD);
			clientLoanRepository.save(clientLoan2DD);

			cardRepository.save(card1MM);
			cardRepository.save(card2MM);
			cardRepository.save(card1DD);



		};
	}

}
