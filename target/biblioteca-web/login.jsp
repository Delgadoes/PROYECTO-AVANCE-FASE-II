<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>INICIO DE SESIÓN</title>
    <style>
        body {
            font-family: Tahoma, sans-serif;
            background-color: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            width: 400px;
        }
        h1 {
            text-align: center;
            font-size: 24px;
            margin-bottom: 30px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-size: 18px;
        }
        input, select {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 18px;
            cursor: pointer;
            margin-bottom: 10px;
        }
        button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            margin-bottom: 15px;
            text-align: center;
        }
        .guest-access {
            text-align: center;
            margin-top: 20px;
        }
        .guest-access a {
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
        }
        .guest-access a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h1>INICIO DE SESIÓN</h1>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="login" method="POST" accept-charset="UTF-8">
        <div class="form-group">
            <label for="usuario">USUARIO</label>
            <input type="text" id="usuario" name="usuario" required>
        </div>

        <div class="form-group">
            <label for="contraseña">CONTRASEÑA</label>
            <input type="password" id="contraseña" name="contraseña" required>
        </div>

        <div class="form-group">
            <label for="tipoUsuario">TIPO DE USUARIO</label>
            <select id="tipoUsuario" name="tipoUsuario" required>
                <option value="">Seleccione...</option>
                <option value="Administrador">Administrador</option>
                <option value="Profesor">Profesor</option>
                <option value="Alumno">Alumno</option>
            </select>
        </div>

        <button type="submit">INICIAR SESIÓN</button>
    </form>

    <div class="guest-access">
        <p>¿Solo quieres consultar ejemplares? <a href="consultarEjemplaresInvitado.jsp">Accede como invitado</a></p>
    </div>
</div>
</body>
</html>