<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.UsuarioSesion" %>
<%@ page import="java.sql.*" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.ConexionBD" %>

<%
    // Verificar sesión y privilegios
    HttpSession userSession = request.getSession();
    if (!UsuarioSesion.isLoggedIn(userSession)) {
        response.sendRedirect("../login.jsp");
        return;
    }
    if (!"Administrador".equals(UsuarioSesion.getTipoUsuario(userSession))) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Usuarios</title>
    <style>
        body {
            font-family: Tahoma, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 1000px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 12px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            margin-bottom: 30px;
            font-size: 24px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .btn-container {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 5px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .btn-danger {
            background-color: #f44336;
        }
        .btn-danger:hover {
            background-color: #d32f2f;
        }
        .btn-secondary {
            background-color: #555555;
        }
        .btn-secondary:hover {
            background-color: #333333;
        }
        .message {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 5px;
            text-align: center;
        }
        .success {
            background-color: #dff0d8;
            color: #3c763d;
        }
        .error {
            background-color: #f2dede;
            color: #a94442;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>PANEL - GESTIÓN DE USUARIOS</h1>

    <%
        // Mostrar mensajes de éxito o error
        String message = (String) request.getAttribute("message");
        String messageType = (String) request.getAttribute("messageType");
        if (message != null) {
    %>
    <div class="message <%= messageType %>"><%= message %></div>
    <% } %>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Usuario</th>
            <th>Privilegio</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Cargar usuarios desde la base de datos
            try (Connection conn = ConexionBD.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String usuario = rs.getString("usuario");
                    String privilegio = rs.getString("privilegio");
        %>
        <tr>
            <td><%= id %></td>
            <td><%= nombre %></td>
            <td><%= usuario %></td>
            <td><%= privilegio %></td>
            <td>
                <a href="editarUsuario.jsp?id=<%= id %>" class="btn">Modificar</a>
                <a href="eliminarUsuario?id=<%= id %>" class="btn btn-danger"
                   onclick="return confirm('¿Está seguro de que desea eliminar este usuario?');">Eliminar</a>
            </td>
        </tr>
        <%
                }
            } catch (SQLException e) {
                out.println("<tr><td colspan='5'>Error al cargar usuarios: " + e.getMessage() + "</td></tr>");
            }
        %>
        </tbody>
    </table>

    <div class="btn-container">
        <a href="agregarUsuario.jsp" class="btn">Agregar Usuario</a>
        <a href="VentanaAdministrador.jsp" class="btn btn-secondary">Volver</a>
    </div>
</div>
</body>
</html>