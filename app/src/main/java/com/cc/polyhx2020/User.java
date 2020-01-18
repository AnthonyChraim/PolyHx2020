package com.cc.polyhx2020;

public class User {

    private static int userId;
    private String gpsLocation;
    private String firstName, lastName;
    private String email, password;
    private String dateOfBirth, phoneNumber;

    // CONSTRUCTOR
    public User(String firstName, String lastName, String dateOfBirth, String phoneNumber,
                String email, String password) {
        userId = generateUserId();
        gpsLocation = getCoordinates();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // Generates a unique user ID
    private int generateUserId() {
        return User.userId++;
    }

    // Gets the user's GPS location (coordinates)
    private String getCoordinates() {
        return "";
    }

    // GETTERS & SETTERS
    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        User.userId = userId;
    }

    public String getGpsLocation() {
        return this.gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
