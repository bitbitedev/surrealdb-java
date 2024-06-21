package dev.bitbite.surrealdb.query;

/**
 * Represents an order by clause in a database query.
 */
public class OrderBy {

    /**
     * Represents the order direction.
     */
    public enum Order {
        ASC, DESC
    }
    
    private String field;
    private Order order;
    
    /**
     * Constructs an OrderBy object with the specified field and order.
     * 
     * @param field the field to order by
     * @param order the order direction
     */
    public OrderBy(String field, Order order){
        this.field = field;
        this.order = order;
    }

    /**
     * Constructs an OrderBy object with the specified field and default order (ASC).
     * 
     * @param field the field to order by
     */
    public OrderBy(String field){
        this.field = field;
    }

    /**
     * Returns a string representation of the OrderBy object.
     * 
     * @return a string representation of the OrderBy object
     */
    @Override
    public String toString() {
        return field + ((order != null) ? " " + order.toString() : "");
    }

}
