package MIndHub.HomeBanking.repositories;

import MIndHub.HomeBanking.models.Client;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository <Client, String> {

    Client findClientByEmail(String email);


    boolean existsClientByEmail(String email);
}
