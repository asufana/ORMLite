package com.github.asufana.orm.functions.query;

import java.sql.*;
import java.util.*;

import com.github.asufana.orm.exceptions.*;

public class Query {
    
    public static Integer execute(final Connection connection, final String sql) {
        return execute(connection, sql, Collections.emptyList());
    }
    
    public static Integer execute(final Connection connection,
                                  final String sql,
                                  final List<Object> params) {
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            final PreparedStatement paramdPreparedStatement = setParameters(prepareStatement,
                                                                            params);
            return paramdPreparedStatement.executeUpdate();
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e, sql);
        }
    }
    
    private static PreparedStatement setParameters(final PreparedStatement prepareStatement,
                                                   final List<Object> params) throws SQLException {
        if (params == null || params.size() == 0) {
            return prepareStatement;
        }
        for (int i = 0; i < params.size(); i++) {
            prepareStatement.setObject(i + 1, params.get(i));
        }
        return prepareStatement;
    }
    
    public static <R> R executeQuery(final Connection connection,
                                     final String sql,
                                     final ResultSetCallback<R> callback) {
        return executeQuery(connection, sql, Collections.emptyList(), callback);
    }
    
    public static <R> R executeQuery(final Connection connection,
                                     final String sql,
                                     final List<Object> params,
                                     final ResultSetCallback<R> callback) {
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            final PreparedStatement paramdPreparedStatement = setParameters(prepareStatement,
                                                                            params);
            try (final ResultSet rs = paramdPreparedStatement.executeQuery()) {
                return callback.apply(rs);
            }
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e, sql);
        }
    }
}
