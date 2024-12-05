package gym.management;

import gym.Exception.InvalidAgeException;
import gym.customers.Person;

public class Gym {
    private static Gym instance;
    private String name;
    private Secretary secretary;

    private Gym() {}

    public static Gym getInstance() {
        if (instance == null) {
            instance = new Gym();
        }
        return instance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecretary(Person person, double salary) {
        try {
            this.secretary = new Secretary(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth().toString(), salary);
        } catch (InvalidAgeException e) {
            throw new RuntimeException("Invalid age for secretary: " + person.getName(), e);
        }
    }


    public Secretary getSecretary() {
        return secretary;
    }

    @Override
    public String toString() {
        return "Gym Name: " + name + "\nSecretary: " + secretary.getName();
    }
}
