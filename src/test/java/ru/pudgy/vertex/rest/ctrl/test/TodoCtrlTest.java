package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.*;
import ru.pudgy.vertex.AbstractBaseTest;
import ru.pudgy.vertex.TestDataUtil;
import ru.pudgy.vertex.rest.dto.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TodoCtrlTest extends AbstractBaseTest {

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
    @DisplayName("list empty todo list")
    void test02() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();
        Page<TodoDto> todos = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/todo?page=1&size=10&project=3d1520f7-87a5-4c31-ae07-b8774b1fc1f0&searchString=ads*")
                                .bearerAuth(TOKEN),
                        Argument.of(Page.class, TodoDto.class)
                );

        assertEquals(0, todos.getContent().size()); //)
    }

    @Test
    @Order(30)
    @DisplayName("create todo")
    void test03() {
        assertThat(TOKEN).isNotBlank();

        TodoNewDto newTodo = new TodoNewDto();
        newTodo.setDescription("one");
        newTodo.setPurpose(UUID.fromString("3d1520f7-87a5-4c31-ae07-b8774b1fc1f0"));
        newTodo.setAuto(0);
        newTodo.setExternal(false);
        newTodo.setAutoperiod(0);
        newTodo.setRelevance(ZonedDateTime.now());
        TodoDto todo = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/todo", newTodo)
                                .bearerAuth(TOKEN),
                        TodoDto.class
                );

        assertEquals("one", todo.getDescription());
        assertNotNull(todo.getId());
        assertEquals("home", todo.getPurposeName());
        assertEquals("green", todo.getPurposeColor());
    }

    @Test
    @Order(40)
    @DisplayName("get todo list")
    void test031() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

       Page<TodoDto> todos = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/todo").bearerAuth(TOKEN),
                        Argument.of(Page.class, TodoDto.class)
                );

        assertEquals(1, todos.getContent().size()); //)
    }

    @Test
    @Order(50)
    @DisplayName("get todo statistic")
    void test032() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        List<StatisticDto> statistics = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/todo/purpose/statistic").bearerAuth(TOKEN),
                        Argument.of(List.class, StatisticDto.class)
                );

        assertEquals(3, statistics.size()); //)
    }
}
