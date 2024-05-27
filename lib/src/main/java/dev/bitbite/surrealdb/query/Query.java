package dev.bitbite.surrealdb.query;

public abstract class Query {

    @Override
    public abstract String toString();
    
    public static SelectQuery select(String... fields){
        return new SelectQuery(fields);
    }

    public static SelectQuery selectAll(){
        return new SelectQuery("*");
    }

}
