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
public class FragmentTopicTest extends AbstractBaseTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    ObjectMapper objectMapper;

    private static String TOKEN;

    private static UUID DOCUMENT;

    private static UUID FRAGMENT;

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
        FRAGMENT = fragmentDto.getId();
    }

    @Test
    @DisplayName("add document fragment topic")
    void test032() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();
        assertThat(FRAGMENT).isNotNull();

        TopicNewDto newTopic = new TopicNewDto();
        newTopic.setName("Topic.Test");

        TopicDto topic = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/topic", newTopic).bearerAuth(TOKEN),
                        TopicDto.class
                );

        assertThat(topic).isNotNull();
        assertThat(topic.getName()).isEqualTo("Topic.Test");


        TopicNewDto newTopicNo = new TopicNewDto();
        newTopicNo.setName("Topic.No");

        TopicDto topicNo = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/topic", newTopicNo).bearerAuth(TOKEN),
                        TopicDto.class
                );

        assertThat(topicNo).isNotNull();
        assertThat(topicNo.getName()).isEqualTo("Topic.No");




        TopicDto addedTopic = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()), topic)
                                .bearerAuth(TOKEN),
                        TopicDto.class
                );

        assertThat(addedTopic).isNotNull();
        assertThat(addedTopic.getName()).isEqualTo("Topic.Test");

    }

    @Test
    @DisplayName("search fragment topic")
    void test033() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();

        List<TopicDto> topics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topics).isNotNull().size().isEqualTo(1);
        TopicDto topic = topics.get(0);


        List<TopicDto> topicsearche = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic?searchString=No", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topicsearche).isNotNull().size().isEqualTo(2);
    }

    @Test
    @DisplayName("update fragment topic")
    void test034() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();

        List<TopicDto> topics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topics).isNotNull().size().isEqualTo(1);
        TopicDto topic = topics.get(0);
        assertThat(topic.getChecked()).isTrue();

        topic.setChecked(false);
        TopicDto topic1 = client.toBlocking()
                .retrieve(
                        HttpRequest.POST(String.format("/document/%s/fragment/%s/topic/%s", DOCUMENT.toString(), FRAGMENT.toString(), topic.getId()), topic)
                                .bearerAuth(TOKEN),
                        TopicDto.class
                );
        assertThat(topic1).isNotNull();
        assertThat(topic1.getChecked()).isFalse();


        topics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topics).isNotNull().size().isEqualTo(0);


        topic.setChecked(true);
        TopicDto topic2 = client.toBlocking()
                .retrieve(
                        HttpRequest.POST(String.format("/document/%s/fragment/%s/topic/%s", DOCUMENT.toString(), FRAGMENT.toString(), topic.getId()), topic)
                                .bearerAuth(TOKEN),
                        TopicDto.class
                );
        assertThat(topic2).isNotNull();
        assertThat(topic2.getChecked()).isTrue();

        topics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topics).isNotNull().size().isEqualTo(1);


    }

    @Test
    @DisplayName("list topic fragments")
    void test08() {
        List<TopicDto> topicList = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/topic"))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topicList).isNotNull().size().isEqualTo(2);

        TopicDto dto = topicList.stream()
                .filter(topic -> topic.getName().equalsIgnoreCase("Topic.Test"))
                .findFirst().get();

        List<FragmentDto> fragments = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/topic/%s/fragment", dto.getId()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, FragmentDto.class)
                );

        assertThat(fragments)
                .isNotNull()
                .size().isEqualTo(1);


    }

    @Test
    @DisplayName("delete document fragment")
    void test09() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();


        List<TopicDto> topics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(topics).isNotNull().size().isEqualTo(1);
        TopicDto topic = topics.get(0);

        client.toBlocking()
                .exchange(
                        HttpRequest.DELETE(String.format("/document/%s/fragment/%s/topic/%s", DOCUMENT.toString(), FRAGMENT.toString(), topic.getId().toString()))
                                .bearerAuth(TOKEN)
                );

        List<TopicDto> dtopics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/topic", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, TopicDto.class)
                );

        assertThat(dtopics)
                .isNotNull()
                .size().isEqualTo(0);

    }
}
