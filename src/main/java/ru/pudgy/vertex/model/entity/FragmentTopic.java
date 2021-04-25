package ru.pudgy.vertex.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fragmenttopic")
public class FragmentTopic {
    @EmbeddedId
    private FragmentTopicKey key;

    public FragmentTopic() {
    }

    public FragmentTopic(FragmentTopicKey key) {
        this.key = key;
    }

    public FragmentTopicKey getKey() {
        return key;
    }

    public void setKey(FragmentTopicKey key) {
        this.key = key;
    }
}


