package ru.pudgy.vertex.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class NoteNewDto {
    private String text;
    private UUID purpose;
}
