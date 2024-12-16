package gym.management.Sessions;

import gym.customers.Client;
import gym.customers.Gender;
import gym.customers.Instructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class Session {
    private final SessionType type;
    private final String dateTime; // Stored as "dd-MM-yyyy HH:mm"
    private final ForumType forum;
    private final Instructor instructor;
    private final List<Client> participants = new ArrayList<>();

    public Session(SessionType type, String dateTime, ForumType forum, Instructor instructor) {
        this.type = type;
        validateFormat(dateTime);
        this.dateTime = dateTime;
        this.forum = forum;
        this.instructor = instructor;
    }

    // This method converts the date format dd-MM-yyyy HH:mm to yyyy-MM-dd'T'HH:mm format
    public static String convertDateTimeFormat(String inputDateTime) {
        // Define the input format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Define the desired output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Parse the input string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, inputFormatter);

        // Format the LocalDateTime object into the desired string format
        return dateTime.format(outputFormatter);
    }


    // Validation of dd-MM-yyyy HH:mm format
    private void validateFormat(String dateOfBirth) {
        if (!dateOfBirth.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}")) {
            throw new IllegalArgumentException("Invalid date format. Use 'dd-MM-yyyy HH:mm'.");
        }
    }


    // This function checks whether the session is expired or not
    public boolean isExpired() {
        // Parse session date-time
        LocalDateTime sessionDateTime = LocalDateTime.parse(convertDateTimeFormat(dateTime), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        // Get current time formatted to the same pattern
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Compare session date-time with the current date-time
        return !sessionDateTime.isAfter(currentDateTime); // Returns true if expired
    }

    public boolean isForumCompatible(Client client) {
        switch (forum) {
            case Male:
                return client.getGender() == Gender.Male;
            case Female:
                return client.getGender() == Gender.Female;
            case Seniors:
                return client.getAge() >= 65;
            case All:
                return true;
            default:
                return false;
        }
    }

    public boolean hasSpace() {
        return participants.size() < type.getMaxParticipants();
    }

    public boolean addParticipant(Client client) {
        if (!hasSpace()) {
            return false; // No space left
        }
        if (participants.contains(client)) {
            return false; // Client already registered
        }
        participants.add(client);
        return true;
    }


    public String getType() {
        return type.getName();
    }


    public ForumType getForum() {
        return forum;
    }
    public int getPrice() {
        return type.getPrice();
    }

    public List<Client> getParticipants() {
        return participants;
    }

    // Returns yyyy-MM-dd'T'HH:mm format
    public String getDateTime() {
        return convertDateTimeFormat(dateTime);
    }
    // Returns yyyy-MM-dd format
    public String getDate() {
        return dateTime;
    }

    // This function converts yyyy-MM-dd'T'HH:mm format to yyyy-MM-dd format
    private static String convertDateFormat(String input) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
        LocalDate date = dateTime.toLocalDate();
        return date.toString();
    }


    @Override
    public String toString() {
        return "Session Type: " + type.getName() +
                " | Date: " + dateTime +
                " | Forum: " + forum +
                " | Instructor: " + instructor.getName() +
                " | Participants: " + participants.size() + "/" + type.getMaxParticipants();
    }

}
