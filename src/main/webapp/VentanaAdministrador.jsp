<%@ page contentType="text/html" pageEncoding="UTF-8" import="javax.servlet.http.HttpSession" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.UsuarioSesion" %>

<%
    int idUsuario = UsuarioSesion.getIdUsuario(session);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inicio Administrador</title>
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
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            padding: 15px;
            border-radius: 5px;
            font-size: 16px;
            text-align: center;
            transition: background-color 0.3s;
        }
        .menu a:hover {
            background-color: #45a049;
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
    <h1>BIENVENIDO ADMINISTRADOR, <%= session.getAttribute("nombreUsuario") %></h1>

    <div class="menu">
        <a href="gestionUsuarios.jsp">GESTIONAR USUARIOS</a>
        <a href="configurarMora.jsp">CONFIGURACIÓN MORA Y DÍAS</a>
        <a href="agregarEjemplar.jsp">AGREGAR EJEMPLARES</a>
        <a href="consultarEjemplaresadmin.jsp">CONSULTAR/ELIMINAR EJEMPLARES</a>
    </div>

    <div class="logout">
        <a href="login.jsp">CERRAR SESIÓN</a>
    </div>
</div>
</body>
</html>