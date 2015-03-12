package com.github.asufana.orm.functions.inspection;

import org.junit.*;

import com.github.asufana.orm.*;

public class InspectionTest extends BaseTest {
    
    private Inspection inspection;
    
    @Before
    public void before() {
        inspection = new Inspection(connection);
    }
    
    @After
    public void after() {
        inspection.closeConnection();
    }
    
    @Test
    public void testTables() {
        inspection.tables();
    }
    
}
