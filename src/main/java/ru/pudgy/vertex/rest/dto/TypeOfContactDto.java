package ru.pudgy.vertex.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TypeOfContactDto {
    private UUID id;
    private String name;
}
