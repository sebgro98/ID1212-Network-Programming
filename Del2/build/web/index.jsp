<%-- 
    Document   : index
    Created on : Nov 28, 2023, 4:58:57 PM
    Author     : Zebbe
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
    <h1>Welcome, <%= session.getAttribute("username") %>!</h1>
    <p>Choose a quiz:</p>
    <ul>
        <% Map<String, QuizModel> quizzes = DatabaseManager.getAllQuizzes();
           for (Map.Entry<String, QuizModel> entry : quizzes.entrySet()) { %>
               <li><a href="QuizServlet?quizId=<%= entry.getKey() %>">Quiz <%= entry.getKey() %></a></li>
        <% } %>
    </ul>
    <p>Previous Results:</p>
    <a href="logout.jsp">Logout</a>
</body>
</html>

