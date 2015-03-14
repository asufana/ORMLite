package com.github.asufana.orm;

import java.sql.*;

import lombok.*;

import com.github.asufana.orm.exceptions.*;

@Getter
public class ORMLite {
    
    private final Connection connection;
    
    public ORMLite(final Connection connection) {
        try {
            if (connection == null || connection.isClosed()) {
                throw new ORMLiteException("No connection.");
            }
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e);
        }
        this.connection = connection;
    }
    
    public <T> EntityManager<T> on(final Class<T> klass) {
        return new EntityManager<T>(connection, klass);
    }
}
