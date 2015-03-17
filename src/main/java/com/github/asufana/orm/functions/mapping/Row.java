package com.github.asufana.orm.functions.mapping;

import java.util.*;
import java.util.function.*;

import lombok.*;

import com.github.asufana.orm.*;
import com.github.asufana.orm.functions.util.*;

@Getter
public class Row<T> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final Row EMPTY = new Row(null, null);
    
    private final EntityManager<T> em;
    private final T instance;
    
    public Row(final EntityManager<T> em, final T instance) {
        this.em = em;
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
    
    private String pkColumnName() {
        return PrimaryKeyFunction.pkColumnName(em.connection(), em.tableName());
    }
    
    //-----------------------------------------
    
    public Integer delete() {
        return where().delete();
    }
    
    public EntityManager<T> values(final Map<String, String> values) {
        return where().values(values);
    }
    
    private EntityManager<T> where() {
        final String pkColumnName = pkColumnName();
        final Object pkColumnValue = PrimaryKeyFunction.pkColumnValue(pkColumnName,
                                                                      instance);
        return em.where(String.format("%s=?", pkColumnName),
                        Arrays.asList(pkColumnValue));
    }
    
}
