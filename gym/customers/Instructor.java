package gym.customers;

import gym.Exception.InvalidAgeException;
import gym.management.Sessions.SessionType;

import java.util.List;

public class Instructor extends Person {
    private int salary;
    private List<SessionType> qualifications;

    public Instructor(String name, int balance, Gender gender, String dateOfBirth, int salary, List<SessionType> qualifications) throws InvalidAgeException {
        super(name, balance, gender, dateOfBirth);
        this.salary = salary;
        this.qualifications = qualifications;
    }

    public boolean isQualifiedFor(SessionType type) {
        return qualifications.contains(type);
    }

    public int getSalary() {
        return salary;
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
                " | Salary per Hour: " + getSalary() +
                " | Certified Classes: " + String.join(", ",
                getQualifiedClasses().stream()
                        .map(SessionType::toString) // Convert each SessionType to its String representation
                        .toList());
    }

//    private int getBalance() {
//        return this.getBalance();
//    }

}
