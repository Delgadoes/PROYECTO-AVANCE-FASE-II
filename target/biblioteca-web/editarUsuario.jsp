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

    // Obtener datos del usuario a editar
    String idStr = request.getParameter("id");
    if (idStr == null || idStr.isEmpty()) {
        response.sendRedirect("gestionUsuarios.jsp");
        return;
    }

    int id = Integer.parseInt(idStr);
    String nombre = "";
    String usuario = "";
    String privilegio = "";

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {

        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                nombre = rs.getString("nombre");
                usuario = rs.getString("usuario");
                privilegio = rs.getString("privilegio");
            } else {
                response.sendRedirect("gestionUsuarios.jsp");
                return;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        response.sendRedirect("gestionUsuarios.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Usuario</title>
    <style>
        body {
            font-family: Tahoma, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 500px;
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
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn-container {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
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
            cursor: pointer;
            border-radius: 5px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .btn-secondary {
            background-color: #555555;
        }
        .btn-secondary:hover {
            background-color: #333333;
        }
        .error {
            color: red;
            margin-top: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>EDITAR USUARIO</h1>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <div class="error"><%= error %></div>
    <% } %>

    <form action="actualizarUsuario" method="POST">
        <input type="hidden" name="id" value="<%= id %>">

        <div class="form-group">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" value="<%= nombre %>" required>
        </div>

        <div class="form-group">
            <label for="usuario">Usuario:</label>
            <input type="text" id="usuario" name="usuario" value="<%= usuario %>" required>
        </div>

        <div class="form-group">
            <label for="contrasena">Nueva Contraseña (dejar en blanco para no cambiar):</label>
            <input type="password" id="contrasena" name="contrasena">
        </div>

        <div class="form-group">
            <label for="privilegio">Privilegio:</label>
            <select id="privilegio" name="privilegio" required>
                <option value="Administrador" <%= "Administrador".equals(privilegio) ? "selected" : "" %>>Administrador</option>
                <option value="Profesor" <%= "Profesor".equals(privilegio) ? "selected" : "" %>>Profesor</option>
                <option value="Alumno" <%= "Alumno".equals(privilegio) ? "selected" : "" %>>Alumno</option>
            </select>
        </div>

        <div class="btn-container">
            <button type="submit" class="btn">Guardar Cambios</button>
            <a href="gestionUsuarios.jsp" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>