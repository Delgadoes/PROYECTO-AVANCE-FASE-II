<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="main.java.bibliotecaamigosdonbosco.SolicitarPrestamoServlet.Ejemplar" %>

<%
    List<Ejemplar> listaEjemplares = (List<Ejemplar>) request.getAttribute("listaEjemplares");
    if (listaEjemplares == null) listaEjemplares = new java.util.ArrayList<>();
    String tipoEjemplarSeleccionado = (String) request.getAttribute("tipoEjemplarSeleccionado");
    if (tipoEjemplarSeleccionado == null) tipoEjemplarSeleccionado = "";

    String mensaje = (String) request.getAttribute("mensaje");
    if (mensaje == null) mensaje = "";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Solicitar Préstamo</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f0f4f8;
            margin: 0;
            padding: 20px;
        }

        h1 {
            text-align: center;
            color: #2c3e50;
        }

        form {
            background-color: #fff;
            max-width: 500px;
            margin: 20px auto;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #34495e;
        }

        select, input[type="date"], button {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 8px;
            border: 1px solid #ccc;
            box-sizing: border-box;
            font-size: 14px;
        }

        select:disabled {
            background-color: #eee;
        }

        button {
            background-color: #3498db;
            color: white;
            border: none;
            cursor: pointer;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #2980b9;
        }

        button[type="submit"]:last-of-type {
            background-color: #e74c3c;
        }

        button[type="submit"]:last-of-type:hover {
            background-color: #c0392b;
        }

        p {
            text-align: center;
            font-weight: bold;
            color: #e67e22;
        }
    </style>
</head>
<body>

<h1>Solicitar Préstamo</h1>

<form method="post" action="SolicitarPrestamoServlet">
    <input type="hidden" name="accion" value="Buscar" />

    <label for="tipoEjemplar">Tipo de Ejemplar:</label>
    <select name="tipoEjemplar" id="tipoEjemplar" onchange="this.form.submit()" required>
        <option value="">--Seleccione--</option>
        <option value="Libro" <%= "Libro".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Libro</option>
        <option value="Revista" <%= "Revista".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Revista</option>
        <option value="Obra" <%= "Obra".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Obra</option>
        <option value="Tesis" <%= "Tesis".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>Tesis</option>
        <option value="CD" <%= "CD".equals(tipoEjemplarSeleccionado) ? "selected" : "" %>>CD</option>
    </select>
</form>

<% if (!tipoEjemplarSeleccionado.isEmpty()) { %>
    <form method="post" action="SolicitarPrestamoServlet">
        <input type="hidden" name="tipoEjemplar" value="<%= tipoEjemplarSeleccionado %>" />

        <label for="ejemplarSeleccionado">Ejemplar:</label>
        <select name="ejemplarSeleccionado" id="ejemplarSeleccionado" required>
            <% for (Ejemplar e : listaEjemplares) { %>
                <option value="<%= e.getId() %>"><%= e.getTitulo() %> - <%= e.getAutorArtista() %></option>
            <% } %>
            <% if (listaEjemplares.isEmpty()) { %>
                <option disabled>No hay ejemplares disponibles</option>
            <% } %>
        </select>

        <label for="fechaPrestamo">Fecha Préstamo:</label>
        <input type="date" id="fechaPrestamo" name="fechaPrestamo" required
               value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" />

        <button type="submit" name="accion" value="Confirmar Prestamo">Confirmar Préstamo</button>
        <button type="submit" name="accion" value="Cancelar">Cancelar</button>
    </form>
<% } %>

<% if (!mensaje.isEmpty()) { %>
    <p><%= mensaje %></p>
<% } %>

</body>
</html>
