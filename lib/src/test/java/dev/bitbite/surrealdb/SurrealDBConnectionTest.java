package dev.bitbite.surrealdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_METHOD)
public class SurrealDBConnectionTest {

    private static SurrealDBConnection conn;

    @BeforeAll
    public static void init() {
        conn = new SurrealDBConnection(URI.create("http://localhost:8000"));
        conn.use("test", "test");
    }
    
    @Test
    public void testSurrealDBConnection() {
        assertTrue(conn.getStatus());
        assertTrue(conn.getHealth());
        assertEquals("surrealdb-1.4.2", conn.getVersion());
    }

    @Test
    void testSignin() {
        assertTrue(conn.signin(
            "root", 
            "pass")
        );
    }

    @Test
    void testScopeSignin() {
        assertTrue(conn.signin("user_scope", Map.of("username","test","password","test")));
    }

    @Test
    void testCRUD() {
        testSignin();
        conn.delete(Person.class, "person");

        Person person = new Person("netcode");
        var createResult = conn.create("person", person).get(0);
        assertEquals("OK", createResult.getStatus());
        assertEquals(person.getName(), createResult.getResult().get(0).getName());
        person = createResult.getResult().get(0);

        var selectResult = conn.select(Person.class, "person");
        assertEquals("OK", selectResult.get(0).getStatus());
        assertEquals(person, selectResult.get(0).getResult().get(0));

        var selectResult2 = conn.select(Person.class, "person", person.getId().split(":")[1]);
        assertEquals("OK", selectResult2.get(0).getStatus());
        assertEquals(person, selectResult2.get(0).getResult().get(0));

        person.setName("netcode2");
        var updateResult = conn.update("person", person).get(0);
        assertEquals("OK", updateResult.getStatus());
        assertEquals(person.getName(), updateResult.getResult().get(0).getName());

        var deleteResult = conn.delete(Person.class, "person", person.getId().split(":")[1]).get(0);
        assertEquals("OK", deleteResult.getStatus());
        assertEquals(person, deleteResult.getResult().get(0));
    }

    public static void main(String[] args) {
        var conn = new SurrealDBConnection(URI.create("http://localhost:8000"));
        conn.use("test", "test");
        // conn.signin("user_scope", Map.of("username","test","password","test"));
        conn.signin("root", "pass");
        //conn.select(User.class, "user");
    }

}
