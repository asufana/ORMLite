package com.github.asufana.orm.functions.mapping;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import lombok.*;

import com.github.asufana.orm.*;
import com.github.asufana.orm.exceptions.*;
import com.github.asufana.orm.functions.inspection.*;
import com.github.asufana.orm.functions.query.*;

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
    
    public Integer delete() {
        return Query.execute(em.connection(),
                             String.format("DELETE FROM %s WHERE %s=?",
                                           em.tableName(),
                                           pkColumnName()),
                             Arrays.asList(pkFieldValue()));
    }
    
    private String pkColumnName() {
        return Inspection.pkColumns(em.connection(), em.tableName())
                         .get(0)
                         .get()
                         .columnName();
    }
    
    private Object pkFieldValue() {
        try {
            final String pkColumnName = pkColumnName();
            final Field field = Arrays.asList(em.targetClass()
                                                .getDeclaredFields())
                                      .stream()
                                      .filter(f -> f.getName()
                                                    .toLowerCase()
                                                    .equals(pkColumnName.toLowerCase()))
                                      .findFirst()
                                      .orElseThrow(() -> new ORMLiteException(String.format("Can't find PK Field on Target Class. Target Class:%s, PK Field:%s",
                                                                                            em.targetClass()
                                                                                              .getSimpleName(),
                                                                                            pkColumnName)));
            field.setAccessible(true);
            final Object pkFieldValue = field.get(instance);
            if (pkFieldValue == null) {
                throw new ORMLiteException(String.format("Can't get PK Field value on Target Class. Target Class:%s, PK Field:%s",
                                                         em.targetClass()
                                                           .getSimpleName(),
                                                         field.getName()));
            }
            return pkFieldValue;
        }
        catch (IllegalArgumentException | IllegalAccessException
                | SecurityException e) {
            throw new ORMLiteException(e);
        }
    }
}
