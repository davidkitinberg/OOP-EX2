package gym.management;

import gym.management.Sessions.Observer;

public interface Subject
    {
        void registerObserver(Observer o);
        void removeObserver(Observer o);
        void notifyObservers(String s);
}
