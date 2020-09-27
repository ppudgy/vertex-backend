package ru.pudgy.vertex.rest.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ErrorDto {
    private String Message;
}
