package MIndHub.HomeBanking.services.Implements;

import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.repositories.ClientRepository;
import MIndHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClientImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findClientByEmail(String email) {
     return   clientRepository.findClientByEmail(email);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public boolean clientExistsByEmail(String email) {
        return clientRepository.existsClientByEmail(email);
    }

    @Override
    public boolean passwordValid(String password) {
        if (password.length() < 8){
            return false;
        }
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        Set<Character> specialChars = new HashSet<>();
        specialChars.add('_');
        specialChars.add('@');
        specialChars.add('#');
        specialChars.add('$');
        specialChars.add('%');
        specialChars.add('!');
        specialChars.add('?');
        specialChars.add('/');
        specialChars.add('(');
        specialChars.add(')');
        specialChars.add('=');
        specialChars.add('-');
        specialChars.add('&');
        specialChars.add('¡');
        specialChars.add('¿');

        for (char i : password.toCharArray()){
            if (Character.isLowerCase(i)){
                hasLowercase = true;
            }
            if (Character.isUpperCase(i)){
                hasUppercase = true;
            }
            if (Character.isDigit(i)){
                hasNumber = true;
            }
             else if (specialChars.contains(i)){
                 hasSpecialChar = true;
            };
        }
        return hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }


}
