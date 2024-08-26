/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package net.codejava.json;

// EmployeeServlet.java
import net.codejava.json.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@WebServlet("/index")
public class EmployeeServlet extends HttpServlet {

    private List<Employee> employees = new ArrayList<>();

    private static final String JSON_FILE_PATH = "C:/Users/hp/OneDrive/Documents/NetBeansProjects/JSON/src/main/java/net/codejava/json/Employees.json";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            employees = objectMapper.readValue(new File(JSON_FILE_PATH), new TypeReference<List<Employee>>() {
            });

            String searchEmployeeID = request.getParameter("searchEmployeeID");
            String searchDesignation = request.getParameter("searchDesignation");

            if (searchEmployeeID != null && !searchEmployeeID.isEmpty()) {

                employees = filterEmployeesByEmployeeID(employees, searchEmployeeID);
            } else if (searchDesignation != null && !searchDesignation.isEmpty()) {

                employees = filterEmployeesByDesignation(employees, searchDesignation);
            } else if (request.getParameter("showJavaDevelopers") != null) {

                List<Employee> javaDevelopers = filterJavaDevelopers(employees);
                javaDevelopers.sort(Comparator.comparingInt(e -> e.getKnownLanguages()
                        .stream()
                        .mapToInt(KnownLanguage::getScoreOutof100)
                        .max()
                        .orElse(0)));
                request.setAttribute("javaDevelopers", javaDevelopers);
            }

            System.out.println("Employees read from file: " + employees);

            if ("POST".equalsIgnoreCase(request.getMethod())) {
                addNewEmployee(request);

                objectMapper.writeValue(new File(JSON_FILE_PATH), employees);
            }

            request.setAttribute("employees", employees);

            String deleteEmployeeID = request.getParameter("deleteEmployeeID");
            if (deleteEmployeeID != null && !deleteEmployeeID.isEmpty()) {
                deleteEmployeeByID(employees, deleteEmployeeID);

                objectMapper.writeValue(new File(JSON_FILE_PATH), employees);
            }

            String updateEmployeeID = request.getParameter("updateEmployeeID");
            String newDesignation = request.getParameter("newDesignation");

            if (updateEmployeeID != null && newDesignation != null) {
                updateEmployee(Integer.parseInt(updateEmployeeID), newDesignation);

                objectMapper.writeValue(new File(JSON_FILE_PATH), employees);
            }

            if (employees.isEmpty()) {
                request.getRequestDispatcher("/emptyList.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private int getHighestScore(Employee employee) {
        // Find and return the highest score from known languages
        return employee.getKnownLanguages().stream()
                .mapToInt(KnownLanguage::getScoreOutof100)
                .max()
                .orElse(0);
    }

    private void displayJavaDevelopers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            List<Employee> allEmployees = objectMapper.readValue(new File(JSON_FILE_PATH), new TypeReference<List<Employee>>() {
            });

            List<Employee> javaDevelopers = filterJavaDevelopers(allEmployees);

            request.setAttribute("javaDevelopers", javaDevelopers);

            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private void updateEmployee(int employeeID, String newDesignation) {
        for (Employee employee : employees) {
            if (employee.getEmployeeID() == employeeID) {
                employee.setDesignation(newDesignation);

                break;
            }
        }
    }

    private void deleteEmployeeByID(List<Employee> employees, String employeeID) {
        employees.removeIf(employee -> String.valueOf(employee.getEmployeeID()).equals(employeeID));
    }

    private List<Employee> filterEmployeesByEmployeeID(List<Employee> employees, String searchEmployeeID) {
        List<Employee> filteredEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            if (String.valueOf(employee.getEmployeeID()).equals(searchEmployeeID)) {
                filteredEmployees.add(employee);
            }
        }

        return filteredEmployees;
    }

    private List<Employee> filterJavaDevelopers(List<Employee> employees) {
        List<Employee> javaDevelopers = new ArrayList<>();

        for (Employee employee : employees) {
            if (employee.knowsJava()) {
                javaDevelopers.add(employee);
            }
        }

        return javaDevelopers;
    }

    private List<Employee> filterEmployeesByDesignation(List<Employee> employees, String searchDesignation) {
        List<Employee> filteredEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            if (employee.getDesignation().equalsIgnoreCase(searchDesignation)) {
                filteredEmployees.add(employee);
            }
        }

        return filteredEmployees;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean isEmployeeIDUnique(int employeeID) {
        for (Employee employee : employees) {
            if (employee.getEmployeeID() == employeeID) {
                return false; // Employee ID is not unique
            }
        }
        return true; // Employee ID is unique
    }

   private void addNewEmployee(HttpServletRequest request) {
    try {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String employeeIDStr = request.getParameter("employeeID");
        String designation = request.getParameter("designation");

        if (employeeIDStr == null || employeeIDStr.isEmpty()) {
            System.out.println("Employee ID cannot be null or empty.");
            return;
        }

        int employeeID = Integer.parseInt(employeeIDStr);

        if (isEmployeeIDUnique(employeeID)) {
            Employee newEmployee = new Employee();
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setEmployeeID(employeeID);
            newEmployee.setDesignation(designation);

            String[] languageNames = request.getParameterValues("languageName");
            String[] scoreOutof100Str = request.getParameterValues("scoreOutof100");

            if (languageNames != null && scoreOutof100Str != null) {
                List<KnownLanguage> knownLanguages = new ArrayList<>();
                for (int i = 0; i < languageNames.length && i < scoreOutof100Str.length; i++) {
                    String languageName = languageNames[i];
                    int scoreOutof100 = Integer.parseInt(scoreOutof100Str[i]);

                    KnownLanguage knownLanguage = new KnownLanguage();
                    knownLanguage.setLanguageName(languageName);
                    knownLanguage.setScoreOutof100(scoreOutof100);

                    knownLanguages.add(knownLanguage);
                }
                newEmployee.setKnownLanguages(knownLanguages);
            }

            employees.add(newEmployee);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(JSON_FILE_PATH), employees);
        }else {
            
            request.setAttribute("errorMessage", "Employee ID must be unique. Another employee with the same ID already exists.");
        }

    } catch (NumberFormatException e) {
        System.out.println("Error parsing Employee ID or score. Please provide valid numeric values.");
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Error writing employees to JSON file.");
    }
}


}
