package safe.bank.app.authservice.services;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import safe.bank.app.authservice.controller_advice.exceptions.UserCreationException;
import safe.bank.app.authservice.dtos.BankUserDTO;
import safe.bank.app.authservice.dtos.UserPostDTO;
import safe.bank.app.authservice.entities.ErrorResponseEntity;
import safe.bank.app.authservice.mappers.BankUserMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final RestTemplate restTemplate;
    private final RealmResource realmResource;
    private final BankService bankService;
    private final BankUserMapper bankUserMapper;
    private final PartialPasswordService partialPasswordService;
    private static final Logger logger = LoggerFactory.getLogger(LoggerFactory.PROVIDER_PROPERTY_KEY);

    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.scope}")
    private String scope;

    public String login(String username, String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username",username);
        map.add("password",password);
        map.add("client_id",clientId);
        map.add("grant_type", "password");
        map.add("client_secret",clientSecret);
        map.add("scope",scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(keycloakTokenUri, request, String.class);
    }

    public void logout(String userId) {
        realmResource.users().get(userId).logout();
    }

    public void register(UserPostDTO userPostDTO) throws UserCreationException {

        UsersResource usersResource = realmResource.users();
        UserRepresentation user = getUserRepresentation(userPostDTO);

        try (Response response = usersResource.create(user)) {

            if(response.getStatus() != HttpStatus.CREATED.value()) {
                ErrorResponseEntity responseEntity = response.readEntity(ErrorResponseEntity.class);
                throw new UserCreationException(responseEntity.getErrorMessage());
            }

            String userId = extractUserIdFromResponse(response);
            assignRoles(userId, List.of("USER"));

            registerBankUser(userPostDTO, userId); //What if system fails to create user?
            partialPasswordService.createPartialPasswordSet(
                    userPostDTO.getPassword(),
                    UUID.fromString(userId)
            );
        }
    }

    public String refreshJwtToken(String refreshToken) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id",clientId);
        map.add("grant_type", "refresh_token");
        map.add("client_secret",clientSecret);
        map.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        return restTemplate.postForObject(keycloakTokenUri, request, String.class);
    }

    public List<String> getRoles(String userId) {

        UsersResource usersResource = realmResource.users();

        List<RoleRepresentation> roles = usersResource.get(userId).roles().realmLevel().listAll();
        return roles
                .stream()
                .map(RoleRepresentation::toString)
                .toList();
    }

    public void forgotPasswordEmail(String email) {
        logger.info(String.format("Sending email to the user with email: %s", email));
    }

    private UserRepresentation getUserRepresentation(UserPostDTO userPostDTO) {
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

    private void assignRoles(String userId, List<String> roles) {
        List<RoleRepresentation> roleList = rolesToRealmRoleRepresentation(roles);
        realmResource
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(roleList);
    }

    private List<RoleRepresentation> rolesToRealmRoleRepresentation(List<String> roles) {
        List<RoleRepresentation> existingRoles = realmResource
                .roles()
                .list();

        List<String> serverRoles = existingRoles
                .stream()
                .map(RoleRepresentation::getName)
                .toList();
        List<RoleRepresentation> resultRoles = new ArrayList<>();

        for (String role : roles) {
            int index = serverRoles.indexOf(role);
            if (index != -1) {
                resultRoles.add(existingRoles.get(index));
            }

        }
        return resultRoles;
    }

    private String extractUserIdFromResponse(Response response) {

        List<Object> locations = response.getMetadata().get("Location");
        String location = (String) locations.get(0);
        List<String> parts = Arrays.stream(location.split("/")).toList();
        return parts.get(parts.size() - 1);
    }

    private void registerBankUser(UserPostDTO userPostDTO, String userId) {

        BankUserDTO bankUserDTO = bankUserMapper.fromUserToBankUserDTO(userPostDTO);
        bankUserDTO.setUserId(UUID.fromString(userId));

        Mono<ResponseEntity<String>> responseMono = bankService.createBankUser(bankUserDTO);

        responseMono
                .map(ResponseEntity::getStatusCode)
                .block();
    }

}
