package gym.management;

import gym.customers.*;
import gym.Exception.*;
import gym.management.Sessions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Secretary extends Person {
    private double salary;
    private List<Client> clients = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Session> sessions = new ArrayList<>();
    private List<String> actions = new ArrayList<>();
    private static Secretary instance;
    private static List<Client> formerSecretaries = new ArrayList<>();


    // Private constructor to prevent instantiation
    private Secretary(String name, int age, Gender gender, String dateOfBirth, double salary) throws InvalidAgeException {
        super(name, age, gender, dateOfBirth);
        this.salary = salary;
        actions.add("A new secretary has started working at the gym: " + name);

    }
    public static Secretary getInstance() {
        return instance; // Return the singleton instance
    }


    public static void replaceInstance(String name, int age, Gender gender, String dateOfBirth, double salary) throws InvalidAgeException {
        if (instance != null) {
            Client formerSecreraty = new Client(instance.getName(), instance.getAge(), instance.getGender(), instance.getDateOfBirth(), 1000 );
            formerSecretaries.add(formerSecreraty);
        }
        instance = new Secretary(name, age, gender, dateOfBirth, salary);
    }


    // Ensure this secretary has access rights
    private void ensureAccess() {
        if (isContained(formerSecretaries,instance)) {
            throw new NullPointerException("Error: Former secretaries are not permitted to perform actions");
        }

    }

    public Client registerClient(Person person) throws InvalidAgeException, DuplicateClientException {
        ensureAccess();
        try {
            if (person.getAge() < 18) {
                throw new InvalidAgeException("Error: Client must be at least 18 years old to register");
            }

            // check if client is already registered
            if(isContained(clients, person))
            {
                throw new DuplicateClientException("Error: The client is already registered");
            }

            Client client = new Client(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), 1000.0);
            clients.add(client);
            actions.add("Registered new client: " + client.getName());
            return client;
        } catch (InvalidAgeException | DuplicateClientException e) {
            //actions.add("Failed to register client: " + person.getName() + " - " + e.getMessage());
            System.out.println(e.getMessage());
            return null; // Return null if registration fails
        }
    }




    public void unregisterClient(Client client) throws ClientNotRegisteredException {
        ensureAccess();
        if (!clients.contains(client)) {
            throw new ClientNotRegisteredException("Error: Registration is required before attempting to unregister");
        }
        clients.remove(client);
        actions.add("Unregistered client: " + client.getName());
    }


    public Instructor hireInstructor(Person person, double hourlyWage, List<SessionType> qualifications) throws InvalidAgeException {
        ensureAccess();
        Instructor instructor = new Instructor(person.getName(), person.getAge(), person.getGender(), person.getDateOfBirth(), hourlyWage, qualifications);
        instructors.add(instructor);
        actions.add("Hired new instructor: " + instructor.getName());
        return instructor;
    }

    public Session addSession(SessionType type, String dateTime, ForumType forum, Instructor instructor) throws InstructorNotQualifiedException {

    try
    {
        // Use the factory to create the session
        Session session = SessionFactory.createSession(type, dateTime, forum, instructor);

        // Add the session to the list and record the action
        sessions.add(session);
        actions.add("Created new session: " + type.getName() + " on " + dateTime + " with instructor: " + instructor.getName());

        return session; // Return the created session
    } catch (IllegalArgumentException e)
    {
        // Handle invalid session creation
        return null; // Return null if session creation fails
    }
    }



    public void registerClientToLesson(Client client, Session session) throws ClientNotRegisteredException,DuplicateClientException  {
        ensureAccess();
        try {
            // check if client is within the clients list
            if(!isContained(clients, client))
            {
                throw new ClientNotRegisteredException("Error: The client is not registered with the gym and cannot enroll in lessons");
            }

            // check if client is already registered to this lesson
            if(isContained(session.getParticipants(), client))
            {
                throw new DuplicateClientException("Error: The client is already registered for this lesson");
            }

            // check if lesson is expired
            if (session.isExpired()) {
                actions.add("Failed registration: Session is not in the future");
                return;
            }

            // check if client is met with lesson's forum type

            if (!session.isForumCompatible(client))
            {
                if (session.getForum() == ForumType.Seniors)
                {
                    actions.add("Failed registration: Client doesn't meet the age requirements for this session (" + session.getForum() + ")");
                }
                else if (session.getForum() == ForumType.Male || session.getForum() == ForumType.Female)
                {
                    actions.add("Failed registration: Client's gender doesn't match the session's gender requirements (" + session.getForum() + ")");
                }
                return;
                //return; // Exit the method since the client is not compatible with the session
            }

            // check if this session has enough space for another client
            if (!session.hasSpace()) {
                actions.add("Failed registration: No available spots for session");
                return;
            }
            // check if client has sufficient balance for this lesson
            if (client.getBalance() < session.getPrice()) {
                actions.add("Failed registration: Client doesn't have enough balance");
                return;
            }
            session.addParticipant(client);
            client.setBalance(client.getBalance() - session.getPrice());
            Gym.getInstance().setGymBalance(Gym.getInstance().getGymBalance() + session.getPrice());
            actions.add("Registered client: " + client.getName() + " to session: " + session.getType() + " on " + session.getDateTime());
        } catch (ClientNotRegisteredException | DuplicateClientException | IllegalArgumentException | InsufficientFundsException e) {
            actions.add("Failed to register client: " + client.getName() + " to session: " + session.getType() + " - " + e.getMessage());
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }




    public void registerClientToLesson2(Client client, Session session) {
        //ensureAccess();

        try {
            if (!clients.contains(client)) {
                throw new ClientNotRegisteredException("Error: The client is not registered with the gym and cannot enroll in lessons");
            }
        } catch (ClientNotRegisteredException e) {
            actions.add("Failed registration: " + e.getMessage());
            return; // Exit the method since the client is not registered
        }

        try {
            List<Client> participants = session.getParticipants();
            if (participants.contains(client)) {
                throw new DuplicateClientException("Error: The client is already registered for this lesson");
            }
        } catch (DuplicateClientException e) {
            actions.add("Failed registration: " + e.getMessage());
            return; // Exit the method since the client is already registered
        }

        try {
            if (session.isExpired()) {
                throw new IllegalArgumentException("Error: Session has already passed.");
            }
        } catch (IllegalArgumentException e) {
            actions.add("Failed registration: " + e.getMessage());
            return; // Exit the method since the session is expired
        }

        try {
            if (!session.isForumCompatible(client)) {
                if (session.getForum() == ForumType.Seniors) {
                    actions.add("Failed registration: Client doesn't meet the age requirements for this session (" + session.getForum() + ")");
                } else if (session.getForum() == ForumType.Male || session.getForum() == ForumType.Female) {
                    actions.add("Failed registration: Client's gender doesn't match the session's gender requirements (" + session.getForum() + ")");
                }
                throw new IllegalArgumentException("Client is not compatible with the session forum.");
            }
        } catch (IllegalArgumentException e) {
            actions.add("Failed registration: " + e.getMessage());
            return; // Exit the method since the client is not compatible with the session
        }

        try {
            if (!session.hasSpace()) {
                actions.add("Failed registration: No available spots for session");
                throw new IllegalArgumentException("No space left in the session.");
            }
        } catch (IllegalArgumentException e) {
            actions.add("Failed registration: " + e.getMessage());
            return; // Exit the method since no space is available
        }

        try {
            if (client.getBalance() < session.getPrice()) {
                actions.add("Failed registration: Client doesn't have enough balance");
                throw new InsufficientFundsException("Client does not have enough balance.");
            }
        } catch (InsufficientFundsException e) {
            actions.add("Failed registration: " + e.getMessage());
            return; // Exit the method since the client doesn't have enough balance
        }

        try {
            if (session.addParticipant(client)) {
                // Reduce client balance
                client.setBalance(client.getBalance() - session.getPrice());
                // Update Gym balance
                Gym.getInstance().setGymBalance(Gym.getInstance().getGymBalance() + session.getPrice());
                actions.add("Registered client: " + client.getName() + " to session: " + session.getType() + " on " + session.getDateTime() + " for price: " + session.getPrice());
            } else {
                actions.add("Failed registration: Session is not in the future");
                throw new DuplicateClientException("Client already registered for session: " + session.getType());
            }
        } catch (DuplicateClientException e) {
            actions.add("Failed registration: " + e.getMessage());
        }
    }

    public void registerClientToLesson1(Client client, Session session) throws DuplicateClientException,ClientNotRegisteredException  {
        //ensureAccess();
        if (!clients.contains(client))
        {
            throw new ClientNotRegisteredException("The client is not registered with the gym and cannot enroll in lessons");
        }
        List<Client> participants = session.getParticipants();
        if(participants.contains(client))
        {
            throw new DuplicateClientException("The client is already registered for this lesson");
        }
        if (session.isExpired())
        {
            throw new IllegalArgumentException("Session has already passed.");
        }
        if (!session.isForumCompatible(client))
        {
            if(session.getForum() == ForumType.Seniors)
            {
                actions.add("Failed registration: Client doesn't meet the age requirements for this session (" + session.getForum() + ")");
            }
            else if(session.getForum() == ForumType.Male || session.getForum() == ForumType.Female)
            {
                actions.add("Failed registration: Client's gender doesn't match the session's gender requirements (" + session.getForum() + ")");
            }
            throw new IllegalArgumentException("Client is not compatible with the session forum.");
        }
        if (!session.hasSpace()) {
            actions.add("Failed registration: No available spots for session");
            throw new IllegalArgumentException("No space left in the session.");
        }
        if (client.getBalance() < session.getPrice())
        {
            actions.add("Failed registration: Client doesn't have enough balance");
            throw new InsufficientFundsException("Client does not have enough balance.");
        }
        if (session.addParticipant(client)) {
            // Reduce client balance
            client.setBalance(client.getBalance() - session.getPrice());
            // Update Gym balance
            Gym.getInstance().setGymBalance(Gym.getInstance().getGymBalance() + session.getPrice());
            actions.add("Registered client: " + client.getName() + " to session: " + session.getType() + " on " + session.getDateTime() + " for price: " + session.getPrice() );

        } else {
            actions.add("Failed registration: Session is not in the future");
            throw new DuplicateClientException("Client already registered for session: " + session.getType());
        }
    }

    // Notify clients that related to the specific session
    public void notify(Session session, String message) {
        ensureAccess();
        for (Client client : session.getParticipants()) {
            client.receiveNotification(message);
        }
        actions.add("A message was sent to everyone registered for session " + session.getType() + " on " + session.getDateTime() + " : " + message);
    }

    // Notify all clients that are sighed to session on a specific date
    public void notify(String date, String message) {
        ensureAccess();
        for(Session session : sessions)
        {
            if(extractDate(session.getDateTime()).equals(extractDate(date))) // only by format of dd-MM-yyyy
            {
                for (Client client : session.getParticipants())
                {
                    client.receiveNotification(message);
                }
                actions.add("A message was sent to everyone registered for a session on " + session.getDateTime() + " : " + message);
            }
        }
    }

    // Notify all clients
    public void notify(String message) {
        ensureAccess();
        for(Client client : clients) {
            client.receiveNotification(message);
        }
        actions.add("A message was sent to all gym clients: " + message);
    }

    public void paySalaries() {
        ensureAccess();
        actions.add("Salaries have been paid to all employees");
    }

    public void printActions() {
        ensureAccess();
        for (String action : actions) {
            System.out.println(action);
        }
    }

    private static String extractDate(String dateTimeString) {
        try {
            // Check if the input includes a time component
            if (dateTimeString.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}")) {
                // If it includes time, parse it as LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } else if (dateTimeString.matches("\\d{2}-\\d{2}-\\d{4}")) {
                // If it only includes the date, parse it as LocalDate
                return dateTimeString; // Already in the correct format
            } else {
                throw new IllegalArgumentException("Invalid date format. Expected 'dd-MM-yyyy' or 'dd-MM-yyyy HH:mm'.");
            }
        } catch (Exception e) {
            System.out.println("Error extracting date: " + e.getMessage());
            throw e; // Re-throw exception if needed
        }
    }

}
