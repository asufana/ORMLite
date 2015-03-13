package com.github.asufana.orm.functions.inspection.resources;

import java.sql.*;

import lombok.*;

@Value
public class Column {
    private final DatabaseMetaData meta;
    private final String tableCat;
    private final String tableSchem;
    private final String tableName;
    private final String columnName;
    private final Integer dataType;
    private final String typeName;
    private final Integer columnSize;
    private final Integer decimalDigits;
    private final Integer numPrecRadix;
    private final Integer nullable;
    private final String remarks;
    private final String columnDef;
    private final Integer charOctetLength;
    private final Integer ordinalPosition;
    private final String isNullable;
    private final String scopeCatlog;
    private final String scopeSchema;
    private final String scopeTable;
    private final String sourceDataType;
    private final String isAutoincrement;
    private final Short keySeq;
    private final String pkName;
}
