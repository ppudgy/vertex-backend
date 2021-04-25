package ru.pudgy.vertex.srvc;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.pudgy.vertex.AbstractBaseTest;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class TextServiceTest  extends AbstractBaseTest {

    @Inject
    private TextService textService;

    @Test
    void testFormatEmptySearchString() {
        String res = textService.formatSearchString("");
        assertThat(res).isNull();
    }

    @Test
    void testFormatNullSearchString() {
        String res = textService.formatSearchString(null);
        assertThat(res).isNull();
    }
    @Test
    void testFormatOneSpaceSearchString() {
        String res = textService.formatSearchString(" ");
        assertThat(res).isNull();
    }

    @Test
    void testFormatOneWorldSearchString() {
        String res = textService.formatSearchString("one");
        assertThat(res).isNotNull().isEqualTo("%one%");
    }

    @Test
    void testFormatTwoWorldSearchString() {
        String res = textService.formatSearchString("one two");
        assertThat(res).isNotNull().isEqualTo("%one%two%");
    }

    @Test
    void testFormatTwoWorldWithSpaceSearchString() {
        String res = textService.formatSearchString("  one    two      ");
        assertThat(res).isNotNull().isEqualTo("%one%two%");
    }

    @Test
    void testAnnotationNull() {
        String res = textService.createAnnotation(null);
        assertThat(res).isNotNull().isEqualTo("Fail to create annotation");
    }

    @Test
    void testAnnotationEmpty() {
        String res = textService.createAnnotation("");
        assertThat(res).isNotNull().isEqualTo("Fail to create annotation");
        String res1 = textService.createAnnotation(" ");
        assertThat(res1).isNotNull().isEqualTo("Fail to create annotation");

    }

    @Test
    void testAnnotationOneWorld() {
        String res = textService.createAnnotation("Саша");
        assertThat(res).isNotNull().isEqualTo("Саша");
    }

    @Test
    void testAnnotationShort() {
        String res = textService.createAnnotation("Саша мыла раму");
        assertThat(res).isNotNull().isEqualTo("Саша мыла раму");
    }

    @Test
    void testAnnotationLong() {
        String res = textService.createAnnotation("Саша тихо мыла раму.");
        assertThat(res).isNotNull().isEqualTo("Саша тихо мыла раму.");
    }
}
