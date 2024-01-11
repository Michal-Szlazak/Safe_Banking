package safe.bank.app.bankservice.configurations;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Beans {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}
