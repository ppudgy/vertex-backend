package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.*;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.PurposeDto;
import ru.pudgy.vertex.rest.dto.PurposeNewDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PurposeCtrlTest extends AbstractBaseTest {

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

    private static String TOKEN;

    @Test
    @Order(10)
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
    @Order(20)
    @DisplayName("list empty purpose list")
    void test02() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();
        List<PurposeDto> purposes = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/purpose")
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PurposeDto.class)
                );

        assertEquals(3, purposes.size()); //)
    }

    @Test
    @Order(30)
    @DisplayName("create purpose")
    void test03() {
        assertThat(TOKEN).isNotBlank();

        PurposeNewDto newPurpose = new PurposeNewDto();
        newPurpose.setName("test-purpose");
        newPurpose.setColor("red");
        PurposeDto purpose = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/purpose", newPurpose)
                                .bearerAuth(TOKEN),
                         PurposeDto.class
                );

        assertEquals("test-purpose", purpose.getName());
        assertNotNull(purpose.getId());
    }

    @Test
    @Order(40)
    @DisplayName("get purpose list")
    void test031() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        List<PurposeDto> purposes = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/purpose").bearerAuth(TOKEN),
                        Argument.of(List.class, PurposeDto.class)
                );

        assertEquals(4, purposes.size()); //)
    }

}
