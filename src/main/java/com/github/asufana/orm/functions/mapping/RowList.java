package com.github.asufana.orm.functions.mapping;

import java.util.*;
import java.util.stream.*;

import com.github.asufana.orm.functions.util.AbstractCollection;

public class RowList<T> extends AbstractCollection<RowList<T>, Row<T>> {
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final RowList EMPTY = new RowList(null);
    
    public RowList(final List<Row<T>> list) {
        super(list);
    }
    
    public List<T> toList() {
        return stream().map(row -> row.get()).collect(Collectors.toList());
    }
    
    @SuppressWarnings("unchecked")
    public Row<T> first() {
        return isEmpty()
                ? (Row<T>) Row.EMPTY
                : list().get(0);
    }
}
