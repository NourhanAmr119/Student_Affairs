<%@page import="net.codejava.json.KnownLanguage"%>
<%@ page import="net.codejava.json.Employee" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>

<html>
<head>
    <title>Employee Management</title>
</head>
<body>
    <h1>Employee List</h1>
    <ul>
        <% List<Employee> employees = (List<Employee>) request.getAttribute("employees");
        if (employees != null) {
            for (Employee employee : employees) { %>
                <li>
                    <%= employee.getFirstName() %> <%= employee.getLastName() %>
                    (Employee ID: <%= employee.getEmployeeID() %>, Designation: <%= employee.getDesignation() %>)
                    <form action="<%= request.getContextPath() %>/index" method="post" style="display: inline;">
                        <input type="hidden" name="deleteEmployeeID" value="<%= employee.getEmployeeID() %>">
                        <input type="submit" value="Delete">
                    </form>
                </li>
            <% }
        } else {
            out.println("No employees available");
        }
        %>
    </ul>
<%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
        <p style="color: red;"><%= errorMessage %></p>
    <%
        }
    %>
    <h2>Add New Employee</h2>
    <form action="<%= request.getContextPath() %>/index" method="post">
        First Name: <input type="text" name="firstName" required><br>
        Last Name: <input type="text" name="lastName" required><br>
        Employee ID: <input type="text" name="employeeID" required><br>
        Designation: <input type="text" name="designation" required><br>

       
        <h3>Known Languages</h3>
        <div id="languages">
            <div class="language">
                Language Name: <input type="text" name="languageName" required><br>
                Score Out of 100: <input type="text" name="scoreOutof100" required><br>
            </div>
        </div>

        <button type="button" onclick="addLanguage()">Add Language</button>

        <script>
            function addLanguage() {
                var languageDiv = document.createElement("div");
                languageDiv.innerHTML = '<div class="language">Language Name: <input type="text" name="languageName" required><br>' +
                    'Score Out of 100: <input type="text" name="scoreOutof100" required><br></div>';
                document.getElementById("languages").appendChild(languageDiv);
            }
        </script>

        <input type="submit" value="Add Employee">
    </form>

   
    <h2>Update Employee</h2>
    <form action="<%= request.getContextPath() %>/index" method="get">
        Employee ID to Update: <input type="text" name="updateEmployeeID" required><br>
        New Designation: <input type="text" name="newDesignation" required><br>
        <input type="submit" value="Update Employee">
    </form>

    
    <form action="<%= request.getContextPath() %>/index" method="get">
        Search by Employee ID: <input type="text" name="searchEmployeeID">
        <input type="submit" value="Search">
    </form>

    <form action="<%= request.getContextPath() %>/index" method="get">
        Search by Designation: <input type="text" name="searchDesignation">
        <input type="submit" value="Search">
    </form>

  
 
    <h2>Java Developers with Score > 50</h2>
    <form action="<%= request.getContextPath() %>/index" method="get">
        <input type="submit" name="showJavaDevelopers" value="Show Java Developers">
    </form>
    <ul>
        <% List<Employee> javaDevelopers = (List<Employee>) request.getAttribute("javaDevelopers");
        if (javaDevelopers != null && !javaDevelopers.isEmpty()) {

            Collections.sort(javaDevelopers, (developer1, developer2) -> {
                int score1 = developer1.getKnownLanguages().stream()
                        .mapToInt(KnownLanguage::getScoreOutof100)
                        .max()
                        .orElse(0);
                int score2 = developer2.getKnownLanguages().stream()
                        .mapToInt(KnownLanguage::getScoreOutof100)
                        .max()
                        .orElse(0);

                // Sort in ascending order
                return Integer.compare(score1, score2);
            });

            for (Employee developer : javaDevelopers) { %>
                <li>
                    <%= developer.getFirstName() %> <%= developer.getLastName() %>
                    (Employee ID: <%= developer.getEmployeeID() %>, Designation: <%= developer.getDesignation() %>)
                </li>
            <% }
        } else {
            out.println("No Java developers with score > 50");
        }
        %>
    </ul>


</body>
</html>
