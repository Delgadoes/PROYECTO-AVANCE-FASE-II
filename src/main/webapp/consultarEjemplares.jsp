<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.ConsultarEjemplaresAlumnos.Ejemplar" %>

<%
    List<Ejemplar> listaEjemplares = (List<Ejemplar>) request.getAttribute("listaEjemplares");
    if (listaEjemplares == null) listaEjemplares = new java.util.ArrayList<>();
    String tipoEjemplarSeleccionado = (String) request.getAttribute("tipoEjemplarSeleccionado");
    String buscarTexto = (String) request.getAttribute("buscarTexto");
    if (tipoEjemplarSeleccionado == null) tipoEjemplarSeleccionado = "";
    if (buscarTexto == null) buscarTexto = "";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consultar Ejemplares</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            color: #2c3e50;
            margin-top: 30px;
        }

        form {
            background-color: #ffffff;
            padding: 20px;
            width: 80%;
            margin: 20px auto;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            border-radius: 10px;
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
            justify-content: center;
        }

        label {
            font-weight: bold;
            color: #34495e;
        }

        select, input[type="text"] {
            padding: 8px 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 6px;
            min-width: 150px;
        }

        button {
            padding: 10px 20px;
            background-color: #3498db;
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #2980b9;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
            border-radius: 10px;
            overflow: hidden;
        }

        th, td {
            padding: 12px 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #ecf0f1;
            color: #2c3e50;
        }

        td {
            color: #2c3e50;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .no-results {
            text-align: center;
            padding: 20px;
            font-weight: bold;
            color: #888;
        }

        .button-container {
            width: 80%;
            margin: 20px auto;
            text-align: center;
        }

        .button-container button {
            background-color: #e74c3c;
        }

        .button-container button:hover {
            background-color: #c0392b;
        }
    </style>
</head>
<body>

<h1>Consultar Ejemplares</h1>

<form method="get" action="consultarEjemplares">
    <label for="tipoEjemplar">Tipo de Ejemplar:</label>
    <select id="tipoEjemplar" name="tipoEjemplar" required>
        <option value="">-- Seleccione --</option>
        <option value="Libro" <%= "Libro".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Libro</option>
        <option value="Revista" <%= "Revista".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Revista</option>
        <option value="Obra" <%= "Obra".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Obra</option>
        <option value="Tesis" <%= "Tesis".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Tesis</option>
        <option value="CD" <%= "CD".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>CD</option>
    </select>

    <label for="buscar">Buscar por título:</label>
    <input type="text" id="buscar" name="buscar" value="<%= buscarTexto %>" />

    <button type="submit">Buscar</button>
</form>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Autor/Artista</th>
            <th>Ubicación</th>
        </tr>
    </thead>
    <tbody>
    <% for(Ejemplar e : listaEjemplares) { %>
        <tr>
            <td><%= e.getId() %></td>
            <td><%= e.getTitulo() %></td>
            <td><%= e.getAutorArtista() %></td>
            <td><%= e.getUbicacion() %></td>
        </tr>
    <% } %>
    <% if(listaEjemplares.isEmpty()) { %>
        <tr>
            <td colspan="4" class="no-results">No se encontraron resultados.</td>
        </tr>
    <% } %>
    </tbody>
</table>

<div class="button-container">
    <button type="button" onclick="window.location.href='alumno.jsp';">Regresar</button>
</div>

</body>
</html>
