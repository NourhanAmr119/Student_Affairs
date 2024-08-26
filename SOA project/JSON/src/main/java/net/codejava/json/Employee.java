/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.codejava.json;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
public class Employee {
    


    private String firstName;
    private String lastName;
    private int employeeID;
    private String designation;
    private List<KnownLanguage> knownLanguages;

    // Getter methods
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getDesignation() {
        return designation;
    }

 
    // Setter methods
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setKnownLanguages(List<KnownLanguage> knownLanguages) {
        this.knownLanguages = knownLanguages;
    }



    public List<KnownLanguage> getKnownLanguages() {
        if (knownLanguages == null) {
            knownLanguages = new ArrayList<>();
        }
        return knownLanguages;
    }

    
    public boolean knowsJava() {
        List<KnownLanguage> languages = getKnownLanguages();

        for (KnownLanguage language : languages) {
            if ("Java".equalsIgnoreCase(language.getLanguageName()) && language.getScoreOutof100() > 50) {
                return true;
            }
        }
        return false;
    }
}
