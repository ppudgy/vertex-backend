package ru.pudgy.vertex.srvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pudgy.text.MorfoAnalyzer;
import ru.pudgy.text.utils.freeling.FreeLing;
import ru.pudgy.vertex.cfg.properties.TextProperty;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class TextService {

    private final TextProperty property;

    private final static String ANNOTATION_ERROR_STRING = "Fail to create annotation";

    public Optional<String> formatSearchString(String search_string){
        return Optional.ofNullable(search_string)
                .map(str -> {
                    String ls = search_string.trim();
                    if(ls.length() > 0){
                        StringBuilder builder = new StringBuilder();
                        builder.append('%');
                        String [] ss = ls.split("\\s");
                        for(String s : ss){
                            if(s.length() > 0)
                                builder.append(s).append('%');
                        }
                        return builder.toString();
                    }
                    return null;
                });
    }

    public String createAnnotation(String text) {
        if(text == null || text.isBlank())
            return ANNOTATION_ERROR_STRING;
        return switch (property.getAnnotationBuilder()) {
            case Glagol -> createAnnotationGlagol(text);
            case Simple -> createAnnotationSimple(text);
        };
    }

    private String createAnnotationGlagol(String text) {
        try {
            var document = MorfoAnalyzer.analyze(FreeLing.createPudgyRu(), text);
            document.analyze();
            return document.getAnotation();
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return ANNOTATION_ERROR_STRING;
        }
    }

    private String createAnnotationSimple(String text) {
        String tmp = filterHtmlString(text);
        String [] words = tmp.split(" ", 40);
        int len = words.length;
        if(len == 40)
            words[len - 1] = "";
        return String.join(" ", words);
    }

    private static String filterHtmlString(String search_string) {
        String lss = "";
        if(search_string != null){
            String tmp_s = search_string.replaceAll("\\<.*?>", "");
            lss = tmp_s.trim();
        }
        return lss;
    }
}
