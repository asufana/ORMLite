package com.github.asufana.orm;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.*;

import com.github.asufana.orm.functions.inspection.*;
import com.github.asufana.orm.functions.inspection.resources.*;
import com.github.asufana.orm.functions.mapping.*;
import com.github.asufana.orm.functions.query.*;
import com.github.asufana.orm.functions.query.Query.ExecuteResult;

public class EntityManager<T> {
    
    private final Connection connection;
    private final Class<T> klass;
    private Map<String, String> values = Collections.emptyMap();
    private String sql = null;
    private List<Object> sqlParams = Collections.emptyList();
    
    EntityManager(final Connection connection, final Class<T> klass) {
        this.connection = connection;
        this.klass = klass;
    }
    
    private void clearQueryParameters() {
        this.values = Collections.emptyMap();
        this.sql = null;
        this.sqlParams = Collections.emptyList();
    }
    
    private String tableName() {
        return klass.getSimpleName();
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
        final ExecuteResult result = Query.executeInsert(connection,
                                                         String.format("INSERT INTO %s (%s) VALUES (%s)",
                                                                       tableName(),
                                                                       insertColumns(),
                                                                       insertValues()));
        clearQueryParameters();
        assert result.executedCount == 1;
        
        final String pkColumnName = getPKColumnName();
        return where(String.format("%s=?", pkColumnName), result.generatedId).select();
    }
    
    private String getPKColumnName() {
        final Table table = new Inspection(connection).tables()
                                                      .get(tableName())
                                                      .get();
        assert table != null : "テーブル情報が取得できません";
        final ColumnList pkColumns = table.pkColumns();
        assert pkColumns.size() != 1 : "現時点でPKカラム1つまでしか対応していない・・・。PK Column count:"
                + pkColumns.size();
        return pkColumns.get(0).get().columnName();
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
    
    public EntityManager<T> where(final String sql, final Object... params) {
        this.sql = sql;
        this.sqlParams = Arrays.asList(params);
        return this;
    }
    
    public Row<T> select() {
        return selectList().first();
    }
    
    public RowList<T> selectList() {
        final RowList<T> rows = Query.executeQuery(connection,
                                                   String.format("SELECT * FROM %s WHERE %s",
                                                                 tableName(),
                                                                 sql),
                                                   sqlParams,
                                                   rs -> RowFactory.create(klass,
                                                                           rs));
        clearQueryParameters();
        return rows;
    }
    
    //- DELETE ---------------------------------
    
    //- UPDATE ---------------------------------
    
}