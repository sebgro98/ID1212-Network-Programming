<%-- 
    Document   : login
    Created on : Nov 28, 2023, 5:02:34 PM
    Author     : Zebbe
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
    <h1>Login</h1>
    <form action="LoginServlet" method="POST">
        <label for="username">Username (email): </label>
        <input type="text" name="username" required><br>
        <label for="password">Password: </label>
        <input type="password" name="password" required><br>
        <input type="submit" value="Login">
    </form>
</body>
</html>
