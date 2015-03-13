package com.github.asufana.orm;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.*;

import com.github.asufana.orm.functions.query.*;

public class EntityManager<T> {
    
    private final Connection connection;
    private final Class<T> klass;
    private Map<String, String> values = Collections.emptyMap();
    
    EntityManager(final Connection connection, final Class<T> klass) {
        this.connection = connection;
        this.klass = klass;
    }
    
    private void clearQueryParameters() {
        this.values = Collections.emptyMap();
    }
    
    private String tableName() {
        return klass.getSimpleName();
    }
    
    public Integer count() {
        return count(null, null);
    }
    
    private Integer count(final String sql, final List<Object> params) {
        final String whereString = StringUtils.isNotEmpty(sql)
                ? "WHERE " + sql
                : "";
        return Query.executeQuery(connection,
                                  String.format("SELECT count(*) FROM %s %s",
                                                tableName(),
                                                whereString),
                                  params,
                                  rs -> {
                                      rs.next();
                                      return rs.getInt(1);
                                  });
    }
    
    public EntityManager<T> values(final Map<String, String> values) {
        this.values = values;
        return this;
    }
    
    public void insert() {
        Query.execute(connection,
                      String.format("INSERT INTO %s (%s) VALUES (%s)",
                                    tableName(),
                                    insertColumns(),
                                    insertValues()));
        clearQueryParameters();
    }
    
    private String insertColumns() {
        return values.keySet().stream().collect(Collectors.joining(","));
    }
    
    private String insertValues() {
        return values.values()
                     .stream()
                     .collect(Collectors.joining(",", "'", "'"));
    }
}
