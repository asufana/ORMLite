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
    
    private final Connection connection;
    private final DatabaseMetaData meta;
    
    public Inspection(final Connection connection) {
        if (connection == null) {
            throw new ORMLiteException("No connection.");
        }
        this.connection = connection;
        try {
            meta = connection.getMetaData();
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e);
        }
    }
    
    public void closeConnection() {
        try {
            if (connection.isClosed() != false) {
                connection.close();
            }
        }
        catch (final Exception e) {
            throw new ORMLiteException(e);
        }
    }
    
    public List<Table> tables() {
        final List<Table> tables = new ArrayList<>();
        try {
            try (final ResultSet rs = meta.getTables(CATALOG,
                                                     SCHEMA,
                                                     NAME_PATTERN,
                                                     TABLE_TYPES)) {
                while (rs.next()) {
                    //http://docs.oracle.com/javase/6/docs/api/java/sql/DatabaseMetaData.html#getTables%28java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.String%5b%5d%29
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
                ? tables
                : Collections.<Table> emptyList();
    }
}