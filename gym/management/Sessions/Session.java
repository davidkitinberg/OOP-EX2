package gym.management.Sessions;

import gym.customers.Client;
import gym.customers.Instructor;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private SessionType type;
    private String dateTime;
    private ForumType forum;
    private Instructor instructor;
    private List<Client> participants = new ArrayList<>();
    private int maxParticipants = 30;

    public Session(SessionType type, String dateTime, ForumType forum, Instructor instructor) {
        this.type = type;
        this.dateTime = dateTime;
        this.forum = forum;
        this.instructor = instructor;
    }

    public boolean addParticipant(Client client) {
        if (participants.size() < maxParticipants && !participants.contains(client)) {
            participants.add(client);
            return true;
        }
        return false;
    }

    public List<Client> getParticipants() {
        return participants;
    }

    public SessionType getType() {
        return type;
    }
}
