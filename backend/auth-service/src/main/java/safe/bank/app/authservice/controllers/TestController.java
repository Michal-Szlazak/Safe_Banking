package safe.bank.app.authservice.controllers;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(JwtAuthenticationToken token) {
        System.out.println(token.getName());
        return "test";
    }
}
