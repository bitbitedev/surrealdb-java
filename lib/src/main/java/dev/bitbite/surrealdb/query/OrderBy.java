package dev.bitbite.surrealdb.query;

public class OrderBy {

    public enum Order {
        ASC, DESC
    }
    
    private String field;
    private Order order;
    
    public OrderBy(String field, Order order){
        this.field = field;
        this.order = order;
    }

    public OrderBy(String field){
        this.field = field;
        this.order = Order.ASC;
    }

    @Override
    public String toString() {
        return field + " " + order.toString();
    }

}
