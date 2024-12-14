package gym.customers;

import gym.Exception.InvalidAgeException;

import java.util.ArrayList;
import java.util.List;

public class Client extends Person {
    //private double balance;
    private List<String> notifications = new ArrayList<>();

    public Client(String name, int balance, Gender gender, String dateOfBirth) throws InvalidAgeException {
        super(name, balance, gender, dateOfBirth); // This can throw InvalidAgeException
        //this.balance = balance;
    }


    public void receiveNotification(String message) {
        notifications.add(message);
    }

    public List<String> getNotifications() {
        return notifications;
    }


    @Override
    public String toString() {
        return "ID: " + this.hashCode() +
                " | Name: " + getName() +
                " | Gender: " + getGender() +
                " | Birthday: " + getDateOfBirth() +
                " | Age: " + getAge() +
                " | Balance: " + getBalance();
    }

}

