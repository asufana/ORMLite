package com.github.asufana.orm.functions.connection;

import java.sql.*;

import lombok.*;

import com.github.asufana.orm.exceptions.*;

@Getter
public class ConnectionFactory {
    
    private final DatabaseType databaseType;
    private final String url;
    private final String user;
    private final String pass;
    private final Connection connection;
    
    public static Connection create(final DatabaseType databaseType,
                                    final String url,
                                    final String user,
                                    final String pass) {
        final ConnectionFactory factory = new ConnectionFactory(databaseType,
                                                                url,
                                                                user,
                                                                pass);
        return factory.connection();
    }
    
    private ConnectionFactory(final DatabaseType databaseType,
            final String url,
            final String user,
            final String pass) {
        this.databaseType = databaseType;
        isDatabaseDriverExists(databaseType.driverClassName);
        
        this.url = url;
        this.user = user;
        this.pass = pass;
        connection = createConnection(url, user, pass);
    }
    
    private Connection createConnection(final String url,
                                        final String user,
                                        final String pass) {
        try {
            return DriverManager.getConnection(url, user, pass);
        }
        catch (final SQLException e) {
            throw new ORMLiteException(e);
        }
    }
    
    private void isDatabaseDriverExists(final String driverClassName) {
        try {
            Class.forName(driverClassName);
            //Class.forName(driverClassName).newInstance();
        }
        catch (final ClassNotFoundException e) {
            throw new ORMLiteException(e);
        }
    }
    
    public static enum DatabaseType {
        H2("org.h2.Driver"),
        PostgreSQL("org.postgresql.Driver"),
        MySQL("com.mysql.jdbc.Driver");
        
        private String driverClassName;
        
        private DatabaseType(final String driverClassName) {
            this.driverClassName = driverClassName;
        }
        
        public String driverClassName() {
            return driverClassName;
        }
    }
}
