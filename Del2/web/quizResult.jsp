<%-- 
    Document   : quizResult
    Created on : Nov 28, 2023, 5:06:29 PM
    Author     : Zebbe
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Result</title>
</head>
<body>
    <h1>Quiz Result</h1>
    <p>Your score: <%= request.getAttribute("score") %> out of <%= ((List<QuizModel.Question>) request.getAttribute("quiz").getQuestions()).size() %></p>
    <a href="main.jsp">Back to Main Page</a>
</body>
</html>

