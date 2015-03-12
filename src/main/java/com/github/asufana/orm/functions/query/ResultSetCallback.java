package com.github.asufana.orm.functions.query;

import java.sql.*;

@FunctionalInterface
public interface ResultSetCallback<R> {
    
    R apply(final ResultSet rs) throws SQLException;
    
}
