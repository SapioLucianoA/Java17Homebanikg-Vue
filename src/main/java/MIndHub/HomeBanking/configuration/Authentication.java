package MIndHub.HomeBanking.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import MIndHub.HomeBanking.models.Client;
import MIndHub.HomeBanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;




@Configuration
public class Authentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private ClientService clientService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName -> {

            if (!clientService.clientExistsByEmail(inputName)){
                throw new UsernameNotFoundException("Unknown email: " + inputName);
            }
            Client client = clientService.findClientByEmail(inputName);

            if (client.isAdmin()) {
                return new User(client.getEmail(), client.getPassword(),
                        AuthorityUtils.createAuthorityList("ADMIN"));
            }
            else if (!client.isAdmin()) {
                return new User(client.getEmail(), client.getPassword(),
                        AuthorityUtils.createAuthorityList("CLIENT"));
            }
            else {
                throw new UsernameNotFoundException("Unknown email: " + inputName);
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }
}



