package safe.bank.app.authservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UsersResourceBuilder {

    @Value("${keycloak.server-url}")
    private String serverUrl;
    @Value("${keycloak.master-realm}")
    private String masterRealm;
    @Value("${keycloak.realm}")
    private String appRealm;
    @Value("${keycloak.admin}")
    private String admin;
    @Value("${keycloak.admin-password}")
    private String adminPassword;

    public UsersResource usersResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .grantType(OAuth2Constants.PASSWORD)
                .realm(masterRealm)
                .clientId("admin-cli")
                .username(admin)
                .password(adminPassword)
                .serverUrl(serverUrl)
                .build();

        return keycloak.realm(appRealm).users();
    }
}
