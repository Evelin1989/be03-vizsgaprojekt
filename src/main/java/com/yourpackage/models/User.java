package com.yourpackage.models;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private int status;

    public User(int id, String firstName, String lastName, String dateOfBirth, int status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getStatus() {
        return status;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setStatus(int status) { this.status = status; }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
