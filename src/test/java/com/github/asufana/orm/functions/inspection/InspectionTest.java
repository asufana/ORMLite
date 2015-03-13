package com.github.asufana.orm.functions.inspection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import com.github.asufana.orm.*;
import com.github.asufana.orm.functions.inspection.resources.*;
import com.github.asufana.orm.functions.query.*;

public class InspectionTest extends BaseTest {
    
    private Inspection inspection;
    private final String tableName = "x";
    
    @Before
    public void before() {
        assertThat(Query.execute(connection, "DROP TABLE IF EXISTS x"), is(0));
        assertThat(Query.execute(connection, String.format("CREATE TABLE %s ("
                + "id integer unsigned auto_increment primary key,"
                + "name varchar(255) not null)", tableName)), is(0));
        
        inspection = new Inspection(connection);
    }
    
    @Test
    public void testTables() {
        final TableList tables = inspection.tables();
        assertThat(tables.size(), is(1));
        assertThat(tables.get(tableName).get(), is(notNullValue()));
        tables.forEach(System.out::println);
    }
    
    @Test
    public void testColumns() throws Exception {
        final TableList tables = inspection.tables();
        final Table table = tables.get(tableName).get();
        final ColumnList columns = table.columns();
        assertThat(columns.size(), is(2));
        columns.forEach(System.out::println);
    }
    
    @Test
    public void testPkColumns() throws Exception {
        final TableList tables = inspection.tables();
        final Table table = tables.get(tableName).get();
        final ColumnList columns = table.pkColumns();
        assertThat(columns.size(), is(1));
        columns.forEach(System.out::println);
    }
}
