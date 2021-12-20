package ru.pudgy.vertex.rest.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import ru.pudgy.vertex.model.repository.AccountRepository;
import ru.pudgy.vertex.model.repository.SchemaRepository;
import javax.inject.Singleton;

import java.util.*;

import static ru.pudgy.vertex.rest.security.SecurityHelper.ACCOUNT_ATTRIBUTES;
import static ru.pudgy.vertex.rest.security.SecurityHelper.SCHEMA_ATTRIBUTES;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    private final AccountRepository accountRepository;
    private final SchemaRepository schemaRepository;
    private final BCryptPasswordEncoderService passwordEncoder = new BCryptPasswordEncoderService();

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        String userName = authenticationRequest.getIdentity().toString();
        String secret =  authenticationRequest.getSecret().toString();
        ObjectMapper mapper = new ObjectMapper();
        return accountRepository.findByName(userName)
                        .filter(account ->  passwordEncoder.matches(secret, account.getPasswd()))
                        .flatMap(account -> schemaRepository.findById(account.getSchemata())
                                            .map(schemata -> {
                                                String acc = null;
                                                String sch = null;
                                                try {
                                                    acc = mapper.writeValueAsString(account);
                                                    sch = mapper.writeValueAsString(schemata);
                                                } catch (JsonProcessingException e) {
                                                    log.error(e.getMessage(), e);
                                                }

                                                List<String> roles = new ArrayList<>();
                                                roles.add("ROLE_USER");
                                                Map<String, Object> attributes = new HashMap<>();
                                                attributes.put(ACCOUNT_ATTRIBUTES, acc);
                                                attributes.put(SCHEMA_ATTRIBUTES, sch);
                                                return Flowable.just( AuthenticationResponse.success(account.getName(), roles, attributes));
//                                                        (AuthenticationResponse) new ServerAuthentication(
//                                                                account.getName(),
//                                                                roles /*List.of("ROLE_USER")*/,   // TODO add roles to Account
//                                                                attributes/*Map.of(ACCOUNT_ATTRIBUTES, acc, SCHEMA_ATTRIBUTES, sch)*/)
//                                                );
                                            })
                        )
                        .orElse(Flowable.just(AuthenticationResponse.failure(AuthenticationFailureReason.USER_NOT_FOUND)));
    }
}