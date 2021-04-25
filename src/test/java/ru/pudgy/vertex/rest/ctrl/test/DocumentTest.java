package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.*;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.DocumentDto;
import ru.pudgy.vertex.rest.dto.NoteDto;
import ru.pudgy.vertex.rest.dto.NoteNewDto;
import ru.pudgy.vertex.rest.dto.StatisticDto;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentTest extends AbstractBaseTest {

    @Inject
    ObjectMapper objectMapper;

    private static String TOKEN;

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

    @Test
    @Order(10)
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
    @Order(20)
    @DisplayName("list empty document list")
    void test02() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        String document = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/document?page=1&size=10&project=3d1520f7-87a5-4c31-ae07-b8774b1fc1f0&searchString=ads*")
                                .bearerAuth(TOKEN)
                );

        //Page<DocumentDto> documents = new ObjectMapper().readValue(document, new TypeReference<Page<DocumentDto>>() { });

        Page<DocumentDto> documents = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/document?page=1&size=10&project=3d1520f7-87a5-4c31-ae07-b8774b1fc1f0&searchString=ads*")
                                .bearerAuth(TOKEN),
                        Argument.of(Page.class, DocumentDto.class)
                );

        assertEquals(0, documents.getContent().size()); //)
    }

    @Test
    @Order(30)
    @DisplayName("create document")
    void test03() {
        assertThat(TOKEN).isNotBlank();

        NoteNewDto newNote = new NoteNewDto();
        newNote.setText("one");
        newNote.setPurpose(UUID.fromString("3d1520f7-87a5-4c31-ae07-b8774b1fc1f0"));
        NoteDto note = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/note", newNote)
                                .bearerAuth(TOKEN),
                        NoteDto.class
                );
        HttpResponse resp = client.toBlocking()
                .exchange(
                        HttpRequest.DELETE(String.format("/note/%s",note.getId()))
                                .bearerAuth(TOKEN)
                );

        assertEquals(HttpStatus.NO_CONTENT, resp.status());
    }

    @Test
    @Order(40)
    @DisplayName("get document list")
    void test031() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        Page<DocumentDto> documents = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/document").bearerAuth(TOKEN),
                        Argument.of(Page.class, DocumentDto.class)
                );

        assertEquals(1, documents.getContent().size()); //)
    }

    @Test
    @Order(50)
    @DisplayName("get document statistic")
    void test032() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        List<StatisticDto> statistics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/document/purpose/statistic").bearerAuth(TOKEN),
                        Argument.of(List.class, StatisticDto.class)
                );
        assertEquals(3, statistics.size()); //)
    }
}
