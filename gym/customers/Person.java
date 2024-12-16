package gym.customers;

import gym.Exception.InvalidAgeException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {
    private String name;
    private int balance;
    private Gender gender;
    private String dateOfBirth; // Now stored as a string in the specified format
    private int ID;
    private static int nextID = 1110;
    private static Map<String, Integer> idMap = new HashMap<>(); // Map to store name + DOB -> ID



    public Person(String name, int balance, Gender gender, String dateOfBirth) throws InvalidAgeException {
        //validateAge(balance);
        validateFormat(dateOfBirth); // Validate the format of the dateOfBirth string
        this.name = name;
        this.balance = balance;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        // Assign or reuse ID
        String uniqueKey = name + "_" + dateOfBirth;
        if (idMap.containsKey(uniqueKey)) {
            this.ID = idMap.get(uniqueKey); // Reuse existing ID
        } else {
            this.ID = ++nextID;            // Assign new ID
            idMap.put(uniqueKey, this.ID); // Save ID to the map
        }
    }

    public int getID() {
        return ID;
    }

    // Validation for age
    private void validateAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Person must be at least 18 years old.");
        }
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int newBalance) {
        balance = newBalance;
    }

    public void reduceBalance(int amount) {
        balance -= amount;
    }

    // Validation for date format
    private void validateFormat(String dateOfBirth) {
        if (!dateOfBirth.matches("\\d{2}-\\d{2}-\\d{4}")) {
            throw new IllegalArgumentException("Invalid date format. Use 'dd-MM-yyyy'.");
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        LocalDate sessionDate = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return currentDate.getYear() - sessionDate.getYear() -
                (currentDate.getDayOfYear() < sessionDate.getDayOfYear() ? 1 : 0);
    }

    public Gender getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", gender=" + gender +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
    public static boolean comparePersons(Person c1, Person c2) {
        // Compare all fields for equality
        return c1.getName().equals(c2.getName()) &&
                c1.getAge() == c2.getAge() &&
                c1.getGender() == c2.getGender() &&
                c1.getDateOfBirth().equals(c2.getDateOfBirth());
    }
    public static boolean isContained(List<Client> personList, Person personToCheck) {
        // Iterate over the list and compare each Person
        for (Person p : personList) {
            if (p.getName().equals(personToCheck.getName()) &&
                    p.getAge() == personToCheck.getAge() &&
                    p.getGender() == personToCheck.getGender() &&
                    p.getDateOfBirth().equals(personToCheck.getDateOfBirth())) {
                return true; // Person is found in the list
            }
        }
        return false; // Person is not in the list
    }

}
