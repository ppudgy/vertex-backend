package ru.pudgy.vertex.rest.dto.error;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ErrorDto {
    private final int code;
    private final String msg;
}
