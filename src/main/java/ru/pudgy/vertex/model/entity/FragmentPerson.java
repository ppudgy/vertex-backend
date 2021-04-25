package ru.pudgy.vertex.model.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fragmentperson")
public class FragmentPerson {
    @EmbeddedId
    private FragmentPersonKey key;

    public FragmentPerson() {
    }

    public FragmentPerson(FragmentPersonKey key) {
        this.key = key;
    }

    public FragmentPersonKey getKey() {
        return key;
    }

    public void setKey(FragmentPersonKey key) {
        this.key = key;
    }
}


