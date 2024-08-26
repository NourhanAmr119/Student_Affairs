package net.codejava.project1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/Students")
public class StudentServlet extends HttpServlet {
    private static final String XML_FILE_PATH = "C://Users/hp/OneDrive/Documents/NetBeansProjects/project1/src/main/java/net/codejava/project1/Students.xml";
    private DocumentBuilderFactory documentBuilderFactory;

    @Override
    public void init() throws ServletException {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        createXmlFileIfNotExists();
    }

    private void createXmlFileIfNotExists() {
    try {
        File xmlFile = new File(XML_FILE_PATH);
        if (!xmlFile.exists()) {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("University");
            document.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String numberOfStudentsParam = request.getParameter("numberOfStudents");
    int numberOfStudents = 0;

    if (numberOfStudentsParam != null && !numberOfStudentsParam.trim().isEmpty()) {
        try {
            numberOfStudents = Integer.parseInt(numberOfStudentsParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    String action = request.getParameter("action");

 if ("update".equals(action)) {
        updateStudent(request, response);
    }


    for (int i = 0; i < numberOfStudents; i++) {
     
        String id = request.getParameter("id" + i);
        String firstName = request.getParameter("firstName" + i);
        String lastName = request.getParameter("lastName" + i);
        String gender = request.getParameter("gender" + i);
        String gpaParameter = request.getParameter("gpa" + i);
        String levelParameter = request.getParameter("level" + i);
        String address = request.getParameter("address" + i);

      
       
        if (validateStudentData(id, firstName, lastName, gender, gpaParameter, levelParameter, address)) {
            Student student = new Student();
            student.setId(id);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setGender(gender);

            double gpa = Double.parseDouble(gpaParameter);
            int level = Integer.parseInt(levelParameter);

            
            if (gpa >= 0 && gpa <= 4) {
                student.setGpa(gpa);
            } else {
             String errorMessage = "Invalid Gpa provided. Please check your Gpa to be from 0 to 4.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
                
            }

            student.setLevel(level);
            student.setAddress(address);

            
            if (!isStudentIdDuplicate(id)) {
                
                storeStudent(student);
            } else {
              String errorMessage = " The ID is Duplicate , please check ID.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
            }
        } else {
            
             String errorMessage = "Invalid data provided. Please check your input.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
           
        }
    }

    request.setAttribute("numberOfStudents", numberOfStudents);
    response.sendRedirect("index.jsp");
}

private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    String idToUpdate = request.getParameter("idToUpdate");
    String updatedFirstName = request.getParameter("updatedFirstName");
    String updatedLastName = request.getParameter("updatedLastName");
    String updatedGender = request.getParameter("updatedGender");
    String updatedGpaParam = request.getParameter("updatedGpa");
    String updatedLevelParam = request.getParameter("updatedLevel");
    String updatedAddress = request.getParameter("updatedAddress");

   
    Student currentStudent = findStudentById(idToUpdate);

    // Check if the student with the given ID exists
    if (currentStudent != null) {
        // Update the student attributes with the provided values
        currentStudent.setFirstName(updatedFirstName);
        currentStudent.setLastName(updatedLastName);
        currentStudent.setGender(updatedGender);

        try {
           
            double updatedGpa = Double.parseDouble(updatedGpaParam);
            int updatedLevel = Integer.parseInt(updatedLevelParam);

            
            if (updatedGpa >= 0 && updatedGpa <= 4) {
                currentStudent.setGpa(updatedGpa);
            } else {
                String errorMessage = "Invalid GPA provided. Please check your GPA to be from 0 to 4.";
                request.setAttribute("errorMessage", errorMessage);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            currentStudent.setLevel(updatedLevel);
            currentStudent.setAddress(updatedAddress);

            
            updateStudentInXml(currentStudent);
RequestDispatcher dispatcher = request.getRequestDispatcher("Update.jsp");
    dispatcher.forward(request, response);
           
        } catch (NumberFormatException e) {
            
            String errorMessage = "Invalid GPA or Level provided. Please check your input.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    } else {
        
        String errorMessage = "Student not found for the provided ID: " + idToUpdate;
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}
private void updateStudentInXml(Student updatedStudent) {
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();
        NodeList studentElements = universityElement.getElementsByTagName("Student");

        // Find the existing student with the provided ID and update its attributes
        for (int i = 0; i < studentElements.getLength(); i++) {
            Element studentElement = (Element) studentElements.item(i);
            String id = studentElement.getAttribute("ID");

            if (id.equals(updatedStudent.getId())) {
                studentElement.getElementsByTagName("FirstName").item(0).setTextContent(updatedStudent.getFirstName());
                studentElement.getElementsByTagName("LastName").item(0).setTextContent(updatedStudent.getLastName());
                studentElement.getElementsByTagName("Gender").item(0).setTextContent(updatedStudent.getGender());
                studentElement.getElementsByTagName("GPA").item(0).setTextContent(String.valueOf(updatedStudent.getGpa()));
                studentElement.getElementsByTagName("Level").item(0).setTextContent(String.valueOf(updatedStudent.getLevel()));
                studentElement.getElementsByTagName("Address").item(0).setTextContent(updatedStudent.getAddress());
                break;
            }
        }

       
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(XML_FILE_PATH));
        transformer.transform(source, result);
    } catch (Exception e) {
        e.printStackTrace();
    }
}




private boolean validateStudentData(String id, String firstName, String lastName, String gender,
                                     String gpaParameter, String levelParameter, String address) {
    return !id.trim().isEmpty() && !firstName.trim().isEmpty() && !lastName.trim().isEmpty()
            && !gender.trim().isEmpty() && !gpaParameter.trim().isEmpty() && !levelParameter.trim().isEmpty()
            && !address.trim().isEmpty() && isAlphaCharacters(firstName) && isAlphaCharacters(lastName);
}


private boolean isStudentIdDuplicate(String id) {
   

    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();
        NodeList studentElements = universityElement.getElementsByTagName("Student");

        for (int i = 0; i < studentElements.getLength(); i++) {
            Element studentElement = (Element) studentElements.item(i);
            String existingId = studentElement.getAttribute("ID");

            
            if (existingId.equals(id)) {
                return true; 
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false; 
}




private boolean isAlphaCharacters(String str) {
    return str.matches("[a-zA-Z]+");
}


   
   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchType = request.getParameter("searchType");
        String searchValue = request.getParameter("searchValue");
        String sortAttribute = request.getParameter("sortAttribute");
        String sortOrder = request.getParameter("sortOrder");

        if (sortAttribute != null && sortOrder != null && !sortAttribute.isEmpty() && !sortOrder.isEmpty()) {
            sortStudents(request,sortAttribute, sortOrder);
        } else if (searchType != null && searchValue != null && !searchType.isEmpty() && !searchValue.isEmpty()) {
            List<Student> searchResults = searchStudentsByAttribute(searchType, searchValue);
            request.setAttribute("searchResults", searchResults);
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        } else if ("delete".equals(action)) {
            String studentId = request.getParameter("id");
            deleteStudent(studentId);
        } else if ("updateResult".equals(action)) {
        String studentIdToUpdate = request.getParameter("idToUpdate");
        Student studentToUpdate = findStudentById(studentIdToUpdate);
        request.setAttribute("studentToUpdate", studentToUpdate);
        request.getRequestDispatcher("Update.jsp").forward(request, response);
        return;
    }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    
private List<Student> searchStudentsByAttribute(String searchType, String searchValue) {
    List<Student> searchResults = new ArrayList<>();
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        NodeList studentElements = document.getElementsByTagName("Student");
        for (int i = 0; i < studentElements.getLength(); i++) {
            Node studentNode = studentElements.item(i);
            if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) studentNode;
                String value;

                switch (searchType.toLowerCase()) {
                    case "id":
                        value = studentElement.getAttribute("ID");
                        break;
                    case "firstname":
                        value = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                        break;
                    case "lastname":
                        value = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                        break;
                    case "gender":
                        value = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                        break;
                    case "gpa":
                        value = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                        break;
                    case "level":
                        value = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                        break;
                    case "address":
                        value = studentElement.getElementsByTagName("Address").item(0).getTextContent();
                        break;
                    default:
                        continue; // Skip unknown search types
                }

                if (value.equalsIgnoreCase(searchValue)) {
                    searchResults.add(parseStudent(studentElement));
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return searchResults;
}

private Student findStudentById(String studentId) {
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();
        NodeList studentElements = universityElement.getElementsByTagName("Student");

        for (int i = 0; i < studentElements.getLength(); i++) {
            Element studentElement = (Element) studentElements.item(i);
            String id = studentElement.getAttribute("ID");

            
            if (id.equalsIgnoreCase(studentId)) {
                return parseStudent(studentElement);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null; 
}



private void deleteStudent(String studentId) {
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();
        NodeList studentElements = universityElement.getElementsByTagName("Student");

        for (int i = 0; i < studentElements.getLength(); i++) {
            Element studentElement = (Element) studentElements.item(i);
            String id = studentElement.getAttribute("ID");

            if (id.equals(studentId)) {
                universityElement.removeChild(studentElement);
                break;
            }
        }

        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(XML_FILE_PATH));
        transformer.transform(source, result);
    } catch (Exception e) {
        e.printStackTrace();
    }
       
}


    private void storeStudent(Student student) {
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();
        NodeList existingStudents = universityElement.getElementsByTagName("Student");

        
        boolean studentExists = false;
        for (int i = 0; i < existingStudents.getLength(); i++) {
            Element existingStudent = (Element) existingStudents.item(i);
            if (existingStudent.getAttribute("ID").equals(student.getId())) {
                
                existingStudent.getElementsByTagName("FirstName").item(0).setTextContent(student.getFirstName());
                existingStudent.getElementsByTagName("LastName").item(0).setTextContent(student.getLastName());
                existingStudent.getElementsByTagName("Gender").item(0).setTextContent(student.getGender());
                existingStudent.getElementsByTagName("GPA").item(0).setTextContent(String.valueOf(student.getGpa()));
                existingStudent.getElementsByTagName("Level").item(0).setTextContent(String.valueOf(student.getLevel()));
                existingStudent.getElementsByTagName("Address").item(0).setTextContent(student.getAddress());
                studentExists = true;
                break;
            }
        }

        
        if (!studentExists) {
            Element studentElement = document.createElement("Student");
            studentElement.setAttribute("ID", student.getId());

            Element firstNameElement = document.createElement("FirstName");
            firstNameElement.appendChild(document.createTextNode(student.getFirstName()));
            studentElement.appendChild(firstNameElement);

            Element lastNameElement = document.createElement("LastName");
            lastNameElement.appendChild(document.createTextNode(student.getLastName()));
            studentElement.appendChild(lastNameElement);

            Element genderElement = document.createElement("Gender");
            genderElement.appendChild(document.createTextNode(student.getGender()));
            studentElement.appendChild(genderElement);

            Element gpaElement = document.createElement("GPA");
            gpaElement.appendChild(document.createTextNode(String.valueOf(student.getGpa())));
            studentElement.appendChild(gpaElement);

            Element levelElement = document.createElement("Level");
            levelElement.appendChild(document.createTextNode(String.valueOf(student.getLevel())));
            studentElement.appendChild(levelElement);

            Element addressElement = document.createElement("Address");
            addressElement.appendChild(document.createTextNode(student.getAddress()));
            studentElement.appendChild(addressElement);

            universityElement.appendChild(studentElement);
        }

        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(XML_FILE_PATH));
        transformer.transform(source, result);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


private void sortStudents(HttpServletRequest request, String sortAttribute, String sortOrder) {
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();
        NodeList studentElements = universityElement.getElementsByTagName("Student");

        List<Student> students = new ArrayList<>();

        for (int i = 0; i < studentElements.getLength(); i++) {
            Element studentElement = (Element) studentElements.item(i);
            students.add(parseStudent(studentElement));
        }

        // Sort students based on the selected attribute and order
        students.sort((s1, s2) -> {
            switch (sortAttribute.toLowerCase()) {
                case "id":
                    return sortOrder.equals("asc") ? s1.getId().compareTo(s2.getId()) : s2.getId().compareTo(s1.getId());
                case "firstname":
                    return sortOrder.equals("asc") ? s1.getFirstName().compareTo(s2.getFirstName()) : s2.getFirstName().compareTo(s1.getFirstName());
                case "lastname":
                    return sortOrder.equals("asc") ? s1.getLastName().compareTo(s2.getLastName()) : s2.getLastName().compareTo(s1.getLastName());
                case "gender":
                    return sortOrder.equals("asc") ? s1.getGender().compareTo(s2.getGender()) : s2.getGender().compareTo(s1.getGender());
                case "gpa":
                    return sortOrder.equals("asc") ? Double.compare(s1.getGpa(), s2.getGpa()) : Double.compare(s2.getGpa(), s1.getGpa());
                case "level":
                    return sortOrder.equals("asc") ? Integer.compare(s1.getLevel(), s2.getLevel()) : Integer.compare(s2.getLevel(), s1.getLevel());
                case "address":
                    return sortOrder.equals("asc") ? s1.getAddress().compareTo(s2.getAddress()) : s2.getAddress().compareTo(s1.getAddress());
               
                default:
                    return 0;
            }
        });

        
        updateXmlFile(students);

        
        Document updatedDocument = reloadXmlDocument();

       
        request.setAttribute("sortedResults", parseStudents(updatedDocument));

    } catch (Exception e) {
        e.printStackTrace();
    }
}

private Document reloadXmlDocument() throws Exception {
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    return documentBuilder.parse(XML_FILE_PATH);
}

private List<Student> parseStudents(Document document) {
    List<Student> students = new ArrayList<>();
    Element universityElement = document.getDocumentElement();
    NodeList studentElements = universityElement.getElementsByTagName("Student");
    for (int i = 0; i < studentElements.getLength(); i++) {
        Element studentElement = (Element) studentElements.item(i);
        students.add(parseStudent(studentElement));
    }
    return students;
}

private void updateXmlFile(List<Student> students) {
    try {
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(XML_FILE_PATH);

        Element universityElement = document.getDocumentElement();

        // Clear existing student elements
        NodeList existingStudents = universityElement.getElementsByTagName("Student");
        for (int i = existingStudents.getLength() - 1; i >= 0; i--) {
            Node studentNode = existingStudents.item(i);
            universityElement.removeChild(studentNode);
        }

        // Use a HashSet to track unique student IDs for this sorting operation
        Set<String> uniqueIds = new HashSet<>();

        // Add sorted students to the document without duplicates
        for (Student student : students) {
            Element studentElement = document.createElement("Student");
            studentElement.setAttribute("ID", student.getId());

            Element firstNameElement = document.createElement("FirstName");
            firstNameElement.appendChild(document.createTextNode(student.getFirstName()));
            studentElement.appendChild(firstNameElement);

            Element lastNameElement = document.createElement("LastName");
            lastNameElement.appendChild(document.createTextNode(student.getLastName()));
            studentElement.appendChild(lastNameElement);

            Element genderElement = document.createElement("Gender");
            genderElement.appendChild(document.createTextNode(student.getGender()));
            studentElement.appendChild(genderElement);

            Element gpaElement = document.createElement("GPA");
            gpaElement.appendChild(document.createTextNode(String.valueOf(student.getGpa())));
            studentElement.appendChild(gpaElement);

            Element levelElement = document.createElement("Level");
            levelElement.appendChild(document.createTextNode(String.valueOf(student.getLevel())));
            studentElement.appendChild(levelElement);

            Element addressElement = document.createElement("Address");
            addressElement.appendChild(document.createTextNode(student.getAddress()));
            studentElement.appendChild(addressElement);

            // Check if the student ID is not already in the HashSet
            if (uniqueIds.add(student.getId())) {
                universityElement.appendChild(studentElement);
            }
        }

        // Save the updated XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(XML_FILE_PATH));
        transformer.transform(source, result);
    } catch (Exception e) {
        e.printStackTrace();
    }
}





    private Student parseStudent(Element studentElement) {
        Student student = new Student();
        student.setId(studentElement.getAttribute("ID"));
        student.setFirstName(studentElement.getElementsByTagName("FirstName").item(0).getTextContent());
        student.setLastName(studentElement.getElementsByTagName("LastName").item(0).getTextContent());
        student.setGender(studentElement.getElementsByTagName("Gender").item(0).getTextContent());
        student.setGpa(Double.parseDouble(studentElement.getElementsByTagName("GPA").item(0).getTextContent()));
        student.setLevel(Integer.parseInt(studentElement.getElementsByTagName("Level").item(0).getTextContent()));
        student.setAddress(studentElement.getElementsByTagName("Address").item(0).getTextContent());
        return student;
    }
    
}