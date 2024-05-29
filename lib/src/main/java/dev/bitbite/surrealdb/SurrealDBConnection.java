package dev.bitbite.surrealdb;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import dev.bitbite.surrealdb.exception.AuthenticationException;

import org.json.JSONObject;

import com.google.gson.Gson;

import dev.bitbite.surrealdb.query.Query;
import dev.bitbite.surrealdb.query.QueryResult;

/**
 * Represents a connection to the SurrealDB server.
 * It provides methods for performing various operations such as authentication, querying, and data manipulation.
 */
public class SurrealDBConnection {

    private URL host;
    private HttpClient client;
    private String namespace, database;
    private String token;
    
    /**
     * Create a new connection to the SurrealDB server
     * @param host URI of the server
     */
    public SurrealDBConnection(URI host) {
        URL url = null;
        try {
            url = host.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        if(!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
            throw new IllegalArgumentException("The protocol of the host must be http or https");
        }
        this.host = url;
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Set the namespace for the connection<br />
     * <p>
     * <i>When using root authentication: This should be used <b>AFTER</b> signing in</i>
     * </p>
     * @param namespace to use
     */
    public void use(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Set the namespace and database for the connection<br />
     * <p>
     * <i>When using root authentication: This should be used <b>AFTER</b> signing in</i>
     * </p>
     * @param namespace to use
     * @param database to use
     */
    public void use(String namespace, String database) {
        this.namespace = namespace;
        this.database = database;
    }

    /**
     * Get the status of the SurrealDB server
     * @return true if the server is running, false otherwise
     */
    public boolean getStatus() {
        HttpResponse<String> response = GET("/status");
        return response.statusCode() == 200;
    }

    /**
     * Get the health of the SurrealDB server
     * @return true if the server is healthy, false otherwise
     */
    public boolean getHealth() {
        HttpResponse<String> response = GET("/health");
        return response.statusCode() == 200;
    }

    /**
     * Get the version of the SurrealDB server
     * @return version of the server
     */
    public String getVersion() {
        HttpResponse<String> response = GET("/version");
        return response.body();
    }

    /**
     * Sign in to the SurrealDB server using root authentication
     * @param user username
     * @param pass password
     * @return true if the sign in was successful, false otherwise
     */
    public boolean signin(String user, String pass) {
        JSONObject data = new JSONObject();

        if(this.namespace != null)
            data.put("ns", this.namespace);

        if(this.database != null)
            data.put("db", this.database);

        data.put("user", user);
        data.put("pass", pass);

        HttpResponse<String> response = POST("/signin", data.toString());
        if(response.body().equals("There was a problem with authentication"))
            return false;

        JSONObject responseJson = new JSONObject(response.body());
        if(responseJson.getInt("code") != 200)
            return false;
        if(!responseJson.has("token"))
            return false;

        this.token = responseJson.getString("token");
        return true;
    }

    /**
     * Sign in to the SurrealDB server using scope authentication
     * @param scope scope to use
     * @param args arguments for the scope
     * @return true if the sign in was successful, false otherwise
     */
    public boolean signin(String scope, Map<String, String> args) {
        if(this.namespace == null || this.database == null){
            System.err.println("namespace or database not defined");
            return false;
        }

        JSONObject data = new JSONObject();
        data.put("ns", this.namespace);
        data.put("db", this.database);
        data.put("sc", scope);
        args.entrySet().stream().forEach(arg -> data.put(arg.getKey(), arg.getValue()));

        JSONObject response = new JSONObject(POST("/signin", data.toString()).body());
        if(response.getInt("code") != 200)
            return false;
        if(!response.has("token")) 
            return false;

        this.token = response.getString("token");
        return true;
    }

    /**
     * Sign up to the SurrealDB server using root authentication
     * @param scope scope to use
     * @param args  arguments for the scope
     * @return  true if the sign up was successful, false otherwise
     */
    public boolean signup(String scope, Map<String, String> args) {
        if(this.namespace == null || this.database == null){
            System.err.println("namespace or database not defined");
            return false;
        }

        JSONObject data = new JSONObject();
        data.put("ns", this.namespace);
        data.put("db", this.database);
        data.put("sc", scope);
        args.entrySet().stream().forEach(arg -> data.put(arg.getKey(), arg.getValue()));

        JSONObject response = new JSONObject(POST("/signup", data.toString()).body());
        if(response.getInt("code") != 200)
            return false;
        if(!response.has("token")) 
            return false;

        this.token = response.getString("token");
        return true;
    }
    
    /**
     * Select data from the SurrealDB server
     * @param <T> type of the data
     * @param type class of the data
     * @param thing name of the table to select from
     * @return query result
     */
    public <T> QueryResult<T> select(Class<T> type, String thing){
        HttpResponse<String> response = GET("/key/"+thing);

        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body()).get(0);
    }

    /**
     * Select data from the SurrealDB server
     * @param <T> type of the data
     * @param type class of the data
     * @param table name of the table to select from
     * @param id id of the data to select
     * @return query result
     */
    public <T> QueryResult<T> select(Class<T> type, String table, String id){
        HttpResponse<String> response = GET("/key/"+table+"/"+id);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body()).get(0);
    }

    /**
     * Create data in the SurrealDB server
     * @param <T> type of the data
     * @param table name of the table to create data in
     * @param data data to create
     * @return query result
     */
    public <T> List<QueryResult<T>> create(String table, T data){
        HttpResponse<String> response = POST("/key/"+table, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray((Type) data.getClass(), response.body());
    }

    /**
     * Create data in the SurrealDB server
     * @param <T> type of the data
     * @param table name of the table to create data in
     * @param id id of the data to create
     * @param data data to create
     * @return query result
     */
    public <T> List<QueryResult<T>> create(String table, String id, T data){
        HttpResponse<String> response = POST("/key/"+table+"/"+id, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray((Type) data.getClass(), response.body());
    }

    /**
     * Update data in the SurrealDB server
     * @param <T> type of the data
     * @param table name of the table to update data in
     * @param data data to update
     * @return query result
     */
    public <T> List<QueryResult<T>> update(String table, T data){
        HttpResponse<String> response = PUT("/key/"+table, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());
        
        return QueryResult.parseArray((Type) data.getClass(), response.body());
    }

    /**
     * Update data in the SurrealDB server
     * @param <T> type of the data
     * @param table name of the table to update data in
     * @param id id of the data to update
     * @param data data to update
     * @return query result
     */
    public <T> List<QueryResult<T>> update(String table, String id, T data){
        HttpResponse<String> response = PUT("/key/"+table+"/"+id, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray((Type) data.getClass(), response.body());
    }

    /**
     * Merge data in the SurrealDB server. This updates all records of a table with the data provided.
     * @param <T> type of the data
     * @param table name of the table to merge data in
     * @param data data to merge
     * @return query result
     */
    public <T> List<QueryResult<T>> merge(String table, Object data){
        HttpResponse<String> response = PATCH("/key/"+table, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray((Type) data.getClass(), response.body());
    }

    /**
     * Merge data in the SurrealDB server. This updates a single record of a table with the data provided.
     * @param <T> type of the data
     * @param table name of the table to merge data in
     * @param id id of the data to merge
     * @param data data to merge
     * @return query result
     */
    public <T> List<QueryResult<T>> merge(String table, String id, Object data){
        HttpResponse<String> response = PATCH("/key/"+table+"/"+id, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray((Type) data.getClass(), response.body());
    }

    /**
     * Delete all data in a table from the SurrealDB server
     * @param <T> type of the data
     * @param type class of the data
     * @param table name of the table to delete data from
     * @return query result
     */
    public <T> List<QueryResult<T>> delete(Class<T> type, String table){
        HttpResponse<String> response = DELETE("/key/"+table);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    /**
     * Delete a record from the SurrealDB server
     * @param <T> type of the data
     * @param type class of the data
     * @param table name of the table to delete data from
     * @param id id of the data to delete
     * @return query result
     */
    public <T> List<QueryResult<T>> delete(Class<T> type, String table, String id){
        HttpResponse<String> response = DELETE("/key/"+table+"/"+id);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    /**
     * Query the SurrealDB server
     * @param <T> type of the data
     * @param type class of the data
     * @param query query to perform
     * @return query result
     */
    public <T> List<QueryResult<T>> query(Class<T> type, String query){
        HttpResponse<String> response = POST("/sql", query);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    /**
     * Query the SurrealDB server
     * @param <T> type of the data
     * @param type class of the data
     * @param query query to perform
     * @return query result
     */
    public <T> List<QueryResult<T>> query(Class<T> type, Query query){
        return query(type, query.toString());
    }

    private HttpResponse<String> GET(String endpoint) {
        try {
            return this.client.send(
                createRequest(endpoint).GET().build(),
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> POST(String endpoint, String data){
        try {
            HttpRequest.Builder request = createRequest(endpoint)
                .POST(HttpRequest.BodyPublishers.ofString(data));

            return this.client.send(
                request.build(),
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> PUT(String endpoint, String data){
        try {
            HttpRequest.Builder request = createRequest(endpoint)
                .PUT(HttpRequest.BodyPublishers.ofString(data));

            return this.client.send(
                request.build(),
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> PATCH(String endpoint, String data){
        try {
            HttpRequest.Builder request = createRequest(endpoint)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(data));

            return this.client.send(
                request.build(),
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> DELETE(String endpoint){
        try {
            HttpRequest.Builder request = createRequest(endpoint)
                .DELETE();

            return this.client.send(
                request.build(),
                HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpRequest.Builder createRequest(String endpoint) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
            .uri(URI.create(this.host+endpoint))
            .header("Accept", "application/json");

        if(this.token != null)
            request.header("Authorization", "Bearer "+this.token);

        if(this.namespace != null)
            request.header("ns", this.namespace);

        if(this.database != null)
            request.header("db", this.database);

        return request;
    }

}
