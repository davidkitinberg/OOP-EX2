package gym.management.Sessions;

import gym.Exception.InstructorNotQualifiedException;
import gym.customers.Instructor;
import gym.management.Sessions.SessionTypes.MachinePilates;
import gym.management.Sessions.SessionTypes.Ninja;
import gym.management.Sessions.SessionTypes.Pilates;
import gym.management.Sessions.SessionTypes.ThaiBoxing;

public class SessionFactory {
    public static Session createSession(SessionType type, String dateTime, ForumType forum, Instructor instructor) throws InstructorNotQualifiedException {

        // Ensure the instructor is qualified for the session type
        if (!instructor.isQualifiedFor(type)) {
            throw new InstructorNotQualifiedException("Error: Instructor is not qualified to conduct this session type.");
        }

        // Create and return a specific session instance based on type
        switch (type) {
            case Pilates:
                return new Pilates(type,dateTime, forum, instructor);
            case MachinePilates:
                return new MachinePilates(type,dateTime, forum, instructor);
            case ThaiBoxing:
                return new ThaiBoxing(type,dateTime, forum, instructor);
            case Ninja:
                return new Ninja(type,dateTime, forum, instructor);
            default:
                throw new IllegalArgumentException("Unsupported session type: " + type);
        }
        // Create and return a new Session object
        //return new Session(type, dateTime, forum, instructor);
    }

}
