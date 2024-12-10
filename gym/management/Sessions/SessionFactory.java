package gym.management.Sessions;

import gym.customers.Instructor;

public class SessionFactory {
    public static Session createSession(SessionType type, String dateTime, ForumType forum, Instructor instructor) {
        // Input validation
        if (type == null || dateTime == null || forum == null || instructor == null) {
            throw new IllegalArgumentException("Error: All parameters must be provided to create a Session.");
        }

        // Ensure the instructor is qualified for the session type
        if (!instructor.isQualifiedFor(type)) {
            throw new IllegalArgumentException("Error: The instructor is not qualified for this session type.");
        }

        // Create and return a new Session object
        return new Session(type, dateTime, forum, instructor);
    }
}
