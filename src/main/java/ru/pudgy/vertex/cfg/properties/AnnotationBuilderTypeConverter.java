package ru.pudgy.vertex.cfg.properties;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class AnnotationBuilderTypeConverter implements TypeConverter<String, AnnotationBuilder> {
    @Override
    public Optional<AnnotationBuilder> convert(String object, Class<AnnotationBuilder> targetType, ConversionContext context) {
        if(AnnotationBuilder.Glagol.name().equalsIgnoreCase(object))
            return Optional.of(AnnotationBuilder.Glagol);
        return Optional.of(AnnotationBuilder.Simple);
    }

    @Override
    public Optional<AnnotationBuilder> convert(String object, Class<AnnotationBuilder> targetType) {
        return convert(object, targetType, null);
    }
}
