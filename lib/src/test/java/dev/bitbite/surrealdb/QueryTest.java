package dev.bitbite.surrealdb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.bitbite.surrealdb.query.Query;

public class QueryTest {
    
    @Test
    void testQuery() {
        Query query = Query.select("name").from("person").where("name", "=", "netcode");
        assertEquals("SELECT name FROM person WHERE name = 'netcode'", query.toString());
        query = Query.selectAll().from("person").where("name", "=", "netcode");
        assertEquals("SELECT * FROM person WHERE name = 'netcode'", query.toString());
        query = Query.selectAll().from("person").where("name", "INSIDE", new String[] { "netcode", "ThatsNasu"});
        assertEquals("SELECT * FROM person WHERE name INSIDE ['netcode', 'ThatsNasu']", query.toString());
        query = Query.selectAll().from("person").where("age", "NOT INSIDE", new Integer[] { 1,2,3 });
        assertEquals("SELECT * FROM person WHERE age NOT INSIDE [1, 2, 3]", query.toString());
    }

}
