<%@ page contentType="text/html" pageEncoding="UTF-8" import="javax.servlet.http.HttpSession" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.UsuarioSesion" %>

<%
            int idUsuario = UsuarioSesion.getIdUsuario(session);        
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inicio Alumno</title>
    <style>
        body {
            font-family: Tahoma, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 700px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            margin-bottom: 40px;
        }
        .menu a {
            display: block;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-size: 18px;
            text-align: center;
        }
        .menu a:hover {
            background-color: #45a049;
        }
        .logout {
            margin-top: 30px;
            text-align: center;
        }
        .logout form button {
            background-color: #d9534f;
            border: none;
            padding: 12px 25px;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }
        .logout form button:hover {
            background-color: #c9302c;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Bienvenido, <%= session.getAttribute("nombreUsuario") %></h1>

    <div class="menu">
        <a href="consultarEjemplares.jsp">Consultar Ejemplares</a>
        <a href="solicitarPrestamo.jsp">Solicitar Préstamo</a>
        <a href="solicitarDevolucion.jsp">Devolver Ejemplar</a>
    </div>

    <div class="logout">
    <a href="login.jsp" style="background-color: #d9534f; border: none; padding: 12px 25px; color: white; font-size: 16px; border-radius: 5px; text-decoration: none; display: inline-block;">
        Cerrar sesión
    </a>
</div>
</div>
</body>
</html>
