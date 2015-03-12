package com.github.asufana.orm.functions.query;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import com.github.asufana.orm.*;

public class QueryTest extends BaseTest {
    
    @Before
    @Test
    public void testExecute() {
        assertThat(Query.execute(connection, "DROP TABLE IF EXISTS x"), is(0));
        assertThat(Query.execute(connection, "CREATE TABLE x ("
                + "id integer unsigned auto_increment primary key,"
                + "name varchar(255) not null)"), is(0));
    }
    
    @Test
    public void testExecuteQuery() {
        //INSERT
        assertThat(Query.execute(connection,
                                 "INSERT INTO x (name) VALUES (?),(?)",
                                 toList("foo", "bar")), is(2));
        
        //COUNT
        assertThat(Query.executeQuery(connection,
                                      "SELECT count(*) FROM x",
                                      rs -> {
                                          rs.next();
                                          return rs.getInt(1);
                                      }), is(2));
        
        //SELECT
        assertThat(Query.executeQuery(connection, "SELECT * FROM x", rs -> {
            rs.next();
            return rs.getString("name");
        }), is("foo"));
        
        //DELETE
        Query.execute(connection,
                      "DELETE FROM x WHERE NAME IN (?,?)",
                      toList("foo", "bar"));
        assertThat(Query.executeQuery(connection,
                                      "SELECT count(*) FROM x",
                                      rs -> {
                                          rs.next();
                                          return rs.getInt(1);
                                      }), is(0));
    }
    
}
