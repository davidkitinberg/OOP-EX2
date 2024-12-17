package gym.management;

import gym.Exception.InvalidAgeException;
import gym.customers.Client;
import gym.customers.Instructor;
import gym.customers.Person;
import gym.management.Sessions.Session;

import java.util.ArrayList;
import java.util.List;

public class Gym {
    private static final Gym INSTANCE = new Gym("gym", 0);
    private String name;
    private Secretary secretary;
    private double gymBalance;
    private static List<String> actions = new ArrayList<>();
    private static List<Session> sessions = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private static List<Instructor> instructors = new ArrayList<>();



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


    public void setSecretary(Person person, int salary) throws InvalidAgeException {
        Secretary.replaceInstance(person.getName(), person.getBalance(), person.getGender(), person.getDateOfBirth(), salary);
        secretary = Secretary.getInstance(); // Update the field to point to the new singleton instance
    }
    public static void addAction(String action) {
        actions.add(action);
    }
    public static List<String> getActions() {
        return actions;
    }
    public static void addSession(Session session) {
        sessions.add(session);
    }
    public static List<Session> getSessions() {
        return sessions;
    }
    public void addClient(Client client) {
        clients.add(client);
    }
    public List<Client> getClients() {
        return clients;
    }
    public void removeClient(Client client) {
        clients.remove(client);
    }
    public void addInstructor(Instructor instructor) {
        instructors.add(instructor);
    }
    public List<Instructor> getInstructors() {
        return instructors;
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
        StringBuilder output = new StringBuilder();
        output.append("Gym Name: ").append(name).append("\n");
        output.append("Gym Secretary: ").append(secretary != null ? secretary.toString() : "None").append("\n");
        output.append("Gym Balance: ").append(gymBalance).append("\n\n");

        output.append("Clients Data:\n");
        for (Client client : clients) {
            output.append(client.toString()).append("\n");
        }

        output.append("\nEmployees Data:\n");
        for (Instructor instructor : instructors) {
            output.append(instructor.toString()).append("\n");
        }
        output.append(secretary.toString()).append("\n");

        output.append("\nSessions Data:\n");
        for (Session session : sessions) {
            output.append(session.toString()).append("\n");
        }

        return output.toString();
    }
}
