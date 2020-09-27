package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.PurposeDto;
import ru.pudgy.vertex.rest.dto.PurposeNewDto;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class PurposeCtrlTest extends AbstractBaseTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    private static String TOKEN;

    @Test
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
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
