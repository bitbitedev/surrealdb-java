package dev.bitbite.surrealdb;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.bitbite.surrealdb.orm.Repository;

public class ORMTest {

    private static SurrealDBConnection conn;
    
    @BeforeAll
    static void init() {
        conn = new SurrealDBConnection(URI.create("http://localhost:8000"));
        conn.use("test", "test");
        conn.signin("root", "pass");
    }

    @Test
    void testORM() {
        Repository<Person> personRepository = new Repository<>(Person.class, conn);
        personRepository.delete(new Person("netcode"));
        Person person = new Person("netcode");
        personRepository.add(person);
        assertEquals(person.getName(), personRepository.getAll().get(0).getName());
        person = personRepository.getAll().get(0);
        person.setName("newname");
        personRepository.update(person);
        assertEquals(person.getName(), personRepository.get(person.getId()).getName());
        person = personRepository.get(person.getId());
        personRepository.delete(person);
        assertEquals(0, personRepository.getAll().size());
    }

}
