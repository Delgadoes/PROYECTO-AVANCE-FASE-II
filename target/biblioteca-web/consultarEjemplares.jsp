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
        /* Aquí puedes poner estilos similares a tu Swing */
        table {
            border-collapse: collapse;
            width: 80%;
            margin: auto;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px 12px;
            text-align: left;
        }
        th {
            background-color: #eee;
        }
        form {
            width: 80%;
            margin: 20px auto;
        }
        label, select, input, button {
            font-size: 1.1em;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<h1 style="text-align:center;">Consultar Ejemplares</h1>

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
            <td colspan="4" style="text-align:center;">No se encontraron resultados.</td>
        </tr>
    <% } %>
    </tbody>
</table>
    <div style="width: 80%; margin: 20px auto; text-align: center;">
    <button type="button" onclick="window.location.href='alumno.jsp';">Regresar a Inicio</button>
</div>
</body>
</html>
