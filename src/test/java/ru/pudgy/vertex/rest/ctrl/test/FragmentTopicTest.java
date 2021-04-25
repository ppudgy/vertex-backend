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

public class FragmentTopicTest extends AbstractBaseTest {

    @Inject
    ObjectMapper objectMapper;
    private static String TOKEN;
    private static UUID DOCUMENT;
    private static UUID FRAGMENT;
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
    @Order(5)
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
    @Order(10)
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
    @Order(20)
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
        FRAGMENT = fragmentDto.getId();
    }

    @Test
    @Order(30)
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
    @Order(40)
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
    @Order(50)
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
    @Order(60)
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
    @Order(70)
    @DisplayName("delete document fragment")
    void test09() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();
        assertThat(FRAGMENT).isNotNull();


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
