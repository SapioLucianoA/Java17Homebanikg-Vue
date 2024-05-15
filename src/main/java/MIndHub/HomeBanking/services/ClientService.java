package MIndHub.HomeBanking.services;

import MIndHub.HomeBanking.models.Client;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.List;

public interface ClientService {

    Client findClientByEmail(String email);

    void save(Client client);

    List<Client> findAll();

    boolean clientExistsByEmail(String email);

    boolean passwordValid (String password);
}
