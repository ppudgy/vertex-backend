package ru.pudgy.vertex.rest.ctrl.test;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.TopicDto;
import ru.pudgy.vertex.rest.dto.TopicNewDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AuthTest extends AbstractBaseTest {
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
    void test02() {
        assertThat(TOKEN).isNotBlank();
        List<TopicDto> topics = client.toBlocking().retrieve(HttpRequest.GET("/topic").bearerAuth(TOKEN), Argument.listOf(TopicDto.class));
        assertEquals(0, topics.size()); //)
    }

    @Test
    @Order(30)
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
    @Order(40)
    void test04() {
        assertThat(TOKEN).isNotBlank();
        List<TopicDto> topics = client.toBlocking().retrieve(HttpRequest.GET("/topic").bearerAuth(TOKEN), Argument.listOf(TopicDto.class));
        assertThat(topics).size().isEqualTo(1);
    }
}
