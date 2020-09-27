package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.*;
import ru.pudgy.vertex.util.RestResponsePage;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class FragmentTest extends AbstractBaseTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    ObjectMapper objectMapper;

    private static String TOKEN;

    private static UUID DOCUMENT;

    @Test
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
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
    @DisplayName("create document fragment")
    void test031() {
        assertThat(TOKEN).isNotBlank();

        RestResponsePage<DocumentDto> documents = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/document").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, DocumentDto.class)
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
