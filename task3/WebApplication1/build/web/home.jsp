<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="task3.*"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Quiz Game</title>
<style>
        #home-page {
            text-align:center;
        }
        
        #scores {
            width:250px; /* or whatever width you want. */
            max-width:250px; /* or whatever width you want. */
            display: inline-block;
            overflow-y: scroll; 
            height: 100px;
            border: 1px solid black;
        }
        a:link {
            color: green;
        }

        /* visited link */
        a:visited {
            color: green;
        }

        /* mouse over link */
        a:hover {
            color: hotpink;
        }

        /* selected link */
        a:active {
            color: blue;
        }
    </style>
</head>
<body>
    <div id="home-page" style="text-align: center">
        <h1>Welcome to the QUIZ GAME</h1>
        <h1>${user.username}</h1>
        <br><br>
        <h2>Which quiz?</h2>

        <% List quizList = (ArrayList)session.getAttribute("quizzes");
            ArrayList<ArrayList> scores = (ArrayList<ArrayList>)session.getAttribute("scores");
        %>
        <table style="text-align:center; margin-left:46%; margin-right:46% ">
            <%
                for(int i=0; i < quizList.size();i++){
            %>
                    <tr style="text-align:center;">
                        <a style="font-size: 36px; text-decoration: none;" href="/WebApplication1/quiz?chosenquiz=<%= (quizList.get(i)) %>"> <%= (quizList.get(i)) %> </a>
                        <p>Previous scores:</p>
                        <div id="scores">
                            <%
                                if(scores.size() > i){
                            %>
                                    <%
                                      for(int j = scores.get(i).size() - 1; j > -1; j--){
                                    %>
                                        <p>Round <%= (j+1) %> : <%= (scores.get(i).get(j)) %>p</p>
                            <%
                                      }
                                } else {
                            %>
                                    <p>No previous results</p>
                            <%
                                }
                            %>
                        </div>
                        <br>
                    </tr><br>
            <%
                }
            %>
        </table>
        <br><br>${message}   
        <a href="/WebApplication1/logout">Logout</a>
    </div>
</body>
</html>