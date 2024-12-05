package gym.management;

import gym.customers.*;
import gym.Exception.*;
import gym.management.Sessions.*;

import java.util.*;

public class Secretary extends Person {
    private double salary;
    private List<Client> clients = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private List<String> actions = new ArrayList<>();

    public Secretary(String name, int age, Gender gender, String dateOfBirth, double salary) throws InvalidAgeException {
        super(name, age, gender, dateOfBirth);
        this.salary = salary;
    }

    public Client registerClient(Person person) throws DuplicateClientException, InvalidAgeException {
        for (Client client : clients) {
            if (client.getName().equals(person.getName())) {
                throw new DuplicateClientException("Client already registered: " + person.getName());
            }
        }
        Client client = new Client(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), 1000.0);
        clients.add(client);
        actions.add("Registered client: " + client.getName());
        return client;
    }

    public void unregisterClient(Client client) throws ClientNotRegisteredException {
        if (!clients.contains(client)) {
            throw new ClientNotRegisteredException("Client not found: " + client.getName());
        }
        clients.remove(client);
        actions.add("Unregistered client: " + client.getName());
    }


    public Instructor hireInstructor(Person person, double hourlyWage, List<SessionType> qualifications) {
        Instructor instructor = new Instructor(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), hourlyWage, qualifications);
        instructors.add(instructor);
        actions.add("Hired instructor: " + instructor.getName());
        return instructor;
    }

    public Session addSession(SessionType type, String dateTime, ForumType forum, Instructor instructor) throws InstructorNotQualifiedException {
        if (!instructor.isQualifiedFor(type)) {
            throw new InstructorNotQualifiedException("Instructor " + instructor.getName() + " is not qualified for " + type);
        }
        Session session = new Session(type, dateTime, forum, instructor);
        sessions.add(session);
        actions.add("Added session: " + type + " on " + dateTime);
        return session;
    }

    public void registerClientToLesson(Client client, Session session) throws DuplicateClientException {
        if (session.addParticipant(client)) {
            actions.add("Registered client " + client.getName() + " to session " + session.getType());
        } else {
            throw new DuplicateClientException("Client already registered for session: " + session.getType());
        }
    }

    public void notify(Session session, String message) {
        for (Client client : session.getParticipants()) {
            client.receiveNotification(message);
        }
        actions.add("Notified clients of session: " + session.getType());
    }

    public void notify(String date, String message) {
        actions.add("Notified all clients on " + date + ": " + message);
    }

    public void notify(String message) {
        actions.add("Notified all clients: " + message);
    }

    public void paySalaries() {
        actions.add("Paid salaries to instructors.");
    }

    public void printActions() {
        for (String action : actions) {
            System.out.println(action);
        }
    }
}
