package ru.pudgy.vertex.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class FragmentTopicKey  implements Serializable {
    private UUID fragment;
    private UUID topic;

    public FragmentTopicKey() {
    }

    public FragmentTopicKey(UUID fragment, UUID topic) {
        this.fragment = fragment;
        this.topic = topic;
    }

    public UUID getFragment() {
        return fragment;
    }

    public void setFragment(UUID fragment) {
        this.fragment = fragment;
    }

    public UUID getTopic() {
        return topic;
    }

    public void setTopic(UUID topic) {
        this.topic = topic;
    }
}
