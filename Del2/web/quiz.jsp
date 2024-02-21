<%-- 
    Document   : quiz
    Created on : Nov 28, 2023, 5:03:59 PM
    Author     : Zebbe
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Page</title>
</head>
<body>
    <h1>Quiz</h1>
    <form action="QuizResultServlet" method="POST">
        <h2>Questions:</h2>
        <ul>
            <% List<QuizModel.Question> questions = (List<QuizModel.Question>)request.getAttribute("quiz").getQuestions();
               for (int i = 0; i < questions.size(); i++) { %>
                   <li>
                       <%= questions.get(i).getText() %><br>
                       <% for (int j = 0; j < questions.get(i).getOptions().length; j++) { %>
                           <input type="radio" name="answer_<%= i %>" value="<%= j %>"><%= questions.get(i).getOptions()[j] %><br>
                       <% } %>
                   </li>
            <% } %>
        </ul>
        <input type="submit" value="Submit Quiz">
    </form>
</body>
</html>

