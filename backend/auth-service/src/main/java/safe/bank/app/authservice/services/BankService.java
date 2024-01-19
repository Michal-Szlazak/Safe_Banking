package safe.bank.app.authservice.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import safe.bank.app.authservice.dtos.BankUserDTO;

@Service
public class BankService {

    private static final String BACKEND_SERVICE_URI = "http://bank-service:8083";
    private final WebClient webClient;

    public BankService() {
        this.webClient = WebClient.create(BACKEND_SERVICE_URI).mutate().build();
    }

    public Mono<ResponseEntity<String>> createBankUser(BankUserDTO bankUserDTO) {

        String fullUri = BACKEND_SERVICE_URI + "/api/bank/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return webClient.post()
                .uri(fullUri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(bankUserDTO))
                .retrieve()
                .toEntity(String.class);
    }
}
