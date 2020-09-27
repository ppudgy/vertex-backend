package ru.pudgy.vertex.rest.ctrl.test;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class FragmentPersonTest extends AbstractBaseTest {
    @Inject
    EmbeddedServer server;
    @Inject
    @Client("/")
    HttpClient client;

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
    @DisplayName("add document fragment person")
    void test032() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();
        assertThat(FRAGMENT).isNotNull();

        PersonNewDto newPerson = new PersonNewDto();
        newPerson.setName("Иван");
        newPerson.setSername("Иванович");
        newPerson.setFamily("Иванов");
        newPerson.setBirthday(LocalDate.now());
        newPerson.setSex("men");

        PersonDto person = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/person", newPerson).bearerAuth(TOKEN),
                        PersonDto.class
                );

        assertThat(person).isNotNull();
        assertThat(person.getName()).isEqualTo("Иван");


        PersonNewDto newPersonNo = new PersonNewDto();
        newPersonNo.setName("Семен");
        newPersonNo.setSername("Семенович");
        newPersonNo.setFamily("Семенов");
        newPersonNo.setBirthday(LocalDate.now());
        newPersonNo.setSex("men");

        PersonDto personNo = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT("/person", newPersonNo).bearerAuth(TOKEN),
                        PersonDto.class
                );

        assertThat(personNo).isNotNull();
        assertThat(personNo.getName()).isEqualTo("Семен");




        PersonDto addedPerson = client.toBlocking()
                .retrieve(
                        HttpRequest.PUT(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()), person)
                                .bearerAuth(TOKEN),
                        PersonDto.class
                );

        assertThat(addedPerson).isNotNull();
        assertThat(addedPerson.getName()).isEqualTo("Иван");

    }

    @Test
    @DisplayName("search fragment person")
    void test033() throws UnsupportedEncodingException {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();

        List<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(persons).isNotNull().size().isEqualTo(1);
        PersonDto person = persons.get(0);


        List<PersonDto> personsearche = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person?searchString=%s", DOCUMENT.toString(), FRAGMENT.toString(),URLEncoder.encode("семен", "UTF-8")))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(personsearche).isNotNull().size().isEqualTo(2);
    }

    @Test
    @DisplayName("update fragment person")
    void test034() {
        assertThat(TOKEN).isNotBlank();
        assertThat(DOCUMENT).isNotNull();

        List<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(persons).isNotNull().size().isEqualTo(1);
        PersonDto person = persons.get(0);
        assertThat(person.getChecked()).isTrue();

        person.setChecked(false);
        PersonDto person1 = client.toBlocking()
                .retrieve(
                        HttpRequest.POST(String.format("/document/%s/fragment/%s/person/%s", DOCUMENT.toString(), FRAGMENT.toString(), person.getId()), person)
                                .bearerAuth(TOKEN),
                        PersonDto.class
                );
        assertThat(person1).isNotNull();
        assertThat(person1.getChecked()).isFalse();


        persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(persons).isNotNull().size().isEqualTo(0);


        person.setChecked(true);
        PersonDto person2 = client.toBlocking()
                .retrieve(
                        HttpRequest.POST(String.format("/document/%s/fragment/%s/person/%s", DOCUMENT.toString(), FRAGMENT.toString(), person.getId()), person)
                                .bearerAuth(TOKEN),
                        PersonDto.class
                );
        assertThat(person2).isNotNull();
        assertThat(person2.getChecked()).isTrue();

        persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(persons).isNotNull().size().isEqualTo(1);
    }

    @Test
    @DisplayName("list person fragments")
    void test08() {
        RestResponsePage<PersonDto> personList = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/person"))
                                .bearerAuth(TOKEN),
                        Argument.of(RestResponsePage.class, PersonDto.class)
                );

        assertThat(personList.getContent()).isNotNull().size().isEqualTo(2);

        PersonDto dto = personList.getContent().stream()
                .filter(person -> person.getName().equalsIgnoreCase("Иван"))
                .findFirst().get();

        List<FragmentDto> fragments = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/person/%s/fragment", dto.getId()))
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


        List<PersonDto> persons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(persons).isNotNull().size().isEqualTo(1);
        PersonDto person = persons.get(0);

        client.toBlocking()
                .exchange(
                        HttpRequest.DELETE(String.format("/document/%s/fragment/%s/person/%s", DOCUMENT.toString(), FRAGMENT.toString(), person.getId().toString()))
                                .bearerAuth(TOKEN)
                );

        List<PersonDto> dpersons = client.toBlocking()
                .retrieve(
                        HttpRequest.GET(String.format("/document/%s/fragment/%s/person", DOCUMENT.toString(), FRAGMENT.toString()))
                                .bearerAuth(TOKEN),
                        Argument.of(List.class, PersonDto.class)
                );

        assertThat(dpersons)
                .isNotNull()
                .size().isEqualTo(0);

    }
}
