package gym.customers;

import gym.Exception.InvalidAgeException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Person {
    private String name;
    private int age; // Optional, could be calculated from dateOfBirth
    private Gender gender;
    private LocalDate dateOfBirth;

    public Person(String name, int age, Gender gender, String dateOfBirth) throws InvalidAgeException {
        validateAge(age);
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dateOfBirth = parseDateOfBirth(dateOfBirth);
    }

    private void validateAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Person must be at least 18 years old.");
        }
    }

    private LocalDate parseDateOfBirth(String dateOfBirth) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(dateOfBirth, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use 'dd-MM-yyyy'.");
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age; // Optionally calculate from dateOfBirth
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int calculateAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
