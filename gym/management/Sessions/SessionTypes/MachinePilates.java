package gym.management.Sessions.SessionTypes;

import gym.customers.Instructor;
import gym.management.Sessions.ForumType;
import gym.management.Sessions.Session;
import gym.management.Sessions.SessionType;

public class MachinePilates extends Session {
    public MachinePilates(SessionType type, String dateTime, ForumType forum, Instructor instructor) {

            super(type, dateTime, forum, instructor);

        }
    }

