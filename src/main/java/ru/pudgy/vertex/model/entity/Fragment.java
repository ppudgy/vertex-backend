package ru.pudgy.vertex.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
public class Fragment {
    @Id
    private UUID          id;         // Сурогатный ключ
    private ZonedDateTime origin;     // Дата и время внесения информации в банк данных

    private UUID          schemata;     // Схема пользователя
    private String        name;       // Имя фрагмента
    private UUID          document;   // Документ частью которого является фрагмент
    private String        text;       // текст фрагмента документа
    private int 		  posstart;		// начало фрагмента в документе
    private int 		  posend;		// окончание фрагмента в документе
}
