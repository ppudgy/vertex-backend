package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.*;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.rest.dto.TypeOfContactDto;
import ru.pudgy.vertex.rest.dto.error.EntityNotFoundErrorDto;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeOfContactTest extends AbstractBaseTest {

    private static EmbeddedServer server;
    private static HttpClient client;

    @BeforeAll
    public static void setupServer() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
    }

    @AfterAll
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static String TOKEN;

    @Test
    @Order(10)
    @DisplayName("not auth get type of contact list")
    void test001() {
        Assertions.assertThrows(Exception.class, () ->
            client.toBlocking()
                    .exchange(HttpRequest.GET("/typeofcontact"))
        );
    }

    @Test
    @Order(20)
    @DisplayName("not auth ext get type of contact list ")
    void test002() {
        Assertions.assertThrows(Exception.class, () ->
                client.toBlocking()
                    .exchange(HttpRequest.GET("/ext/typeofcontact"))
        );
    }

    @Test
    @Order(30)
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
    @Order(40)
    @DisplayName("burn get type of contact list")
    void test021() {
        assertThat(TOKEN).isNotBlank();

        List<TypeOfContactDto> typeOfContacts = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/typeofcontact").bearerAuth(TOKEN),
                        Argument.of(List.class, TypeOfContactDto.class)
                );

        assertEquals(10, typeOfContacts.size()); //)
    }

    @Test
    @Order(50)
    @DisplayName("get type of contact by id")
    void test022() {
        assertThat(TOKEN).isNotBlank();

        List<TypeOfContactDto> typeOfContacts = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/typeofcontact").bearerAuth(TOKEN),
                        Argument.of(List.class, TypeOfContactDto.class)
                );

        typeOfContacts.forEach(type -> {
                    TypeOfContactDto typeOfContact = client.toBlocking()
                        .retrieve(
                            HttpRequest.GET("/typeofcontact/" + type.getId().toString()).bearerAuth(TOKEN),
                            Argument.of(TypeOfContactDto.class)
                        );

                    assertThat(typeOfContact).isNotNull()
                            .isEqualTo(type);
                });
    }

    @Test
    @Order(60)
    @DisplayName("ext get type of contact list ")
    void test041() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        List<TypeOfContactDto> typeOfContacts = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/ext/typeofcontact").bearerAuth(TOKEN),
                        Argument.of(List.class, TypeOfContactDto.class)
                );

        assertEquals(10, typeOfContacts.size()); //)
    }

    @Test
    @Order(70)
    @DisplayName("get ext type of contact by id")
    void test042() {
        assertThat(TOKEN).isNotBlank();

        List<TypeOfContactDto> typeOfContacts = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/ext/typeofcontact").bearerAuth(TOKEN),
                        Argument.of(List.class, TypeOfContactDto.class)
                );

        typeOfContacts.forEach(type -> {
            TypeOfContactDto typeOfContact = client.toBlocking()
                    .retrieve(
                            HttpRequest.GET("/ext/typeofcontact/" + type.getId().toString()).bearerAuth(TOKEN),
                            Argument.of(TypeOfContactDto.class)
                    );

            assertThat(typeOfContact).isNotNull()
                    .isEqualTo(type);
        });
    }

    @Test
    @Order(80)
    @DisplayName("get ext type of contact by id")
    void test0422() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();
        UUID errId = UUID.randomUUID();
        String errorResult = objectMapper.writeValueAsString(EntityNotFoundErrorDto.of(errId, TypeOfContact.class.getName()));


        HttpClientResponseException ex = Assertions.assertThrows(HttpClientResponseException.class, () -> {
                EntityNotFoundErrorDto error = client.toBlocking()
                            .retrieve(
                                    HttpRequest.GET("/ext/typeofcontact/" + errId).bearerAuth(TOKEN),
                                    Argument.of(EntityNotFoundErrorDto.class)
                            );
            }
           );
        assertThat(ex).isNotNull();
        var response = ex.getResponse();
        assertThat(response).isNotNull();
        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        var msg = response.getBody(String.class);
        assertThat(msg).isNotEmpty().get().isEqualTo(errorResult);
    }





}
