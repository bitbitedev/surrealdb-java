package dev.bitbite.surrealdb.orm;

/**
 * The Identifiable interface represents an object that can be uniquely identified.
 * Classes that implement this interface must provide a method to retrieve the ID of the object.
 */
public interface Identifiable {
    /**
     * Retrieves the ID of the object.
     *
     * @return the ID of the object
     */
    String getId();
}
