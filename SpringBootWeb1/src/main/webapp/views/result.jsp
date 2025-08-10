<%@page language = "java" %>

<html>
    <head>
        <link rel = "stylesheet" type = "text/css" href = "style.css">
    </head>
    <body>
        <h2>Result is: <%= session.getAttribute("alien") %></h2>
        <h2>Result is: ${alien}</h2>

        <h2>Result is: ${alien1}</h2>

        <p>Welcome to ${course} world</p>

    </body>
</html>