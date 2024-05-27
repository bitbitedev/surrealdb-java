package dev.bitbite.surrealdb.query;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dev.bitbite.surrealdb.exception.SurrealException;

public class QueryResult<T> {
    
    private String time;
    private String status;
    private List<T> result;
    
    public QueryResult(String time, String status, List<T> result) {
        this.time = time;
        this.status = status;
        this.result = result;
    }

    public QueryResult() {
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public List<T> getResult() {
        return result;
    }

    public static <T> List<QueryResult<T>> parseArray(Class<T> type, String json) {
        Type queryResultType = TypeToken.getParameterized(QueryResult.class, type).getType();
        Type resultType = TypeToken.getParameterized(List.class, queryResultType).getType();
        try {
            return new Gson().fromJson(json, resultType);
        } catch (Exception e) {
            System.out.println(json);
            return null;
        }
    }

    public static <T> List<QueryResult<T>> parseArray(Type type, String json) {
        JSONArray jsonObj = new JSONArray(json);
        if(!jsonObj.getJSONObject(0).getString("status").equals("OK")){
            throw new SurrealException(jsonObj.getJSONObject(0).getString("result"));
        }

        Type queryResultType = TypeToken.getParameterized(QueryResult.class, type).getType();
        Type resultType = TypeToken.getParameterized(List.class, queryResultType).getType();
        return new Gson().fromJson(json, resultType);
    }

    @Override
    public String toString(){
        return "QueryResult [time=" + time + ", status=" + status + ", result=" + result + "]";
    }
}
