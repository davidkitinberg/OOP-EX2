package gym.management;

import gym.Exception.InvalidAgeException;
import gym.customers.Client;
import gym.customers.Instructor;
import gym.customers.Person;
import gym.management.Sessions.Session;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Gym {
    private static final Gym INSTANCE = new Gym("gym", 0);
    private String name;
    private Secretary secretary;
    private int gymBalance;
    private static List<String> actions = new ArrayList<>();
    private static List<Session> sessions = new ArrayList<>();
    private  List<Client> clients = new ArrayList<>();
    private static List<Instructor> instructors = new ArrayList<>();
    private static HashSet<Person> persons = new HashSet<>();



    private Gym(String name, int gymBalance ) {
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
        Client client = getEquivalentClient(person);
        if(client != null) {
            Secretary.replaceInstance(client.getName(), client.getBalance(), client.getGender(), client.getDateOfBirth(), salary);
        }
        else
        {
            addPerson(person);
            Secretary.replaceInstance(person.getName(), person.getBalance(), person.getGender(), person.getDateOfBirth(), salary);
        }
        secretary = Secretary.getInstance(); // Update the field to point to the new singleton instance
    }
    public void addAction(String action) {
        actions.add(action);
    }
    public List<String> getActions() {
        return actions;
    }
    public void addPerson(Person person) {
        persons.add(person);
    }
    public void addSession(Session session) {
        sessions.add(session);
    }
    public List<Session> getSessions() {
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

    public void setGymBalance(int gymBalance) {
        this.gymBalance = gymBalance;
    }

    public int getGymBalance() {
        return gymBalance;
    }

    public Secretary getSecretary() {
        return Secretary.getInstance();
    }
    public Client getEquivalentClient(Object obj) {
        for (Client client : clients) {
            if (obj instanceof Person) {
                Person person = (Person) obj;
                if (client.getName().equals(person.getName()) &&
                        client.getGender().equals(person.getGender()) &&
                        client.getDateOfBirth().equals(person.getDateOfBirth())) {
                    return client;
                }
            } else if (obj instanceof Instructor) {
                Instructor instructor = (Instructor) obj;
                if (client.getName().equals(instructor.getName()) &&
                        client.getGender().equals(instructor.getGender()) &&
                        client.getDateOfBirth().equals(instructor.getDateOfBirth())) {
                    return client;
                }
            } else if (obj instanceof Secretary) {
                Secretary secretary = (Secretary) obj;
                if (client.getName().equals(secretary.getName()) &&
                        client.getGender().equals(secretary.getGender()) &&
                        client.getDateOfBirth().equals(secretary.getDateOfBirth())) {
                    return client;
                }
            }
        }
        return null;
    }

    public  List<Person> getEquivilants(Object obj) {
        List<Person> equivalents = new ArrayList<>();
        Person target = (Person) obj;
        List<Person> allPersons = new ArrayList<>();
        allPersons.addAll(clients);
        allPersons.addAll(instructors);
        allPersons.addAll(persons);
        if(secretary != null) {
            allPersons.add(secretary);
        }
        for (Person person : allPersons) {
            if (person.getID() == target.getID()) {

                // Add to the result if the properties match
                equivalents.add(person);
            }
        }
        return equivalents;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Gym Name: ").append(name).append("\n");
        output.append("Gym Secretary: ").append(secretary != null ? secretary.toString() : "None").append("\n");
        output.append("Gym Balance: ").append(gymBalance).append("\n\n");

        output.append("Clients Data:\n");
        for (int i = 0; i < clients.size(); i++) {
            output.append(clients.get(i).toString());
            if (i < clients.size() - 1) {
                output.append("\n");
            }
        }

        output.append("\n\nEmployees Data:\n");
        for (int i = 0; i < instructors.size(); i++) {
            output.append(instructors.get(i).toString());
            output.append("\n");
        }
        output.append(secretary.toString()).append("\n");

        output.append("\nSessions Data:\n");
        for (int i = 0; i < sessions.size(); i++) {
            output.append(sessions.get(i).toString());
            if (i < sessions.size() - 1) {
                output.append("\n");
            }
        }

        return output.toString();
    }
}
