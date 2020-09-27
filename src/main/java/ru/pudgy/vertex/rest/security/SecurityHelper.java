package ru.pudgy.vertex.rest.security;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.security.authentication.Authentication;
import lombok.extern.slf4j.Slf4j;
import ru.pudgy.vertex.model.entity.Account;
import ru.pudgy.vertex.model.entity.Schemata;

import java.security.Principal;
import java.util.Optional;

@Slf4j
public class SecurityHelper {
    public static final String ROLE_ATTRIBUTES = "userrole";
    public static final String NAME_ATTRIBUTES = "username";
    public static final String SCHEMA_ATTRIBUTES = "schema";
    public static final String ACCOUNT_ATTRIBUTES = "account";

    public static Optional<Schemata> currentSchema() {
        ObjectMapper mapper = new ObjectMapper();
        return  getAuthentication()
                .map(auth -> (String)auth.getAttributes().get(SCHEMA_ATTRIBUTES) )
                .map(json -> {
                    try {
                        return mapper.readValue(json, Schemata.class);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                });
    }

    public static Optional<Account> currentAccount() {
        ObjectMapper mapper = new ObjectMapper();
        return getAuthentication()
                .map(auth -> (String)auth.getAttributes().get(ACCOUNT_ATTRIBUTES) )
                .map(json -> {
                    try {
                        return mapper.readValue(json, Account.class);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                });
    }

    public static Optional<String> username() {
        return getAuthentication().map(Principal::getName);
    }

    private static Optional<Authentication> getAuthentication() {
        return ServerRequestContext.currentRequest().flatMap((request) -> {
            return request.getUserPrincipal(Authentication.class);
        });
    }
}
