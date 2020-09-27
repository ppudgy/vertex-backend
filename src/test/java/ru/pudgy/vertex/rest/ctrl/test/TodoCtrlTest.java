package ru.pudgy.vertex.rest.ctrl.test;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.pudgy.vertex.rest.dto.*;
import ru.pudgy.vertex.util.RestResponsePage;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class TodoCtrlTest extends AbstractBaseTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

//    @Inject
//    ObjectMapper objectMapper;

    private static String TOKEN;

    @Test
    void test01() {
        BearerAccessRefreshToken response = client.toBlocking()
                .retrieve(HttpRequest.POST("/login", TestDataUtil.authPayload()), BearerAccessRefreshToken.class);
        TOKEN = response.getAccessToken();
    }

    @Test
    @DisplayName("list empty todo list")
    void test02() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();
        Page<TodoDto> todos = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/todo?page=1&size=10&project=3d1520f7-87a5-4c31-ae07-b8774b1fc1f0&searchString=ads*")
                                .bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, TodoDto.class)
                );

        assertEquals(0, todos.getContent().size()); //)
    }

    @Test
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
    @DisplayName("get todo list")
    void test031() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();

        RestResponsePage<TodoDto> todos = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/todo").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, TodoDto.class)
                );

        assertEquals(1, todos.getContent().size()); //)
    }

    @Test
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
