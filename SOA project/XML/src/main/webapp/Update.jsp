<%@ page import="net.codejava.project1.Student" %>

<!DOCTYPE html>
<html>
<head>
    <title>Update Student</title>
</head>
<body>
     <%
        
        Student studentToUpdate = (Student) request.getAttribute("studentToUpdate");

        String updatedFirstName = (studentToUpdate != null) ? studentToUpdate.getFirstName() : "";
        String updatedLastName = (studentToUpdate != null) ? studentToUpdate.getLastName() : "";
        String updatedGender = (studentToUpdate != null) ? studentToUpdate.getGender() : "";
        String updatedGpa = (studentToUpdate != null) ? String.valueOf(studentToUpdate.getGpa()) : "";
        String updatedLevel = (studentToUpdate != null) ? String.valueOf(studentToUpdate.getLevel()) : "";
        String updatedAddress = (studentToUpdate != null) ? studentToUpdate.getAddress() : "";

       
    %>
   <h2>Update Student</h2>
    <form action="Students" method="post">
       <input type="hidden" name="action" value="update">
       <input type="hidden" name="idToUpdate" value="<%= (studentToUpdate != null) ? studentToUpdate.getId() : "" %>">

       <label>First Name:</label>
       <input type="text" name="updatedFirstName" value="<%= (studentToUpdate != null) ? studentToUpdate.getFirstName() : "" %>"><br>


       <label>Last Name:</label>
       <input type="text" name="updatedLastName" value="<%= (studentToUpdate != null) ? studentToUpdate.getLastName() : "" %>"><br>


       <label>Gender:</label>
      <input type="text" name="updatedGender" value="<%= (studentToUpdate != null) ? studentToUpdate.getGender() : "" %>"><br>


       <label>GPA:</label>
      <input type="text" name="updatedGpa" value="<%= (studentToUpdate != null) ? studentToUpdate.getGpa() : "" %>"><br>


       <label>Level:</label>
       <input type="text" name="updatedLevel" value="<%= (studentToUpdate != null) ? studentToUpdate.getLevel() : "" %>"><br>


       <label>Address:</label>
      <input type="text" name="updatedAddress" value="<%= (studentToUpdate != null) ? studentToUpdate.getAddress() : "" %>"><br>


       <input type="submit" value="Update Student">
    </form>

    <a href="index.jsp">Back to Home</a>
</body>
</html>
