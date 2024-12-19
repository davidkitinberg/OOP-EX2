package gym.management;

import gym.customers.*;
import gym.Exception.*;
import gym.management.Sessions.*;
import gym.management.Sessions.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Secretary extends Person {
    private int salary;
    private List<Client> clients = new ArrayList<>();
    //private List<Instructor> instructors = new ArrayList<>();
    //private List<Session> sessions = new ArrayList<>();
    //private List<String> actions = new ArrayList<>();
    private static Secretary instance;
    private static List<Client> formerSecretaries = new ArrayList<>();
    private static Gym gym = Gym.getInstance();



    // Private constructor to prevent instantiation
    private Secretary(String name, int balance, Gender gender, String dateOfBirth, int salary) throws InvalidAgeException {
        super(name, balance, gender, dateOfBirth);
        this.salary = salary;
        //actions.add("A new secretary has started working at the gym: " + name);
        gym.addAction("A new secretary has started working at the gym: " + name);

    }
    public static Secretary getInstance() {
        return instance; // Return the singleton instance
    }



    public static void replaceInstance(String name, int balance, Gender gender, String dateOfBirth, int salary) throws InvalidAgeException {
        if (instance != null) {
            Client formerSecreraty = new Client(instance.getName(), instance.getBalance(), instance.getGender(), instance.getDateOfBirth());
            formerSecretaries.add(formerSecreraty);
        }
        instance = new Secretary(name, balance, gender, dateOfBirth, salary);
    }

    // Ensure this secretary has access rights
    private void ensureAccess() {
        if (this != instance) {
            throw new NullPointerException("Error: Former secretaries are not permitted to perform actions");
        }
//        if (isContained(formerSecretaries,instance)) {
//            throw new NullPointerException("Error: Former secretaries are not permitted to perform actions");
//        }

    }

    public Client registerClient(Person person) throws InvalidAgeException, DuplicateClientException {
        ensureAccess();
        try {
            if (person.getAge() < 18) {
                throw new InvalidAgeException("Error: Client must be at least 18 years old to register");
            }

            // check if client is already registered
            if(isContained(gym.getClients(), person))
            {
                throw new DuplicateClientException("Error: The client is already registered");
            }


            Client client = new Client(person.getName(), person.getBalance(), person.getGender(), person.getDateOfBirth());
            //clients.add(client);
            gym.addClient(client);
            gym.addAction("Registered new client: " + client.getName());
            //actions.add("Registered new client: " + client.getName());
            return client;
        } catch (InvalidAgeException | DuplicateClientException e) {
            //actions.add("Failed to register client: " + person.getName() + " - " + e.getMessage());
            System.out.println(e.getMessage());
            return null; // Return null if registration fails
        }
    }


    public void unregisterClient(Client client) throws ClientNotRegisteredException {
        ensureAccess();
        if (!gym.getClients().contains(client)) {
            throw new ClientNotRegisteredException("Error: Registration is required before attempting to unregister");
        }
        gym.removeClient(client);
        //clients.remove(client);
        //actions.add("Unregistered client: " + client.getName());
        gym.addAction("Unregistered client: " + client.getName());
    }


    public Instructor hireInstructor(Person person, int salary, List<SessionType> qualifications) throws InvalidAgeException {
        ensureAccess();
        for (Client client : gym.getClients()) {
            if (comparePersons(client, person)) {
                Instructor instructor = new Instructor(client.getName(), client.getBalance(), client.getGender(), client.getDateOfBirth(), salary, qualifications);
                //Instructor instructor = new Instructor(person.getName(), person.getBalance(), person.getGender(), person.getDateOfBirth(), salary, qualifications);
                gym.addInstructor(instructor);
                gym.addAction("Hired new instructor: " + instructor.getName() + " with salary per hour: " + salary);
                return instructor;
            }
        }
        // Create new instructor if not already a client
        Instructor instructor = new Instructor(person.getName(), person.getBalance(), person.getGender(), person.getDateOfBirth(), salary, qualifications);
        gym.addInstructor(instructor);
        gym.addAction("Hired new instructor: " + instructor.getName());
        return instructor;
        //instructors.add(instructor);
        //gym.addInstructor(instructor);
        //actions.add("Hired new instructor: " + instructor.getName());
        //gym.addAction("Hired new instructor: " + instructor.getName() + " with salary per hour: " + salary);
        //return instructor;
    }

    public Session addSession(SessionType type, String dateTime, ForumType forum, Instructor instructor) throws InstructorNotQualifiedException {

    try
    {
        // Use the factory to create the session
        Session session = SessionFactory.createSession(type, dateTime, forum, instructor);

        // Add the session to the list and record the action
        //sessions.add(session);
        gym.addSession(session);
        //actions.add("Created new session: " + type.getName() + " on " + dateTime + " with instructor: " + instructor.getName());
        gym.addAction("Created new session: " + type.getName() + " on " + session.getDateTime() + " with instructor: " + instructor.getName());

        return session; // Return the created session
    } catch (IllegalArgumentException e)
    {
        // Handle invalid session creation
        return null; // Return null if session creation fails
    }
    }



    public void registerClientToLesson(Client client, Session session) throws ClientNotRegisteredException,DuplicateClientException  {
        ensureAccess();
        boolean flag=false;
        try {
            // check if client is within the clients list
            if(!isContained(gym.getClients(), client))
            {
                flag=true;
                throw new ClientNotRegisteredException("Error: The client is not registered with the gym and cannot enroll in lessons");
            }

            // check if client is already registered to this lesson
            if(isContained(session.getParticipants(), client))
            {
                flag=true;
                throw new DuplicateClientException("Error: The client is already registered for this lesson");
            }

            // check if lesson is expired
            if (session.isExpired()) {
                flag=true;
                gym.addAction("Failed registration: " +
                        "Session is not in the future");
                //actions.add("Failed registration: Session is not in the future");
            }

            // check if client is met with lesson's forum type

            if (!session.isForumCompatible(client))
            {
                flag=true;

                if (session.getForum() == ForumType.Seniors)
                {
                    gym.addAction("Failed registration: Client doesn't meet the age requirements for this session (" + session.getForum() + ")");
                    //actions.add("Failed registration: Client doesn't meet the age requirements for this session (" + session.getForum() + ")");
                }
                else if (session.getForum() == ForumType.Male || session.getForum() == ForumType.Female)
                {
                    gym.addAction("Failed registration: Client's gender doesn't match the session's gender requirements");
                    //actions.add("Failed registration: Client's gender doesn't match the session's gender requirements (" + session.getForum() + ")");
                }

                //return; // Exit the method since the client is not compatible with the session
            }

            // check if this session has enough space for another client
            if (!session.hasSpace()) {
                gym.addAction("Failed registration: No available spots for session");
                //actions.add("Failed registration: No available spots for session");
                flag=true;

            }
            // check if client has sufficient balance for this lesson
            if (client.getBalance() < session.getPrice()) {
                gym.addAction("Failed registration: Client doesn't have enough balance");
                //actions.add("Failed registration: Client doesn't have enough balance");
                flag=true;

            }
            if (flag==false)
            {
                session.addParticipant(client);
                synchronized (client) {
                    client.reduceBalance(session.getPrice()); // Deduct price from client balance
                    Instructor thatNigga = gym.getEquivalentInstructor(client);
                    Client secNigga = gym.getEquivalentClientForSecretary(instance);
                    if(thatNigga != null)
                    {
                        thatNigga.reduceBalance(session.getPrice());
                    }
                    if(secNigga != null)
                    {
                        secNigga.reduceBalance(session.getPrice());
                    }


                }
                synchronized (gym) {
                    session.addParticipant(client); // Add client to session
                    gym.setGymBalance(gym.getGymBalance() + session.getPrice()); // Add price to gym balance
                }

                //client.setBalance(client.getBalance() - session.getPrice());
                //Gym.getInstance().setGymBalance(Gym.getInstance().getGymBalance() + session.getPrice());
                gym.addAction("Registered client: " + client.getName() + " to session: " + session.getType() + " on " + session.getDateTime() + " for price: " + session.getPrice());
            }//actions.add("Registered client: " + client.getName() + " to session: " + session.getType() + " on " + session.getDateTime());
        } catch (ClientNotRegisteredException | DuplicateClientException | IllegalArgumentException | InsufficientFundsException e) {
            //gym.addAction("Failed to register client: " + client.getName() + " to session: " + session.getType() + " - " + e.getMessage());
            //actions.add("Failed to register client: " + client.getName() + " to session: " + session.getType() + " - " + e.getMessage());
            throw e; // Re-throw the exception to propagate it to the caller
        }
    }


    // Notify clients that related to the specific session
    public void notify(Session session, String message) {
        ensureAccess();
        for (Observer client : session.getParticipants()) {
            client.update(message);
        }
        gym.addAction("A message was sent to everyone registered for session " + session.getType() + " on " + session.getDateTime() + " : " + message);
        //actions.add("A message was sent to everyone registered for session " + session.getType() + " on " + session.getDateTime() + " : " + message);
    }

    // Notify all clients that are sighed to session on a specific date
    public void notify(String date, String message) {
        ensureAccess();
        for(Session session : gym.getSessions())
        {
            if(extractDate(session.getDateTime()).equals(extractDate(date))) // only by format of dd-MM-yyyy
            {
                for (Observer client : session.getParticipants())
                {
                    client.update(message);
                }
                gym.addAction("A message was sent to everyone registered for a session on " + extractDate(session.getDate()) + " : " + message);
                //actions.add("A message was sent to everyone registered for a session on " + session.getDateTime() + " : " + message);
            }
        }
    }

    // Notify all clients
    public void notify(String message) {
        ensureAccess();
        for(Observer client : gym.getClients()) {
            client.update(message);
        }
        gym.addAction("A message was sent to all gym clients: " + message);
        //actions.add("A message was sent to all gym clients: " + message);
    }

    public void paySalaries1() {
        ensureAccess();
        for(Instructor instructor : gym.getInstructors())
        {
            for(Session session : gym.getSessions())
            {
                if(instructor.equals(session.getInstructor()))
                {
                    instructor.setBalance(instructor.getBalance() + instructor.getSalary());
                    gym.setGymBalance(Gym.getInstance().getGymBalance() - instructor.getSalary());
                }
            }
        }
        instance.setBalance(instance.getBalance() + instance.getSalary());
        gym.setGymBalance(Gym.getInstance().getGymBalance() - instance.getSalary());
        //actions.add("Salaries have been paid to all employees");
        gym.addAction("Salaries have been paid to all employees");
    }
    public void paySalaries2() {
        ensureAccess();

        // Pay instructors based on their hourly wage and sessions
        for (Instructor instructor : gym.getInstructors()) {
            int totalHours = 0;

            for (Session session : gym.getSessions()) {
                if (comparePersons(instructor,session.getInstructor())) {
                    totalHours += 1; // Assuming each session is 1 hour; modify if necessary
                }
            }

            int payment = instructor.getSalary() * totalHours; // Calculate total payment
            instructor.setBalance(instructor.getBalance() + payment); // Pay the instructor
            gym.setGymBalance(gym.getGymBalance() - payment); // Deduct from gym balance
        }

        // Pay the secretary
        instance.setBalance(instance.getBalance() + instance.getSalary());
        gym.setGymBalance(gym.getGymBalance() - instance.getSalary());

        gym.addAction("Salaries have been paid to all employees");
    }
    public void paySalaries() {
        ensureAccess();

        synchronized (gym) {
            // Pay instructors based on sessions conducted
            for (Instructor instructor : gym.getInstructors()) {
                int totalHours = 0;

                for (Session session : gym.getSessions()) {
                    if (comparePersons(instructor, session.getInstructor())) {
                        totalHours++;
                    }
                }

                int payment = instructor.getSalary() * totalHours;

                synchronized (instructor) {
                    instructor.setBalance(instructor.getBalance() + payment); // Add payment to instructor balance
                    Client thisNigga = gym.getEquivalentClient(instructor);
                    thisNigga.setBalance(thisNigga.getBalance() + payment);
                }

                gym.setGymBalance(gym.getGymBalance() - payment); // Deduct payment from gym balance
            }

            // Pay the secretary
            synchronized (this) {
                setBalance(getBalance() + salary); // Add fixed salary to secretary balance
                gym.setGymBalance(gym.getGymBalance() - salary); // Deduct salary from gym balance
            }

            gym.addAction("Salaries have been paid to all employees");
        }
    }



    public int getSalary()
    {
        return salary;
    }

    public void printActions() {
        ensureAccess();
        for (String action : gym.getActions()) {
            System.out.println(action);
        }
    }

    // This function converts yyyy-MM-dd'T'HH:mm or yyyy-MM-dd HH:mm format to yyyy-MM-dd format
    private static String extractDate(String dateTimeString) {
        try {
            // Check if the input includes a time component
            if (dateTimeString.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}"))
            {
                // If it includes time, parse it as LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
                return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
            else if (dateTimeString.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}"))
            {
                // Parse the date-time string using dd-MM-yyyy HH:mm
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                // Format it to yyyy-MM-dd
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            else if (dateTimeString.matches("\\d{2}-\\d{2}-\\d{4}"))
            {
                // If it only includes the date, parse it as LocalDate
                return dateTimeString; // Already in the correct format
            } else
            {
                throw new IllegalArgumentException("Invalid date format. Expected 'dd-MM-yyyy' or 'dd-MM-yyyy HH:mm'.");
            }
        } catch (Exception e)
        {
            System.out.println("Error extracting date: " + e.getMessage());
            throw e; // Re-throw exception if needed
        }
    }
    @Override
    public String toString() {
        return "ID: " + getID() +
                " | Name: " + getName() +
                " | Gender: " + getGender() +
                " | Birthday: " + getDateOfBirth() +
                " | Age: " + getAge() +
                " | Balance: " + getBalance() +
                " | Role: Secretary" +
                " | Salary per Month: " + getSalary();
    }

}
