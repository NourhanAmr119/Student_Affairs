package net.codejava.project1;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private double gpa;
    private int level;
    private String address;

    // Add public getters and setters

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public double getGpa() {
        return gpa;
    }

    public int getLevel() {
        return level;
    }

    public String getAddress() {
        return address;
    }
}
