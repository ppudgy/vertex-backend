package ru.pudgy.vertex.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import lombok.Data;

import java.util.Collections;
import java.util.List;


@Data
@Deprecated
public class RestResponsePage<T> implements Page<T> {
    private List<T> content;
    Long totalElements;
    int totalPages;
    int number;
    int size;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestResponsePage(@JsonProperty("content") List<T> content,
                            @JsonProperty("pageNumber") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalSize") Long totalElements,
                            //@JsonProperty("pageable") JsonNode pageable,
                            //@JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages ) {
        this.content = content == null ? Collections.emptyList() : content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
    }

    public RestResponsePage(List<T> content, Pageable pageable, long total) {
        this.content = content == null ? Collections.emptyList() : content;
        this.totalElements = total;
        this.number = pageable.getNumber();
        this.size = pageable.getSize();
    }

    public RestResponsePage(List<T> content) {
        this.content = content == null ? Collections.emptyList() : content;
        this.totalElements = 0L;
        this.number = 0;
        this.size = 0;
    }

    public RestResponsePage() {
        this.content = Collections.emptyList();
        this.totalElements = 0L;
        this.number = 0;
        this.size = 0;
    }

    @Override
    public long getTotalSize() {
        return totalElements;
    }

    @NonNull
    @Override
    public Pageable getPageable() {
        return Pageable.from(number, size);
    }
}
