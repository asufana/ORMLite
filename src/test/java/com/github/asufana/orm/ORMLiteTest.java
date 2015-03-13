package com.github.asufana.orm;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.*;

import lombok.*;

import org.junit.*;

import com.github.asufana.orm.functions.inspection.*;
import com.github.asufana.orm.functions.mapping.*;
import com.github.asufana.orm.functions.query.*;
import com.github.asufana.orm.functions.util.*;

public class ORMLiteTest extends BaseTest {
    
    private final ORMLite orm = new ORMLite(connection);
    private final String tableName = "member";
    
    @Before
    @Test
    public void testInsert() {
        //CREATE TABLE
        assertThat(Query.execute(connection,
                                 String.format("DROP TABLE IF EXISTS %s",
                                               tableName)), is(0));
        assertThat(Query.execute(connection, String.format("CREATE TABLE %s ("
                + "id integer unsigned auto_increment primary key,"
                + "name varchar(255) not null)", tableName)), is(0));
        assertThat(new Inspection(connection).tables()
                                             .get(tableName)
                                             .isPresent(), is(true));
        
        //EntityManager
        final EntityManager<Member> em = orm.on(Member.class);
        
        //INSERT
        assertThat(em.count(), is(0));
        em.values(new MapBuilder<String, String>().put("name", "foo").build())
          .insert();
        em.values(new MapBuilder<String, String>().put("name", "bar").build())
          .insert();
        assertThat(em.count(), is(2));
    }
    
    @Test
    public void testSelect() {
        final EntityManager<Member> em = orm.on(Member.class);
        final Row<Member> row = em.where("NAME=?", "foo").select();
        assertThat(row, is(notNullValue()));
        
        final Member member = row.get();
        assertThat(member.name(), is("foo"));
        
        assertThat(em.where("NAME=?", "bar").select().get().name(), is("bar"));
    }
    
    @Test
    public void testSelectList() {
        final EntityManager<Member> em = orm.on(Member.class);
        final RowList<Member> rows = em.where("NAME in (?,?)", "foo", "bar")
                                       .selectList();
        assertThat(rows, is(notNullValue()));
        
        final List<Member> members = rows.toList();
        assertThat(members, is(notNullValue()));
        assertThat(members.size(), is(2));
    }
    
    @Getter
    public static class Member {
        private final Integer id;
        private final String name;
        
        public Member(final Integer id, final String name) {
            this.id = id;
            this.name = name;
        }
    }
    
}
