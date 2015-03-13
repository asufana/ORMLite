package com.github.asufana.orm.functions.mapping;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import org.objenesis.*;

import com.github.asufana.orm.exceptions.*;

public class RowFactory {
    private static final Objenesis objenesis = new ObjenesisStd();
    
    public static <T> RowList<T> create(final Class<T> klass, final ResultSet rs) {
        try {
            final List<Row<T>> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(new Row<T>(newInstance(klass, rs)));
            }
            return new RowList<T>(rows);
        }
        catch (final SQLException | IllegalArgumentException
                | IllegalAccessException e) {
            throw new ORMLiteException(e);
        }
    }
    
    private static <T> T newInstance(final Class<T> klass, final ResultSet rs) throws IllegalArgumentException, IllegalAccessException, SQLException {
        final T instance = createInstance(klass);
        return setFields(instance, rs);
    }
    
    private static <T> T createInstance(final Class<T> klass) {
        return objenesis.newInstance(klass);
    }
    
    private static <T> T setFields(final T instance, final ResultSet rs) throws SQLException, IllegalArgumentException, IllegalAccessException {
        final FieldList fieldList = new FieldList(instance.getClass());
        final ResultSetMetaData meta = rs.getMetaData();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            final String columnName = meta.getColumnName(i + 1);
            final Object value = rs.getObject(i + 1);
            final Field field = fieldList.get(columnName.toLowerCase());
            if (field != null) {
                field.setAccessible(true);
                field.set(instance, value);
            }
        }
        return instance;
    }
    
    static class FieldList {
        private final Map<String, Field> fieldMap = new HashMap<>();
        
        public FieldList(final Class<?> klass) {
            final List<Field> fields = Arrays.asList(klass.getDeclaredFields());
            for (final Field field : fields) {
                final String fieldName = field.getName();
                if (fieldMap.containsKey(fieldName)) {
                    throw new ORMLiteException("フィールド名が重複しています：" + fieldName);
                }
                fieldMap.put(fieldName, field);
            }
        }
        
        public Field get(final String fieldName) {
            return fieldMap.get(fieldName);
        }
    }
    
}
