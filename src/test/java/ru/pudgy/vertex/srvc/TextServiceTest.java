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
        var res = textService.formatSearchString("");
        assertThat(res).isEmpty();
    }

    @Test
    void testFormatNullSearchString() {
        var res = textService.formatSearchString(null);
        assertThat(res).isEmpty();
    }
    @Test
    void testFormatOneSpaceSearchString() {
        var res = textService.formatSearchString(" ");
        assertThat(res).isEmpty();
    }

    @Test
    void testFormatOneWorldSearchString() {
        var res = textService.formatSearchString("one");
        assertThat(res).isPresent()
                .get().isEqualTo("%one%");
    }

    @Test
    void testFormatTwoWorldSearchString() {
        var res = textService.formatSearchString("one two");
        assertThat(res).isPresent()
                .get().isEqualTo("%one%two%");
    }

    @Test
    void testFormatTwoWorldWithSpaceSearchString() {
        var res = textService.formatSearchString("  one    two      ");
        assertThat(res).isPresent()
                .get().isEqualTo("%one%two%");
    }

    @Test
    void testAnnotationNull() {
        var res = textService.createAnnotation(null);
        assertThat(res).isNotNull().isEqualTo("Fail to create annotation");
    }

    @Test
    void testAnnotationEmpty() {
        var res = textService.createAnnotation("");
        assertThat(res).isNotNull().isEqualTo("Fail to create annotation");
        var res1 = textService.createAnnotation(" ");
        assertThat(res1).isNotNull().isEqualTo("Fail to create annotation");

    }

    @Test
    void testAnnotationOneWorld() {
        var res = textService.createAnnotation("Саша");
        assertThat(res).isNotNull().isEqualTo("Саша");
    }

    @Test
    void testAnnotationShort() {
        var res = textService.createAnnotation("Саша мыла раму");
        assertThat(res).isNotNull().isEqualTo("Саша мыла раму");
    }

    @Test
    void testAnnotationLong() {
        var res = textService.createAnnotation("Саша тихо мыла раму.");
        assertThat(res).isNotNull().isEqualTo("Саша тихо мыла раму.");
    }
}
