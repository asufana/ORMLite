package com.github.asufana.orm.functions.query;

import java.sql.*;
import java.util.*;

import lombok.*;

import com.github.asufana.orm.exceptions.*;

public class Query {
    
    public static Integer execute(final Connection connection, final String sql) {
        return execute(connection, sql, Collections.emptyList());
    }
    
    public static Integer execute(final Connection connection,
                                  final String sql,
                                  final List<Object> params) {
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            final PreparedStatement paramedPreparedStatement = setParameters(prepareStatement,
                                                                             params);
            return paramedPreparedStatement.executeUpdate();
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e, sql);
        }
    }
    
    public static ExecuteResult executeInsert(final Connection connection,
                                              final String sql) {
        return executeInsert(connection, sql, Collections.emptyList());
    }
    
    public static ExecuteResult executeInsert(final Connection connection,
                                              final String sql,
                                              final List<Object> params) {
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql,
                                                                              Statement.RETURN_GENERATED_KEYS)) {
            final PreparedStatement paramedPreparedStatement = setParameters(prepareStatement,
                                                                             params);
            final Integer executedCount = paramedPreparedStatement.executeUpdate();
            final Long generatedId = getGeneratedId(paramedPreparedStatement);
            return new ExecuteResult(executedCount, generatedId);
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e, sql);
        }
    }
    
    private static Long getGeneratedId(final PreparedStatement paramdPreparedStatement) throws SQLException {
        final ResultSet generatedKeys = paramdPreparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        }
        return null;
    }
    
    @Value
    public static class ExecuteResult {
        public final Integer executedCount;
        public final Long generatedId;
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
