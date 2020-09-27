package ru.pudgy.vertex.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Pair<K, V> {
    private final K key;
    private final V value;
}
