package com.github.asufana.orm.functions.connection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.*;

import org.h2.tools.*;
import org.junit.*;

import com.github.asufana.orm.*;
import com.github.asufana.orm.functions.connection.ConnectionFactory.DatabaseType;

public class ConnectionFactoryTest {
    
    private Server h2 = null;
    
    @Before
    public void before() throws SQLException {
        h2 = Server.createWebServer(new String[] {"-webAllowOthers"});
    }
    
    @After
    public void after() {
        h2.stop();
    }
    
    @Test
    public void testCreate() throws SQLException {
        final Connection conn = ConnectionFactory.create(DatabaseType.H2,
                                                         BaseTest.dbUrl,
                                                         BaseTest.dbUser,
                                                         BaseTest.dbPass);
        assertThat(conn, is(notNullValue()));
        assertThat(conn.isClosed(), is(false));
        
        conn.close();
        assertThat(conn.isClosed(), is(true));
    }
    
}
