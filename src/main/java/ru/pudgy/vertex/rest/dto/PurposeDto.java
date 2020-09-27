package ru.pudgy.vertex.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PurposeDto {
    private UUID id;
    private String name;
    private String color;
    private Boolean active;
}
