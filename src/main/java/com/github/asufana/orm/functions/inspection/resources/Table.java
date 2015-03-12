package com.github.asufana.orm.functions.inspection.resources;

import java.sql.*;
import java.util.*;

import lombok.*;

import com.github.asufana.orm.exceptions.*;
import com.github.asufana.orm.functions.inspection.*;

@Value
public class Table {
    private final DatabaseMetaData meta;
    private final String tableCat;
    private final String tableSchem;
    private final String tableName;
    private final String tableType;
    private final String remarks;
    private final String typeCat;
    private final String typeSchem;
    private final String typeName;
    private final String selfReferencingColName;
    private final String refGeneration;
    
    public List<Column> columns() {
        final List<Column> columns = new ArrayList<>();
        try {
            try (ResultSet rs = meta.getColumns(Inspection.CATALOG,
                                                Inspection.SCHEMA,
                                                tableName(),
                                                Inspection.NAME_PATTERN)) {
                while (rs.next()) {
                    columns.add(new Column(meta,
                                           rs.getString("TABLE_CAT"),
                                           rs.getString("TABLE_SCHEM"),
                                           rs.getString("TABLE_NAME"),
                                           rs.getString("COLUMN_NAME"),
                                           Integer.valueOf(rs.getString("DATA_TYPE")),
                                           rs.getString("TYPE_NAME"),
                                           Integer.valueOf(rs.getString("COLUMN_SIZE")),
                                           Integer.valueOf(rs.getString("DECIMAL_DIGITS")),
                                           Integer.valueOf(rs.getString("NUM_PREC_RADIX")),
                                           Integer.valueOf(rs.getString("NULLABLE")),
                                           rs.getString("REMARKS"),
                                           rs.getString("COLUMN_DEF"),
                                           Integer.valueOf(rs.getString("CHAR_OCTET_LENGTH")),
                                           Integer.valueOf(rs.getString("ORDINAL_POSITION")),
                                           rs.getString("IS_NULLABLE"),
                                           rs.getString("SCOPE_CATLOG"),
                                           rs.getString("SCOPE_SCHEMA"),
                                           rs.getString("SCOPE_TABLE"),
                                           rs.getString("SOURCE_DATA_TYPE"),
                                           rs.getString("IS_AUTOINCREMENT")));
                }
            }
        }
        catch (final Exception e) {
            throw new ORMLiteException(e);
        }
        return columns.size() != 0
                ? columns
                : Collections.emptyList();
    }
}
