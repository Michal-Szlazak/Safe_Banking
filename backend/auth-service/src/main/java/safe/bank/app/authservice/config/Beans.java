package safe.bank.app.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Beans {


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
