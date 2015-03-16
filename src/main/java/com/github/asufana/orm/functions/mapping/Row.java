package com.github.asufana.orm.functions.mapping;

import java.util.*;
import java.util.function.*;

import lombok.*;

import com.github.asufana.orm.*;
import com.github.asufana.orm.functions.query.*;
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
    
    //-----------------------------------------
    
    public Integer delete() {
        final String pkColumnName = PrimaryKeyFunction.pkColumnName(em.connection(),
                                                                    em.tableName());
        return Query.execute(em.connection(),
                             String.format("DELETE FROM %s WHERE %s=?",
                                           em.tableName(),
                                           pkColumnName),
                             Arrays.asList(PrimaryKeyFunction.pkColumnValue(pkColumnName,
                                                                           instance)));
    }
}
