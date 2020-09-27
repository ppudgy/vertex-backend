package ru.pudgy.vertex.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ContactNewDto {
    private UUID typeofcontact;
    private String contact;
    private String comment;
}
