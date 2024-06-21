package dev.bitbite.surrealdb.query;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dev.bitbite.surrealdb.exception.SurrealException;

/**
 * Represents the result of a query in the SurrealDB system.
 * @param <T> The type of the query result.
 */
public class QueryResult<T> {
    
    private String time;
    private String status;
    private List<T> result;
    
    /**
     * Constructs a QueryResult object with the specified time, status, and result.
     * @param time The time of the query execution.
     * @param status The status of the query execution.
     * @param result The result of the query execution.
     */
    public QueryResult(String time, String status, List<T> result) {
        this.time = time;
        this.status = status;
        this.result = result;
    }

    /**
     * Constructs an empty QueryResult object.
     */
    public QueryResult() {
    }

    /**
     * Gets the time of the query execution.
     * @return The time of the query execution.
     */
    public String getTime() {
        return time;
    }

    /**
     * Gets the status of the query execution.
     * @return The status of the query execution.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the result of the query execution.
     * @return The result of the query execution.
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * Parses a JSON array into a list of QueryResult objects with the specified type.
     * @param <T> The type of the query result.
     * @param type The class object representing the type of the query result.
     * @param json The JSON array to parse.
     * @return A list of QueryResult objects with the specified type.
     */
    public static <T> List<QueryResult<T>> parseArray(Class<T> type, String json) {
        Type queryResultType = TypeToken.getParameterized(QueryResult.class, type).getType();
        Type resultType = TypeToken.getParameterized(List.class, queryResultType).getType();
        try {
            return new Gson().fromJson(json, resultType);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(json);
            return null;
        }
    }

    /**
     * Parses a JSON array into a list of QueryResult objects with the specified type.
     * @param <T> The type of the query result.
     * @param type The type of the query result.
     * @param json The JSON array to parse.
     * @return A list of QueryResult objects with the specified type.
     * @throws SurrealException If the status of the first query result is not "OK".
     */
    public static <T> List<QueryResult<T>> parseArray(Type type, String json) {
        JSONArray jsonObj = new JSONArray(json);
        if(!jsonObj.getJSONObject(0).getString("status").equals("OK")){
            throw new SurrealException(jsonObj.getJSONObject(0).getString("result"));
        }

        Type queryResultType = TypeToken.getParameterized(QueryResult.class, type).getType();
        Type resultType = TypeToken.getParameterized(List.class, queryResultType).getType();
        return new Gson().fromJson(json, resultType);
    }

    /**
     * Returns a string representation of the QueryResult object.
     * @return A string representation of the QueryResult object.
     */
    @Override
    public String toString(){
        return "QueryResult [time=" + time + ", status=" + status + ", result=" + result + "]";
    }
}
