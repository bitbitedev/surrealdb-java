package dev.bitbite.surrealdb.orm;

import java.util.List;

import dev.bitbite.surrealdb.SurrealDBConnection;
import dev.bitbite.surrealdb.query.SelectQuery;

public class Repository<T extends Identifiable> {
    
    private final Class<T> type;
    private final String tableName;
    private SurrealDBConnection connection;

    public Repository(Class<T> type, SurrealDBConnection connection) {
        this.type = type;
        this.tableName = type.getSimpleName().toLowerCase();
        this.connection = connection;
    }

    public T add(T object) {
        return connection.create(this.tableName, object).get(0).getResult().get(0);
    }

    public T update(T object) {
        return connection.update(this.tableName, object.getId().split(":")[1], object).get(0).getResult().get(0);
    }

    public T delete(T object) {
        return connection.delete(this.type, this.tableName, object.getId()).get(0).getResult().get(0);
    }

    public T get(String id) {
        return connection.select(this.type, this.tableName, id.contains(":") ? id.split(":")[1] : id).getResult().get(0);
    }

    public List<T> getSpecific(SelectQuery query) {
        query.from(this.tableName);
        return connection.query(this.type, query).get(0).getResult();
    }
    public List<T> getAll() {
        return connection.select(type, tableName).getResult();
    }

    public void setConnection(SurrealDBConnection connection) {
        this.connection = connection;
    }
}
