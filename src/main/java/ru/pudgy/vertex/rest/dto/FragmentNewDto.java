package ru.pudgy.vertex.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class FragmentNewDto {
    private String name;
    private UUID document;
    private String text;
    private int start;
    private int end;
}
