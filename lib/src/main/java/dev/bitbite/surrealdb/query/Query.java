package dev.bitbite.surrealdb.query;

/**
 * The base class for all query types in the SurrealDB library.
 */
public abstract class Query {

    /**
     * Returns a string representation of the query.
     *
     * @return the string representation of the query
     */
    @Override
    public abstract String toString();
    
    /**
     * Creates a new SelectQuery object with the specified fields.
     *
     * @param fields the fields to select
     * @return a new SelectQuery object
     */
    public static SelectQuery select(String... fields){
        return new SelectQuery(fields);
    }

    /**
     * Creates a new SelectQuery object that selects all fields.
     *
     * @return a new SelectQuery object
     */
    public static SelectQuery selectAll(){
        return new SelectQuery("*");
    }

}
