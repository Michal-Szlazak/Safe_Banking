package safe.bank.app.authservice.services;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import safe.bank.app.authservice.config.UsersResourceBuilder;
import safe.bank.app.authservice.controller_advice.exceptions.UserCreationException;
import safe.bank.app.authservice.dtos.UserPostDTO;
import safe.bank.app.authservice.entities.ErrorResponseEntity;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {

    private final RestTemplate restTemplate;
    private final UsersResource usersResource;

    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.user-info-uri}")
    private String keycloakUserInfo;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.authorization-grant-type}")
    private String grantType;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.scope}")
    private String scope;

    public KeycloakService(RestTemplate restTemplate, UsersResourceBuilder usersResourceBuilder) {
        this.restTemplate = restTemplate;
        usersResource = usersResourceBuilder.usersResource();
    }

    public String login(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username",username);
        map.add("password",password);
        map.add("client_id",clientId);
        map.add("grant_type",grantType);
        map.add("client_secret",clientSecret);
        map.add("scope",scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(keycloakTokenUri, request, String.class);
    }

    public void logout(String userId) {
        usersResource.get(userId).logout();
    }

    public List<String> getRoles(String userId) {

        List<RoleRepresentation> roles = usersResource.get(userId).roles().realmLevel().listAll();
        return roles
                .stream()
                .map(RoleRepresentation::toString)
                .toList();
    }

    private String getUserInfo(String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        return restTemplate.postForObject(keycloakUserInfo, request, String.class);
    }

    public void register(UserPostDTO userPostDTO) throws UserCreationException {

        UserRepresentation user = getUserRepresentation(userPostDTO);

        try (Response response = usersResource.create(user)) {
            if(response.getStatus() != HttpStatus.CREATED.value()) {
                ErrorResponseEntity responseEntity = response.readEntity(ErrorResponseEntity.class);
                throw new UserCreationException(responseEntity.getErrorMessage());
            }
        }
    }

    private static UserRepresentation getUserRepresentation(UserPostDTO userPostDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userPostDTO.getFirstName());
        user.setLastName(userPostDTO.getLastName());
        user.setEmail(userPostDTO.getEmail());
        user.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType("password");
        credentialRepresentation.setValue(userPostDTO.getPassword());
        credentialRepresentation.setTemporary(false);

        user.setCredentials(Collections.singletonList(credentialRepresentation));
        return user;
    }
}
