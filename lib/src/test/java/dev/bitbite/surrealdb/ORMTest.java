package dev.bitbite.surrealdb;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.bitbite.surrealdb.exception.SurrealException;
import dev.bitbite.surrealdb.orm.Repository;

public class ORMTest {

    private static SurrealDBConnection conn;
    
    @BeforeAll
    static void init() {
        conn = new SurrealDBConnection(URI.create("http://localhost:8000"));
        conn.signin("root", "pass");
        conn.use("test", "test");
    }

    @Test
    void testORM() {
        Repository<Person> personRepository = new Repository<>(Person.class, conn);
        personRepository.delete(new Person("netcode"));
        Person person = new Person("netcode");
        assertEquals(person.getName(), personRepository.add(person).getName());
        assertEquals(person.getName(), personRepository.getAll().get(0).getName());
        // Adding an entry that violates the unique constraint should throw an exception
        assertThrows(SurrealException.class, () -> personRepository.add(new Person("netcode")));
        person = personRepository.getAll().get(0);
        person.setName("newname");
        assertEquals(person, personRepository.update(person));
        assertEquals(person.getName(), personRepository.get(person.getId()).getName());
        person = personRepository.get(person.getId());
        assertEquals(person, personRepository.delete(person));
        assertEquals(0, personRepository.getAll().size());
    }

}
