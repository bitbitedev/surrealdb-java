package dev.bitbite.surrealdb.orm;

import java.util.List;

import dev.bitbite.surrealdb.SurrealDBConnection;
import dev.bitbite.surrealdb.query.SelectQuery;

/**
 * A generic repository class for managing objects of a specific type in the SurrealDB.
 * @param <T> The type of objects managed by this repository.
 */
public class Repository<T extends Identifiable> {
    
    private final Class<T> type;
    private final String tableName;
    private SurrealDBConnection connection;

    /**
     * Constructs a new Repository object.
     * @param type The class type of the objects managed by this repository.
     * @param connection The SurrealDBConnection object used for database operations.
     */
    public Repository(Class<T> type, SurrealDBConnection connection) {
        this.type = type;
        this.tableName = type.getSimpleName().toLowerCase();
        this.connection = connection;
    }

    /**
     * Adds a new object to the repository.
     * @param object The object to be added.
     * @return The added object.
     */
    public T add(T object) {
        return connection.create(this.tableName, object).get(0).getResult().get(0);
    }

    /**
     * Updates an existing object in the repository.
     * @param object The object to be updated.
     * @return The updated object.
     */
    public T update(T object) {
        return connection.update(this.tableName, object.getId().split(":")[1], object).get(0).getResult().get(0);
    }

    /**
     * Deletes an object from the repository.
     * @param object The object to be deleted.
     * @return The deleted object.
     */
    public T delete(T object) {
        return connection.delete(this.type, this.tableName, object.getId()).get(0).getResult().get(0);
    }

    /**
     * Retrieves an object from the repository by its ID.
     * @param id The ID of the object to retrieve.
     * @return The retrieved object.
     */
    public T get(String id) {
        return connection.select(this.type, this.tableName, id.contains(":") ? id.split(":")[1] : id).getResult().get(0);
    }

    /**
     * Retrieves a list of objects from the repository based on a specific query.
     * @param query The SelectQuery object representing the query.
     * @return The list of retrieved objects.
     */
    public List<T> getSpecific(SelectQuery query) {
        query.from(this.tableName);
        return connection.query(this.type, query).get(0).getResult();
    }

    /**
     * Retrieves all objects from the repository.
     * @return The list of all objects in the repository.
     */
    public List<T> getAll() {
        return connection.select(type, tableName).getResult();
    }

    /**
     * Sets the SurrealDBConnection object used for database operations.
     * @param connection The SurrealDBConnection object.
     */
    public void setConnection(SurrealDBConnection connection) {
        this.connection = connection;
    }
}
