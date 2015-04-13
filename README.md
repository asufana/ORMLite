

# ORMLite

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

### Count

```
    Integer count = em.count();
```

### Insert

```
    Map<String, String> insertValues = new MapBuilder<String, String>().put("name", "foo").build();
    Row<Member> row = em.values(insertValues).insert();
    Member member = row.get();
```

### Select

```
    // Select a record
    Row<Member> row = em.where("NAME=?", "foo").select();
    Member member = row.get();
    
    // Select list
    RowList<Member> rows = em.where("NAME in (?,?)", "foo", "bar").selectList();
    List<Member> members = rows.toList();
```

### Update

```
    Map<String, String> updateValues = new MapBuilder<String, String>().put("name", "foo2").build();

    // Update Row
    Row<Member> row = em.where("NAME=?", "foo").select();
    row.values(updateValues).update();

    // Update Object
    Member member = em.where("NAME=?", "foo").select().get();
    em.toRow(member).values(updateValues).update();
```

### Delete

```
    // Delete Row
    Row<Member> row = em.where("NAME=?", "foo").select();
    row.delete();
    
    // Delete Object
    Member member = em.where("NAME=?", "bar").select().get();
    em.toRow(member).delete();
```


## Install


```xml:pom.xml
<dependencies>
    <dependency>
        <groupId>com.github.asufana</groupId>
        <artifactId>ORMLite</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>ORMLite</id>
        <url>https://raw.github.com/asufana/ORMLite/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

