package com.github.asufana.orm.functions.inspection;

import java.sql.*;
import java.util.*;

import com.github.asufana.orm.exceptions.*;
import com.github.asufana.orm.functions.inspection.resources.*;

public class Inspection {
    
    public static final String CATALOG = null;
    public static final String SCHEMA = null;
    public static final String NAME_PATTERN = "%";
    private static final String[] TABLE_TYPES = null;
    
    private final DatabaseMetaData meta;
    
    public Inspection(final Connection connection) {
        if (connection == null) {
            throw new ORMLiteException("No connection.");
        }
        try {
            meta = connection.getMetaData();
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e);
        }
    }
    
    public TableList tables() {
        final List<Table> tables = new ArrayList<>();
        try {
            try (final ResultSet rs = meta.getTables(CATALOG,
                                                     SCHEMA,
                                                     NAME_PATTERN,
                                                     TABLE_TYPES)) {
                while (rs.next()) {
                    //http://docs.oracle.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getTables(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
                    if (rs.getString("TABLE_TYPE").equals("TABLE") == false) {
                        continue;
                    }
                    tables.add(new Table(meta,
                                         rs.getString("TABLE_CAT"),
                                         rs.getString("TABLE_SCHEM"),
                                         rs.getString("TABLE_NAME"),
                                         rs.getString("TABLE_TYPE"),
                                         rs.getString("REMARKS"),
                                         rs.getString("TYPE_CAT"),
                                         rs.getString("TYPE_SCHEM"),
                                         rs.getString("TYPE_NAME"),
                                         rs.getString("SELF_REFERENCING_COL_NAME"),
                                         rs.getString("REF_GENERATION")));
                }
            }
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e);
        }
        return tables.size() != 0
                ? new TableList(tables)
                : TableList.EMPTY;
    }
    
    public static ColumnList pkColumns(final Connection connection,
                                       final String tableName) {
        return new Inspection(connection).tables()
                                         .get(tableName)
                                         .map(table -> table.pkColumns())
                                         .orElseThrow(() -> new ORMLiteException("Can't find table or Primary Key columns."));
    }
}
