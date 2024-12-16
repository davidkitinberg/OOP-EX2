package gym.customers;

import gym.Exception.InvalidAgeException;
import gym.management.Sessions.SessionType;

import java.util.List;

public class Instructor extends Person {
    private int hourlyWage;
    private List<SessionType> qualifications;

    public Instructor(String name, int balance, Gender gender, String dateOfBirth, int hourlyWage, List<SessionType> qualifications) throws InvalidAgeException {
        super(name, balance, gender, dateOfBirth);
        this.hourlyWage = hourlyWage;
        this.qualifications = qualifications;
    }

    public boolean isQualifiedFor(SessionType type) {
        return qualifications.contains(type);
    }

    public int getHourlyWage() {
        return hourlyWage;
    }
    public List<SessionType> getQualifiedClasses() {
        return qualifications;
    }


    @Override
    public String toString() {
        return "ID: " + getID() +
                " | Name: " + getName() +
                " | Gender: " + getGender() +
                " | Birthday: " + getDateOfBirth() +
                " | Age: " + getAge() +
                " | Balance: " + getBalance() +
                " | Role: Instructor" +
                " | Salary per Hour: " + getHourlyWage() +
                " | Certified Classes: " + getQualifiedClasses();
    }

//    private int getBalance() {
//        return this.getBalance();
//    }

}
