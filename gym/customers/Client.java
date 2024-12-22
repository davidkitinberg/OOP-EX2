package gym.customers;

import gym.Exception.InvalidAgeException;
import gym.management.Gym;
import gym.management.Secretary;
import gym.management.Sessions.Observer;

import java.util.ArrayList;
import java.util.List;

public class Client extends Person implements Observer {

    private List<String> notifications = new ArrayList<>();

    public Client(String name, int balance, Gender gender, String dateOfBirth) throws InvalidAgeException {
        super(name, balance, gender, dateOfBirth); // This can throw InvalidAgeException

    }
    public List<String> getNotifications() {
        return notifications;
    }

    @Override
    public void update(String message) {
        notifications.add(message); // Standard method for receiving notifications
    }


    @Override
    public String toString() {
        return "ID: " + getID() +
                " | Name: " + getName() +
                " | Gender: " + getGender() +
                " | Birthday: " + getDateOfBirth() +
                " | Age: " + getAge() +
                " | Balance: " + getBalance();
    }

}

