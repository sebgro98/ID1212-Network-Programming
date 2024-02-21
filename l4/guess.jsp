<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Guessing Game</title>
</head>
<body>
    <h1>Guessing Game</h1>
    <form method="post" action="GuessController">
        Enter your guess: <input type="text" name="userGuess">
        <input type="submit" value="Submit">
    </form>
    <p>${result}</p>
</body>
</html>
