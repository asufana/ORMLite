package com.github.asufana.orm.functions.inspection.resources;

import java.util.*;

import com.github.asufana.orm.functions.util.AbstractCollection;

public class TableList extends AbstractCollection<TableList, Table> {
    
    public static final TableList EMPTY = new TableList(null);
    
    public TableList(final List<Table> list) {
        super(list);
    }
    
    public Optional<Table> get(final String tableName) {
        return list().stream()
                     .filter(table -> table.tableName()
                                           .toLowerCase()
                                           .equals(tableName.toLowerCase()))
                     .findAny();
    }
}
