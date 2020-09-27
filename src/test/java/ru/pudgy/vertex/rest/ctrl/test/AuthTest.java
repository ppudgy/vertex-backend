package ru.pudgy.vertex.rest.ctrl.test;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;

import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class AuthTest extends AbstractBaseTest {
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
    void test02() {
        assertThat(TOKEN).isNotBlank();
        List<TopicDto> topics = client.toBlocking().retrieve(HttpRequest.GET("/topic").bearerAuth(TOKEN), Argument.listOf(TopicDto.class));
        assertEquals(0, topics.size()); //)
    }

    @Test
    void test03() {
        assertThat(TOKEN).isNotBlank();
        TopicNewDto topicNewDto = new TopicNewDto();
        topicNewDto.setName("test topic");
        TopicDto topic = client.toBlocking().retrieve(HttpRequest.PUT("/topic", topicNewDto).bearerAuth(TOKEN), TopicDto.class);
        assertThat(topic).isNotNull();
        assertThat(topic.getId()).isNotNull();
        assertThat(topic.getName()).isEqualTo("test topic");
    }

    @Test
    void test04() {
        assertThat(TOKEN).isNotBlank();
        List<TopicDto> topics = client.toBlocking().retrieve(HttpRequest.GET("/topic").bearerAuth(TOKEN), Argument.listOf(TopicDto.class));
        assertThat(topics).size().isEqualTo(1);
    }

}
