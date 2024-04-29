package dev.bitbite.surrealdb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SurrealDBConnection {

    private URL host;
    private HttpClient client;
    
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

    public HttpResponse<String> GET(String endpoint) {
        try {
            return this.client.send(createRequest(endpoint).GET().build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpRequest.Builder createRequest(String endpoint) {
        return HttpRequest.newBuilder()
            .uri(URI.create(this.host+endpoint));
    }

}
