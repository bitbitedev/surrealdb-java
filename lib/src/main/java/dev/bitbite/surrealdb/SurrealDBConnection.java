package dev.bitbite.surrealdb;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import dev.bitbite.surrealdb.exception.AuthenticationException;

import org.json.JSONObject;

import com.google.gson.Gson;

import dev.bitbite.surrealdb.query.Query;
import dev.bitbite.surrealdb.query.QueryResult;

public class SurrealDBConnection {

    private URL host;
    private HttpClient client;
    private String namespace, database;
    private String user, pass;
    private String token;
    
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

    public void use(String namespace) {
        this.namespace = namespace;
    }

    public void use(String namespace, String database) {
        this.namespace = namespace;
        this.database = database;
    }

    public boolean getStatus() {
        HttpResponse<String> response = GET("/status");
        return response.statusCode() == 200;
    }

    public boolean getHealth() {
        HttpResponse<String> response = GET("/health");
        return response.statusCode() == 200;
    }

    public String getVersion() {
        HttpResponse<String> response = GET("/version");
        return response.body();
    }

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
        this.user = user;
        this.pass = pass;
        return true;
    }

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
    
    public <T> List<QueryResult<T>> select(Type type, String thing){
        HttpResponse<String> response = GET("/key/"+thing);

        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    public <T> List<QueryResult<T>> select(Type type, String table, String id){
        HttpResponse<String> response = GET("/key/"+table+"/"+id);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    public <T> List<QueryResult<T>> create(String table, T data){
        HttpResponse<String> response = POST("/key/"+table, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(data.getClass(), response.body());
    }

    public <T> List<QueryResult<T>> create(String table, String id, T data){
        HttpResponse<String> response = POST("/key/"+table+"/"+id, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(data.getClass(), response.body());
    }

    public <T> List<QueryResult<T>> update(String table, T data){
        HttpResponse<String> response = PUT("/key/"+table, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());
        
        return QueryResult.parseArray(data.getClass(), response.body());
    }

    public <T> List<QueryResult<T>> update(String table, String id, Object data){
        HttpResponse<String> response = PUT("/key/"+table+"/"+id, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(data.getClass(), response.body());
    }

    public <T> List<QueryResult<T>> merge(String table, Object data){
        HttpResponse<String> response = PATCH("/key/"+table, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(data.getClass(), response.body());
    }

    public <T> List<QueryResult<T>> merge(String table, String id, Object data){
        HttpResponse<String> response = PATCH("/key/"+table+"/"+id, new Gson().toJson(data));
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(data.getClass(), response.body());
    }

    public <T> List<QueryResult<T>> delete(Type type, String table){
        HttpResponse<String> response = DELETE("/key/"+table);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    public <T> List<QueryResult<T>> delete(Type type, String table, String id){
        HttpResponse<String> response = DELETE("/key/"+table+"/"+id);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    public <T> List<QueryResult<T>> query(Type type, String query){
        HttpResponse<String> response = POST("/sql", query);
        
        if(response.body().equals("There was a problem with authentication"))
            throw new AuthenticationException(response.body());

        return QueryResult.parseArray(type, response.body());
    }

    public <T> List<QueryResult<T>> query(Type type, Query query){
        return query(type, query.toString());
    }

    public HttpResponse<String> GET(String endpoint) {
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

    public HttpResponse<String> POST(String endpoint, String data){
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

    public HttpResponse<String> PUT(String endpoint, String data){
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

    public HttpResponse<String> PATCH(String endpoint, String data){
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

    public HttpResponse<String> DELETE(String endpoint){
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

    public HttpRequest.Builder createRequest(String endpoint) {
        HttpRequest.Builder request = HttpRequest.newBuilder()
            .uri(URI.create(this.host+endpoint))
            .header("Accept", "application/json");

        if(this.token != null){
            if(this.user != null && this.pass != null) {
                String auth = Base64.getEncoder().encodeToString((this.user+":"+this.pass).getBytes());
                request.header("Authorization", "Basic "+auth);
            } else {
                request.header("Authorization", "Bearer "+this.token);
            }
        }

        if(this.namespace != null)
            request.header("ns", this.namespace);

        if(this.database != null)
            request.header("db", this.database);

        return request;
    }

}
