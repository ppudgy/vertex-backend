package ru.pudgy.vertex.rest.ctrl.test;

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
import ru.pudgy.vertex.rest.dto.*;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FragmentTest extends AbstractBaseTest {

    @Inject
    ObjectMapper objectMapper;
    private static String TOKEN;
    private static UUID DOCUMENT;
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
    @Order(30)
    @DisplayName("create document fragment")
    void test031() {
        assertThat(TOKEN).isNotBlank();

        Page<DocumentDto> documents = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/document").bearerAuth(TOKEN),
                        Argument.of(Page.class, DocumentDto.class)
                );

        assertEquals(1, documents.getContent().size()); //)

        DocumentDto doc =  documents.getContent().get(0);
        DOCUMENT = doc.getId();
        FragmentNewDto newFragmentDto = new FragmentNewDto();
        newFragmentDto.setDocument(DOCUMENT);
        newFragmentDto.setName("test");
        newFragmentDto.setText(doc.getText());
        newFragmentDto.setStart(0);
        newFragmentDto.setEnd(doc.getText().length() - 1);

        FragmentDto fragmentDto = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT(String.format("/document/%s/fragment", DOCUMENT.toString()), newFragmentDto)
                                .bearerAuth(TOKEN),
                        FragmentDto.class
                );

        assertThat(fragmentDto).isNotNull();
        assertThat(fragmentDto.getText()).isEqualTo("one");
    }

    @Test
    @Order(40)
    @DisplayName("get document fragment")
    void test032() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();

        List<FragmentDto> fragments = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment", DOCUMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, FragmentDto.class)
                );

        assertThat(fragments)
                .isNotNull()
                .size().isEqualTo(1);

    }

    @Test
    @Order(50)
    @DisplayName("delete document fragment")
    void test033() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();

        List<FragmentDto> fragments = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment", DOCUMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, FragmentDto.class)
                );

        assertThat(fragments)
                .isNotNull()
                .size().isEqualTo(1);


        FragmentDto dto = fragments.get(0);


        client.toBlocking()
                .exchange(
                        HttpRequest.DELETE(String.format("/document/%s/fragment/%s", DOCUMENT.toString(), dto.getId().toString()))
                                .bearerAuth(TOKEN)
                );


        List<FragmentDto> newfragments = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment", DOCUMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, FragmentDto.class)
                );

        assertThat(newfragments)
                .isNotNull()
                .size().isEqualTo(0);

    }
}
