package ru.pudgy.vertex.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class FragmentPersonKey implements Serializable{
    private UUID fragment;
    private UUID person;

    public FragmentPersonKey() {
    }

    public FragmentPersonKey(UUID fragment, UUID person) {
        this.fragment = fragment;
        this.person = person;
    }

    public UUID getFragment() {
        return fragment;
    }

    public void setFragment(UUID fragment) {
        this.fragment = fragment;
    }

    public UUID getPerson() {
        return person;
    }

    public void setPerson(UUID person) {
        this.person = person;
    }
}
