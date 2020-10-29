package ru.pudgy.vertex.cfg.properties;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@ConfigurationProperties("text")
@RequiredArgsConstructor
public class TextProperty {
    private AnnotationBuilder annotationBuilder;
}
