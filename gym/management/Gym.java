package gym.management;

import gym.Exception.InvalidAgeException;
import gym.customers.Person;

public class Gym {
    private static final Gym INSTANCE = new Gym("gym", 1000);
    private String name;
    private Secretary secretary;
    private double gymBalance;

    private Gym(String name, double gymBalance ) {
        if (INSTANCE != null) {
            throw new IllegalStateException("Cannot instantiate singleton class using reflection");
        }
        this.name = name;
        this.gymBalance = gymBalance;
    }

    public static Gym getInstance() {
        return INSTANCE;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSecretary(Person person, double salary) throws InvalidAgeException {
        Secretary.replaceInstance(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), salary);
        secretary = Secretary.getInstance(); // Update the field to point to the new singleton instance
    }

    public void setGymBalance(double gymBalance) {
        this.gymBalance = gymBalance;
    }

    public double getGymBalance() {
        return gymBalance;
    }

    public Secretary getSecretary() {
        return Secretary.getInstance();
    }

    @Override
    public String toString() {
        return "Gym Name: " + name + "\nSecretary: " + secretary.getName();
    }
}
