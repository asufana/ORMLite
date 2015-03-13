package com.github.asufana.orm.functions.inspection.resources;

import java.util.*;

import com.github.asufana.orm.functions.util.AbstractCollection;

public class ColumnList extends AbstractCollection<ColumnList, Column> {
    
    public static final ColumnList EMPTY = new ColumnList(null);
    
    public ColumnList(final List<Column> list) {
        super(list);
    }
}
