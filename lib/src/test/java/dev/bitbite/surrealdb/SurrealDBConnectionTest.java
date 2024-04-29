package dev.bitbite.surrealdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;

import org.junit.jupiter.api.Test;

public class SurrealDBConnectionTest {
    
    @Test
    public void testSurrealDBConnection() {
        SurrealDBConnection conn = new SurrealDBConnection(URI.create("http://10.2.40.1:8000"));
        assertTrue(conn.getStatus());
        assertTrue(conn.getHealth());
        assertEquals("surrealdb-1.4.2", conn.getVersion());
    }

    public static void main(String[] args) {
        var test = new SurrealDBConnectionTest();
        test.testSurrealDBConnection();
    }

}
