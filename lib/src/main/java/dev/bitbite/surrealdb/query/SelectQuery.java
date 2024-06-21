package dev.bitbite.surrealdb.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a SELECT query in the SurrealDB query language.
 */
public class SelectQuery extends Query {

    private String[] fields;
    private String table;
    private List<WhereClause> whereClauses;
    private List<OrderBy> orderByClauses;
    private Integer limit;
    private Integer offset;
    
    /**
     * Constructs a new SelectQuery object with the specified fields.
     * 
     * @param fields the fields to select
     */
    public SelectQuery(String... fields){
        this.fields = fields;
        this.whereClauses = new ArrayList<>();
        this.orderByClauses = new ArrayList<>();
    }

    /**
     * Specifies the table to select from.
     * 
     * @param table the table to select from
     * @return the SelectQuery object
     */
    public SelectQuery from(String table){
        this.table = table;
        return this;
    }

    /**
     * Adds a WHERE clause to the query.
     * 
     * @param field the field to compare
     * @param operator the comparison operator
     * @param value the value to compare against
     * @return the SelectQuery object
     */
    public SelectQuery where(String field, String operator, Object value){
        this.whereClauses.add(new WhereClause(field, operator, value));
        return this;
    }

    /**
     * Adds a combined WHERE clause to the query.
     * 
     * @param combined the combined WHERE clause
     * @return the SelectQuery object
     */
    public SelectQuery where(String combined){
        this.whereClauses.add(new WhereClause(combined));
        return this;
    }

    /**
     * Adds a WHERE clause object to the query.
     * 
     * @param whereClause the WHERE clause object
     * @return the SelectQuery object
     */
    public SelectQuery where(WhereClause whereClause){
        this.whereClauses.add(whereClause);
        return this;
    }

    /**
     * Adds an ORDER BY clause with a random order to the query.
     * 
     * @return the SelectQuery object
     */
    public SelectQuery orderByRand() {
        this.orderByClauses.add(new OrderBy("RAND()"));
        return this;
    }

    /**
     * Adds an ORDER BY clause with the specified field to the query.
     * 
     * @param field the field to order by
     * @return the SelectQuery object
     */
    public SelectQuery orderBy(String field){
        this.orderByClauses.add(new OrderBy(field));
        return this;
    }

    /**
     * Adds an ORDER BY clause with the specified field and order to the query.
     * 
     * @param field the field to order by
     * @param order the order (ASC or DESC)
     * @return the SelectQuery object
     */
    public SelectQuery orderBy(String field, OrderBy.Order order){
        this.orderByClauses.add(new OrderBy(field, order));
        return this;
    }

    /**
     * Adds an ORDER BY clause object to the query.
     * 
     * @param orderBy the ORDER BY clause object
     * @return the SelectQuery object
     */
    public SelectQuery orderBy(OrderBy orderBy){
        this.orderByClauses.add(orderBy);
        return this;
    }

    /**
     * Sets the maximum number of rows to return.
     * 
     * @param limit the maximum number of rows
     * @return the SelectQuery object
     */
    public SelectQuery limit(int limit){
        this.limit = limit;
        return this;
    }

    /**
     * Sets the number of rows to skip before starting to return rows.
     * 
     * @param offset the number of rows to skip
     * @return the SelectQuery object
     */
    public SelectQuery offset(int offset){
        this.offset = offset;
        return this;
    }

    /**
     * Returns the string representation of the SelectQuery object.
     * 
     * @return the string representation of the SelectQuery object
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for(int i = 0; i < fields.length; i++){
            sb.append(fields[i]);
            if(i < fields.length - 1){
                sb.append(", ");
            }
        }
        sb.append(" FROM ");
        sb.append(table);
        if(!whereClauses.isEmpty()){
            sb.append(" WHERE ");
            for(int i = 0; i < whereClauses.size(); i++){
                sb.append(whereClauses.get(i));
                if(i < whereClauses.size() - 1){
                    sb.append(" AND ");
                }
            }
        }
        if(!orderByClauses.isEmpty()){
            sb.append(" ORDER BY ");
            for(int i = 0; i < orderByClauses.size(); i++){
                sb.append(orderByClauses.get(i));
                if(i < orderByClauses.size() - 1){
                    sb.append(", ");
                }
            }
        }
        if(limit != null){
            sb.append(" LIMIT ");
            sb.append(limit);
        }
        if(offset != null){
            sb.append(" START ");
            sb.append(offset);
        }
        return sb.toString();
    }

}
