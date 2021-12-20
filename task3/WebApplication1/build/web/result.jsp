<%@page import="task3.Model.Quiz"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Your result for the ${quiz.subject} quiz!</h1>
        <h2>Score: ${score}</h2>

        <% String[] questionList = (String[])session.getAttribute("questionTexts");%>
        <div style="text-align:center; margin-right:5%">
        <%
            for(int i=0; i < questionList.length;i++){
                String correct = (String)session.getAttribute("correct" + i);
        %>
                <div style="display:block">
                    <p><%= (questionList[i]) %></p>
                    <p> <%= (correct) %></p>    
                </div>
        <%
            }
        %>
        </div>
        <a href="/WebApplication1/quiz?chosenquiz=${quiz.subject}">Play Again</a>
        <a href="/WebApplication1/score">Home</a>
    </body>
</html>
