package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
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
import ru.pudgy.vertex.rest.dto.NoteDto;
import ru.pudgy.vertex.rest.dto.NoteNewDto;
import ru.pudgy.vertex.rest.dto.StatisticDto;
import ru.pudgy.vertex.util.RestResponsePage;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class NoteCtrlTest extends AbstractBaseTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    ObjectMapper objectMapper;

    private static String TOKEN;

    @Test
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
    @DisplayName("list empty note list")
    void test02() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();
        Page<NoteDto> notes = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/note?page=1&size=10&project=3d1520f7-87a5-4c31-ae07-b8774b1fc1f0&searchString=ads*")
                                .bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, NoteDto.class)
                );

        assertEquals(0, notes.getContent().size()); //)
    }

    @Test
    @DisplayName("create note")
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

        assertEquals("one", note.getText());
        assertNotNull(note.getId());
        assertEquals("home", note.getPurposeName());
        assertEquals("green", note.getPurposeColor());
    }
    @Test
    @DisplayName("get note list")
    void test031() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        RestResponsePage<NoteDto> notes = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/note").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, NoteDto.class)
                );

        assertEquals(1, notes.getContent().size()); //)
    }

    @Test
    @DisplayName("get note statistic")
    void test032() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        List<StatisticDto> statistics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/note/purpose/statistic").bearerAuth(TOKEN),
                        Argument.of(List.class, StatisticDto.class)
                );

        assertEquals(3, statistics.size()); //)
    }


}
