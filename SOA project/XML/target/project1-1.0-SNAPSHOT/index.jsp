<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="net.codejava.project1.Student" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Student Management</title>
    </head>
    <body>
        <h1>Student Management</h1>

        <%
            int numberOfStudents = 0;
            List<Student> searchResults = null;
            List<Student> sortedResults = null;
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (request.getAttribute("numberOfStudents") != null) {
                numberOfStudents = Integer.parseInt(request.getAttribute("numberOfStudents").toString());
            }
            if (request.getAttribute("searchResults") != null) {
                searchResults = (List<Student>) request.getAttribute("searchResults");
            }
            if (request.getAttribute("sortedResults") != null) {
                sortedResults = (List<Student>) request.getAttribute("sortedResults");
            }
        %>

        <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div style="color: red; font-weight: bold;">
            <%= errorMessage %>
        </div>
        <% } %>

        <h2>Add Students</h2>
        <form action="Students" method="post">
            <input type="text" id="num" name="numberOfStudents"><br>
            <% for (int i = 0; i < 1; i++) { %>
            <label>Student <%= i + 1 %> ID:</label>
            <input type="text" name="id<%= i %>"><br>
            <label>Student <%= i + 1 %> First Name:</label>
            <input type="text" name="firstName<%= i %>"><br>
            <label>Student <%= i + 1 %> Last Name:</label>
            <input type="text" name="lastName<%= i %>"><br>
            <label>Student <%= i + 1 %> Gender:</label>
            <input type="text" name="gender<%= i %>"><br>
            <label>Student <%= i + 1 %> GPA:</label>
            <input type="text" name="gpa<%= i %>"><br>
            <label>Student <%= i + 1 %> Level:</label>
            <input type="text" name="level<%= i %>"><br>
            <label>Student <%= i + 1 %> Address:</label>
            <input type="text" name="address<%= i %>"><br><br>
            <% } %>
            <input type="submit" value="Add Students">
        </form>



        <h2>Search Students</h2>
        <form action="Students" method="get">
            <label>Search by:</label>
            <select name="searchType">
                <option value="ID">ID</option>
                <option value="FirstName">First Name</option>
                <option value="LastName">Last Name</option>
                <option value="Gender">Gender</option>
                <option value="GPA">GPA</option>
                <option value="Level">Level</option>
                <option value="Address">Address</option>
            </select>
            <input type="text" name="searchValue">
            <input type="submit" value="Search">
        </form>

        <% if (searchResults != null && !searchResults.isEmpty()) { %>
        <h2>Search Results</h2>
        <p>Number of found students: <%= searchResults.size() %></p>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Gender</th>
                <th>GPA</th>
                <th>Level</th>
                <th>Address</th>
                <th>Action</th>
            </tr>
            <% for (Student student : searchResults) { %>
            <tr>
                <!-- Display student information -->
                <td><%= student.getId() %></td>
                <td><%= student.getFirstName() %></td>
                <td><%= student.getLastName() %></td>
                <td><%= student.getGender() %></td>
                <td><%= student.getGpa() %></td>
                <td><%= student.getLevel() %></td>
                <td><%= student.getAddress() %></td>
                <td>
                <td>
                    <a href="Students?action=updateResult&idToUpdate=<%= student.getId() %>">Update</a>
                </td>

                </td>
                <td><a href="Students?action=delete&id=<%= student.getId() %>">Delete</a></td>
            </tr>
            <% } %>
        </table>
        <% } %>

        <h2>Sort Students</h2>
        <form action="Students" method="get">
            <input type="hidden" name="action" value="sort">
            <label>Sort by:</label>
            <select name="sortAttribute">
                <option value="ID">ID</option>
                <option value="FirstName">First Name</option>
                <option value="LastName">Last Name</option>
                <option value="Gender">Gender</option>
                <option value="GPA">GPA</option>
                <option value="Level">Level</option>
                <option value="Address">Address</option>
            </select>
            <label>Sort order:</label>
            <select name="sortOrder">
                <option value="asc">Ascending</option>
                <option value="desc">Descending</option>
            </select>
            <input type="submit" value="Sort">
        </form>

        <% if (sortedResults != null && !sortedResults.isEmpty()) { %>
        <h2>Sorted Results</h2>
        <p>Number of found students: <%= sortedResults.size() %></p>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Gender</th>
                <th>GPA</th>
                <th>Level</th>
                <th>Address</th>
                <th>Action</th>
            </tr>
            <% for (Student student : sortedResults) { %>
            <tr>
                <!-- Display student information -->
                <td><%= student.getId() %></td>
                <td><%= student.getFirstName() %></td>
                <td><%= student.getLastName() %></td>
                <td><%= student.getGender() %></td>
                <td><%= student.getGpa() %></td>
                <td><%= student.getLevel() %></td>
                <td><%= student.getAddress() %></td>
                <td>
                     <a href="Students?action=updateResult&idToUpdate=<%= student.getId() %>">Update</a>

                </td>
                <td><a href="Students?action=delete&id=<%= student.getId() %>">Delete</a></td>
            </tr>
            <% } %>
        </table>
        <% } %>

    </body>
</html>
