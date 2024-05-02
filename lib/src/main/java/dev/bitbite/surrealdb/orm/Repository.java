package dev.bitbite.surrealdb.orm;

import java.util.List;

import dev.bitbite.surrealdb.SurrealDBConnection;
import dev.bitbite.surrealdb.query.SelectQuery;

public class Repository<T extends Identifiable> {
    
    private final Class<T> type;
    private final String tableName;
    private final SurrealDBConnection connection;

    public Repository(Class<T> type, SurrealDBConnection connection) {
        this.type = type;
        this.tableName = type.getSimpleName().toLowerCase();
        this.connection = connection;
    }

    public void add(T object) {
        connection.create(this.tableName, object);
    }

    public void update(T object) {
        connection.update(this.tableName, object.getId().split(":")[1], object);
    }

    public void delete(T object) {
        connection.delete(this.type, this.tableName, object.getId());
    }

    public T get(String id) {
        return connection.select(this.type, this.tableName, id.split(":")[1]).getResult().get(0);
    }

    public List<T> getSpecific(SelectQuery query) {
        query.from(this.tableName);
        return connection.query(this.type, query).get(0).getResult();
    }
    public List<T> getAll() {
        return connection.select(type, tableName).getResult();
    }

}
