<%@ page contentType="text/html" pageEncoding="UTF-8" import="javax.servlet.http.HttpSession" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.UsuarioSesion" %>

<%
    int idUsuario = UsuarioSesion.getIdUsuario(session);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inicio Profesor</title>
    <style>
        body {
            font-family: Tahoma, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            margin-bottom: 40px;
            font-size: 24px;
        }
        .menu {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            margin-bottom: 30px;
        }
        .menu a {
            display: block;
            background-color: #337ab7;
            color: white;
            text-decoration: none;
            padding: 15px;
            border-radius: 5px;
            font-size: 16px;
            text-align: center;
            transition: background-color 0.3s;
        }
        .menu a:hover {
            background-color: #286090;
        }
        .logout {
            text-align: center;
            margin-top: 30px;
        }
        .logout a {
            background-color: #d9534f;
            border: none;
            padding: 12px 25px;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            text-decoration: none;
            display: inline-block;
        }
        .logout a:hover {
            background-color: #c9302c;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>BIENVENIDO PROFESOR, <%= session.getAttribute("nombreUsuario") %></h1>

    <div class="menu">
        <a href="consultarEjemplares.jsp">CONSULTAR EJEMPLARES</a>
        <a href="solicitarPrestamo.jsp">GESTIÓN DE PRÉSTAMOS</a>
        <a href="agregarEjemplar.jsp">AGREGAR EJEMPLARES</a>
    </div>

    <div class="logout">
        <a href="login.jsp">CERRAR SESIÓN</a>
    </div>
</div>
</body>
</html>