package com.github.asufana.orm;

import java.sql.*;

import lombok.*;

import com.github.asufana.orm.exceptions.*;

@Getter
public class ORMLite {
    
    private final Connection connection;
    
    public ORMLite(final Connection connection) {
        if (connection == null) {
            throw new ORMLiteException("No connection.");
        }
        this.connection = connection;
    }
    
    public <T> EntityManager<T> on(final Class<T> klass) {
        return new EntityManager<T>(connection, klass);
    }
}
