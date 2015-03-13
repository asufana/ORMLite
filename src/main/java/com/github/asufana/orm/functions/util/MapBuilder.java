package com.github.asufana.orm.functions.util;

import java.util.*;

public class MapBuilder<K, V> {
    
    private final Map<K, V> map = new HashMap<>();
    
    public MapBuilder<K, V> put(final K key, final V value) {
        map.put(key, value);
        return this;
    }
    
    public Map<K, V> build() {
        return map;
    }
    
}
