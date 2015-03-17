package com.github.asufana.orm;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.*;

import com.github.asufana.orm.exceptions.*;
import com.github.asufana.orm.functions.mapping.*;
import com.github.asufana.orm.functions.query.*;
import com.github.asufana.orm.functions.query.Query.ExecuteResult;
import com.github.asufana.orm.functions.util.*;

public class EntityManager<T> {
    
    //TODO transaction
    
    private final Connection connection;
    private final Class<T> klass;
    private Map<String, String> values = Collections.emptyMap();
    private String sql = null;
    private List<Object> sqlParams = Collections.emptyList();
    
    EntityManager(final Connection connection, final Class<T> klass) {
        this.connection = connection;
        this.klass = klass;
    }
    
    private EntityManager<T> copyThis() {
        return new ORMLite(connection).on(klass);
    }
    
    public Connection connection() {
        return connection;
    }
    
    public Class<T> targetClass() {
        return klass;
    }
    
    private void clearQueryParameters() {
        this.values = Collections.emptyMap();
        this.sql = null;
        this.sqlParams = Collections.emptyList();
    }
    
    public String tableName() {
        return klass.getSimpleName();
    }
    
    public Row<T> toRow(final T instance) {
        return new Row<T>(this, instance);
    }
    
    //- COUNT ---------------------------------
    
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
    
    //- INSERT ---------------------------------
    
    public EntityManager<T> values(final Map<String, String> values) {
        this.values = values;
        return this;
    }
    
    public Row<T> insert() {
        if (StringUtils.isEmpty(tableName())
                || values == null
                || values.size() == 0) {
            throw ORMLiteException.emptyParams();
        }
        final ExecuteResult result = Query.executeInsert(connection,
                                                         String.format("INSERT INTO %s (%s) VALUES (%s)",
                                                                       tableName(),
                                                                       insertColumns(),
                                                                       insertValues()));
        clearQueryParameters();
        assert result.executedCount == 1;
        
        final String pkColumnName = PrimaryKeyFunction.pkColumnName(connection,
                                                                    tableName());
        return where(String.format("%s=?", pkColumnName), result.generatedId).select();
    }
    
    private String insertColumns() {
        return values.keySet().stream().collect(Collectors.joining(","));
    }
    
    private String insertValues() {
        return values.values()
                     .stream()
                     .collect(Collectors.joining(",", "'", "'"));
    }
    
    //- SELECT ---------------------------------
    
    @SuppressWarnings("unchecked")
    public EntityManager<T> where(final String sql, final Object... params) {
        this.sql = sql;
        if (params.length == 1 && params[0] instanceof List<?>) {
            this.sqlParams = (List<Object>) params[0];
        }
        else {
            this.sqlParams = Arrays.asList(params);
        }
        return this;
    }
    
    public Row<T> select() {
        return selectList().first();
    }
    
    public RowList<T> selectList() {
        if (StringUtils.isEmpty(tableName())
                || StringUtils.isEmpty(sql)
                || sqlParams == null
                || sqlParams.size() == 0) {
            throw ORMLiteException.emptyParams();
        }
        final RowList<T> rows = Query.executeQuery(connection,
                                                   String.format("SELECT * FROM %s WHERE %s",
                                                                 tableName(),
                                                                 sql),
                                                   sqlParams,
                                                   rs -> RowFactory.create(this,
                                                                           rs));
        clearQueryParameters();
        return rows;
    }
    
    private Row<T> selectByPK(final Row<T> instance) {
        final String pkColumnName = PrimaryKeyFunction.pkColumnName(connection,
                                                                    tableName());
        final Object pkColumnValue = PrimaryKeyFunction.pkColumnValue(pkColumnName,
                                                                      instance.get());
        return where(String.format("%s=?", pkColumnName), pkColumnValue).select();
    }
    
    //- DELETE ---------------------------------
    
    public Integer delete() {
        if (StringUtils.isEmpty(sql)
                || sqlParams == null
                || sqlParams.size() == 0) {
            throw ORMLiteException.emptyParams();
        }
        //TODO count check
        
        final Integer deleteCount = Query.execute(connection,
                                                  String.format("DELETE FROM %s WHERE %s",
                                                                tableName(),
                                                                sql),
                                                  sqlParams);
        clearQueryParameters();
        assert deleteCount == 1;
        return deleteCount;
    }
    
    //- UPDATE ---------------------------------
    
    public Row<T> update() {
        if (StringUtils.isEmpty(tableName())
                || values == null
                || values.size() == 0
                || StringUtils.isEmpty(sql)
                || sqlParams == null
                || sqlParams.size() == 0) {
            throw ORMLiteException.emptyParams();
        }
        //TODO check count
        
        final Row<T> instance = copyThis().where(sql, sqlParams).select();
        final Integer updateCount = Query.execute(connection,
                                                  String.format("UPDATE %s SET %s WHERE %s",
                                                                tableName(),
                                                                updateValues(),
                                                                sql),
                                                  sqlParams);
        clearQueryParameters();
        assert updateCount == 1;
        
        return selectByPK(instance);
    }
    
    private String updateValues() {
        return values.entrySet()
                     .stream()
                     .map(e -> String.format("%s='%s'",
                                             e.getKey(),
                                             e.getValue()))
                     .collect(Collectors.joining(","));
    }
    
}
