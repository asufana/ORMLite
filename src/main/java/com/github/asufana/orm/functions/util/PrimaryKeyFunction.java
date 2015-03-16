package com.github.asufana.orm.functions.util;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import com.github.asufana.orm.exceptions.*;
import com.github.asufana.orm.functions.inspection.*;
import com.github.asufana.orm.functions.inspection.resources.*;

public class PrimaryKeyFunction {
    
    public static String pkColumnName(final Connection connection,
                                      final String tableName) {
        final ColumnList pkColumns = Inspection.pkColumns(connection, tableName);
        assert pkColumns.size() == 1 : "複合主キーにはまだ未対応...";
        return pkColumns.get(0).get().columnName();
    }
    
    public static <T> Object pkFieldValue(final String pkColumnName,
                                          final T instance) {
        try {
            final Field pkField = Arrays.asList(instance.getClass()
                                                        .getDeclaredFields())
                                        .stream()
                                        .filter(f -> f.getName()
                                                      .toLowerCase()
                                                      .equals(pkColumnName.toLowerCase()))
                                        .findFirst()
                                        .orElseThrow(() -> new ORMLiteException(String.format("Can't find PK Field on Target Class. Target Class:%s, PK Field:%s",
                                                                                              instance.getClass()
                                                                                                      .getSimpleName(),
                                                                                              pkColumnName)));
            pkField.setAccessible(true);
            final Object pkFieldValue = pkField.get(instance);
            if (pkFieldValue == null) {
                throw new ORMLiteException(String.format("Can't get PK Field value on Target Class. Target Class:%s, PK Field:%s",
                                                         instance.getClass()
                                                                 .getSimpleName(),
                                                         pkField.getName()));
            }
            return pkFieldValue;
        }
        catch (IllegalArgumentException | IllegalAccessException
                | SecurityException e) {
            throw new ORMLiteException(e);
        }
    }
}
