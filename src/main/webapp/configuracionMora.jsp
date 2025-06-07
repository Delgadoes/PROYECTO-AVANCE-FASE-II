<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.ConfiguracionMoraServlet.ConfiguracionMora" %>

<%
    Integer diasAntesMora = (Integer) request.getAttribute("diasAntesMora");
    Integer limitePrestamos = (Integer) request.getAttribute("limitePrestamos");
    List<ConfiguracionMora> configuraciones = (List<ConfiguracionMora>) request.getAttribute("configuraciones");

    String mensajeExito = (String) request.getAttribute("mensajeExito");
    String mensajeError = (String) request.getAttribute("mensajeError");

    if (diasAntesMora == null) diasAntesMora = 30;
    if (limitePrestamos == null) limitePrestamos = 2;
    if (configuraciones == null) configuraciones = new java.util.ArrayList<>();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Configuración de Mora</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f0f4f8;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
        }

        .section {
            margin-bottom: 30px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
        }

        .section-title {
            font-size: 1.2em;
            font-weight: bold;
            color: #34495e;
            margin-bottom: 15px;
            border-bottom: 1px solid #eee;
            padding-bottom: 10px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #34495e;
        }

        input[type="text"], input[type="number"], select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        button {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #2980b9;
        }

        .btn-danger {
            background-color: #e74c3c;
        }

        .btn-danger:hover {
            background-color: #c0392b;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        .message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            text-align: center;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Configuración de Mora y Préstamos</h1>

    <% if (mensajeExito != null) { %>
    <div class="message success"><%= mensajeExito %></div>
    <% } %>

    <% if (mensajeError != null) { %>
    <div class="message error"><%= mensajeError %></div>
    <% } %>

    <div class="section">
        <div class="section-title">Configuración General del Sistema</div>
        <form method="post" action="ConfiguracionMoraServlet">
            <input type="hidden" name="accion" value="guardarSistema">

            <div class="form-group">
                <label for="diasAntesMora">Días antes de aplicar mora:</label>
                <input type="number" id="diasAntesMora" name="diasAntesMora"
                       value="<%= diasAntesMora %>" min="1" required>
            </div>

            <div class="form-group">
                <label for="limitePrestamos">Límite de préstamos por usuario:</label>
                <input type="number" id="limitePrestamos" name="limitePrestamos"
                       value="<%= limitePrestamos %>" min="1" required>
            </div>

            <button type="submit">Guardar Configuración</button>
        </form>
    </div>

    <div class="section">
        <div class="section-title">Configuración de Mora por Año</div>
        <form method="post" action="ConfiguracionMoraServlet">
            <input type="hidden" name="accion" value="guardarMora">

            <div class="form-group">
                <label for="anio">Año:</label>
                <select id="anio" name="anio" required>
                    <option value="">-- Seleccione un año --</option>
                    <%
                        int anioActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                        for (int i = anioActual; i <= anioActual + 10; i++) {
                    %>
                    <option value="<%= i %>"><%= i %></option>
                    <% } %>
                </select>
            </div>

            <div class="form-group">
                <label for="moraDiaria">Mora diaria (USD $):</label>
                <input type="number" id="moraDiaria" name="moraDiaria"
                       step="0.01" min="0" required>
            </div>

            <button type="submit">Guardar Configuración de Mora</button>
        </form>

        <h3>Configuraciones Existentes</h3>
        <table>
            <thead>
            <tr>
                <th>Año</th>
                <th>Mora Diaria (USD $)</th>
            </tr>
            </thead>
            <tbody>
            <% for (ConfiguracionMora configItem : configuraciones) { %>
            <tr>
                <td><%= configItem.getAnio() %></td>
                <td><%= String.format("%.2f", configItem.getMoraDiaria()) %></td>
            </tr>
            <% } %>

            <% if (configuraciones.isEmpty()) { %>
            <tr>
                <td colspan="2" style="text-align: center;">No hay configuraciones registradas</td>
            </tr>
            <% } %>
            </tbody>
        </table>

    </div>
    <div class="button-container">
        <button type="button" onclick="window.location.href='VentanaAdministrador.jsp';">Regresar</button>
    </div>
</div>
</body>
</html>