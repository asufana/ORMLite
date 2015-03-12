package com.github.asufana.orm.functions.inspection;

import java.sql.*;

import com.github.asufana.orm.exceptions.*;

public class Inspection {
    
    private static final String CATALOG = null;
    private static final String SCHEMA = null;
    private static final String TABLE_NAME_PATTERN = "%";
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
    
    public void tables() {
        try {
            try (final ResultSet rs = meta.getTables(CATALOG,
                                                     SCHEMA,
                                                     TABLE_NAME_PATTERN,
                                                     TABLE_TYPES)) {
                while (rs.next()) {
                    System.out.println(rs.getString("TABLE_NAME")
                            + ":"
                            + rs.getString("TABLE_TYPE"));
                }
            }
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e);
        }
    }
}
