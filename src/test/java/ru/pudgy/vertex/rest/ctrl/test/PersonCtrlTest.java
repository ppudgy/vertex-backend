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
import ru.pudgy.vertex.rest.dto.PersonDto;
import ru.pudgy.vertex.rest.dto.PersonNewDto;
import ru.pudgy.vertex.util.RestResponsePage;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class PersonCtrlTest extends AbstractBaseTest {

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
    @DisplayName("list empty person list")
    void test02() throws JsonProcessingException {
        assertThat(TOKEN).isNotBlank();
        Page<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/person")
                                .bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, PersonDto.class)
                );

        assertEquals(0, persons.getContent().size()); //)
    }

    @Test
    @DisplayName("create person")
    void test03() {
        assertThat(TOKEN).isNotBlank();

        PersonNewDto newPerson = new PersonNewDto();
        newPerson.setName("Иван");
        newPerson.setSername("Иванович");
        newPerson.setFamily("Иванов");
        newPerson.setBirthday(LocalDate.now());
        newPerson.setSex("men");

        PersonDto person = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/person", newPerson)
                                .bearerAuth(TOKEN),
                         PersonDto.class
                );

        assertEquals("Иванов", person.getFamily());
        assertNotNull(person.getId());
    }

    @Test
    @DisplayName("get person list")
    void test031(){
        assertThat(TOKEN).isNotBlank();

        String str = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/person").bearerAuth(TOKEN), String.class  );


        Page<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/person").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, PersonDto.class)
                );

        assertEquals(1, persons.getContent().size()); //)
    }

    @Test
    @DisplayName("update person list")
    void test032(){
        assertThat(TOKEN).isNotBlank();

        Page<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/person").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, PersonDto.class)
                );

        assertEquals(1, persons.getContent().size());

        PersonDto personDto = persons.getContent().get(0);

        personDto.setFamily("Петров");

        PersonDto uperson = client.toBlocking()
                .retrieve(
                        HttpRequest.POST(String.format("/person/%s",personDto.getId().toString()), personDto).bearerAuth(TOKEN),
                        PersonDto.class
                );

        assertThat(uperson.getFamily()).isEqualTo("Петров");
    }

    @Test
    @DisplayName("delete person")
    void test09(){
        assertThat(TOKEN).isNotBlank();

        Page<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/person").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, PersonDto.class)
                );

        assertEquals(1, persons.getContent().size());
        PersonDto personDto = persons.getContent().get(0);
        assertThat(personDto.getFamily()).isEqualTo("Петров");

        client.toBlocking()
                .exchange(
                        HttpRequest.DELETE(String.format("/person/%s",personDto.getId().toString())).bearerAuth(TOKEN)
                );

        persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET("/person").bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, PersonDto.class)
                );

        assertEquals(0, persons.getContent().size());

    }
}
