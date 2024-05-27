package dev.bitbite.surrealdb.query;

import java.util.ArrayList;
import java.util.List;

public class SelectQuery extends Query {

    private String[] fields;
    private String table;
    private List<WhereClause> whereClauses;
    private List<OrderBy> orderByClauses;
    private Integer limit;
    private Integer offset;
    
    public SelectQuery(String... fields){
        this.fields = fields;
        this.whereClauses = new ArrayList<>();
        this.orderByClauses = new ArrayList<>();
    }

    public SelectQuery from(String table){
        this.table = table;
        return this;
    }

    public SelectQuery where(String field, String operator, Object value){
        this.whereClauses.add(new WhereClause(field, operator, value));
        return this;
    }

    public SelectQuery where(WhereClause whereClause){
        this.whereClauses.add(whereClause);
        return this;
    }

    public SelectQuery orderByRand() {
        this.orderByClauses.add(new OrderBy("RAND()"));
        return this;
    }

    public SelectQuery orderBy(String field){
        this.orderByClauses.add(new OrderBy(field));
        return this;
    }

    public SelectQuery orderBy(String field, OrderBy.Order order){
        this.orderByClauses.add(new OrderBy(field, order));
        return this;
    }

    public SelectQuery orderBy(OrderBy orderBy){
        this.orderByClauses.add(orderBy);
        return this;
    }

    public SelectQuery limit(int limit){
        this.limit = limit;
        return this;
    }

    public SelectQuery offset(int offset){
        this.offset = offset;
        return this;
    }

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
