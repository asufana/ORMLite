package com.github.asufana.orm.functions.inspection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.github.asufana.orm.*;
import com.github.asufana.orm.functions.inspection.resources.*;
import com.github.asufana.orm.functions.query.*;

public class InspectionTest extends BaseTest {
    
    private Inspection inspection;
    
    @Before
    public void before() {
        assertThat(Query.execute(connection, "DROP TABLE IF EXISTS x"), is(0));
        assertThat(Query.execute(connection, "CREATE TABLE x ("
                + "id integer unsigned auto_increment primary key,"
                + "name varchar(255) not null)"), is(0));
        
        inspection = new Inspection(connection);
    }
    
    @After
    public void after() {
        inspection.closeConnection();
    }
    
    @Test
    public void testTables() {
        final List<Table> tables = inspection.tables();
        assertThat(tables.size(), is(1));
        assertThat(tables.get(0).tableName(), is("X"));
        tables.forEach(System.out::println);
    }
    
    @Test
    public void testColumns() throws Exception {
        final List<Table> tables = inspection.tables();
        final Table table = tables.get(0);
        final List<Column> columns = table.columns();
        assertThat(columns.size(), is(2));
        columns.forEach(System.out::println);
    }
}
