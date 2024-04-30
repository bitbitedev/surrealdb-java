package dev.bitbite.surrealdb;

public class Person {
    public String name;
    public String id;

    public Person(String name) {
        this.name = name;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User(name=" + this.name + ", id=" + this.id + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Person)) {
            return false;
        }
        Person user = (Person) obj;
        return user.name.equals(this.name) && user.id.equals(this.id);
    }

    public void setName(String name) {
        this.name = name;
    }
}
