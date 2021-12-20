<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="examples.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quiz</title>
        <style>
            
        </style>
    </head>
    <body  id="grid-container"  style="text-align:center;">
        <div id="quiz-page">
            <h1>Hello ${user.username},</h1>
            <h1>Welcome to the ${quiz.subject} quiz!</h1>
            <h3>Questions:</h3>
            <% String[] questionList = (String[])session.getAttribute("questionTexts");%>
            <% ArrayList<String[]> optionsList = (ArrayList)session.getAttribute("questionOptions");%>
            <form name="quizForm" action="quiz" method="post" style="display:inline-block">
            <div style="text-align:center">
            <%
                for(int i=0; i < questionList.length;i++){
                    String[] ops = optionsList.get(i);
                    int j;
            %>
                    <div style="display:block">
                        <p><%= (questionList[i]) %></p>
                        <% 
                            for (j = 0; j < ops.length; j++){ 
                        %>
                                <input type="checkbox" name="check<%= (i) %>.<%= (j) %>" value="<%= (j) %>">
                                <label for="check${i}.${i}" > <%= (ops[j]) %> </label><br><br>
                                <br>
                        <%
                            }
                        %>    
                    </div>
            <%
                }
            %>
            <input type="submit" name="submit" value="Submit"> 
            </form>
            </div>
        </div>
    </body>
</html>

