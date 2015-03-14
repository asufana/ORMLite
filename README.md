

# ORMLite

[![Build Status](https://travis-ci.org/asufana/ORMLite.svg?branch=master)](https://travis-ci.org/asufana/ORMLite)

Tiny Object-relational mapping tool.


## Examples

### Model

Example model class:

```
    @Getter
    public static class Member {
        private final Integer id;
        private final String name;
        
        public Member(final Integer id, final String name) {
            this.id = id;
            this.name = name;
        }
    }
```

### Setup

```
    Connection connection = ConnectionFactory.create(DatabaseType.H2, dbUrl, dbUser, dbPass);
    EntityManager<Member> em = new ORMLite(connection).on(Member.class);
```                                              

### Insert

```
    Map<String, String> insertValues = new MapBuilder<String, String>().put("name", "foo").build();
    Row<Member> row = em.values(insertValues).insert();
    Member member = row.get();
```

### Count

```
    assertThat(em.count(), is(0));
    em.values(insertValues).insert();
    assertThat(em.count(), is(1));
```

### Select

```
    Row<Member> row = em.where("NAME=?", "foo").select();
    Member member = row.get();
    
    RowList<Member> rows = em.where("NAME in (?,?)", "foo", "bar").selectList();
    List<Member> members = rows.toList();
```

### Delete

```
    Row<Member> row = em.where("NAME=?", "foo").select();
    row.delete();
    
    Member member = em.where("NAME=?", "bar").select().get();
    em.toRow(member).delete();
```
