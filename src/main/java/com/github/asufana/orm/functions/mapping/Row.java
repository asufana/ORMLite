package com.github.asufana.orm.functions.mapping;

import java.util.*;
import java.util.function.*;

import lombok.*;

@Getter
public class Row<T> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Row EMPTY = new Row(null);
    
    private final T instance;
    
    public Row(final T instance) {
        this.instance = instance;
    }
    
    public T get() {
        return instance;
    }
    
    public boolean isPresent() {
        return instance != null;
    }
    
    public T orElse(final T other) {
        return Optional.ofNullable(instance).orElse(other);
    }
    
    public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exception) throws X {
        return Optional.ofNullable(instance).orElseThrow(exception);
    }
}
