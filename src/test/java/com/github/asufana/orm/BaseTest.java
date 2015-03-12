package com.github.asufana.orm;

import java.sql.*;
import java.util.*;

import org.h2.tools.*;
import org.junit.*;

import com.github.asufana.orm.functions.connection.*;
import com.github.asufana.orm.functions.connection.ConnectionFactory.DatabaseType;

public abstract class BaseTest {
    
    public static final String dbUrl = "jdbc:h2:mem:test";
    public static final String dbUser = "sa";
    public static final String dbPass = "";
    
    protected static Server h2;
    protected static Connection connection = null;
    
    @BeforeClass
    public static void BeforeClass() throws SQLException {
        h2 = Server.createWebServer(new String[] {"-webAllowOthers"});
        System.out.println("Database start.");
        connection = ConnectionFactory.create(DatabaseType.H2,
                                              dbUrl,
                                              dbUser,
                                              dbPass);
        System.out.println("Connect.");
        System.out.println();
    }
    
    @AfterClass
    public static void AfterClass() throws SQLException {
        System.out.println();
        if (connection.isClosed() != false) {
            connection.close();
        }
        System.out.println("Disconnect.");
        h2.stop();
        System.out.println("Database stop.");
    }
    
    protected List<Object> toList(final String... params) {
        return Arrays.asList(params);
    }
}
