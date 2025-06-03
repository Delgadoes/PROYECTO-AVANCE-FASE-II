<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consultar Ejemplares - Administrador</title>
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

        form.search-form {
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
            font-weight: bold;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .search-btn {
            background-color: #3498db;
            color: white;
        }

        .search-btn:hover {
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

        .back-btn {
            background-color: #e74c3c;
            color: white;
        }

        .back-btn:hover {
            background-color: #c0392b;
        }

        .delete-btn {
            background-color: #e74c3c;
            color: white;
            padding: 5px 10px;
            font-size: 0.9em;
        }

        .delete-btn:hover {
            background-color: #c0392b;
        }

        .message {
            width: 80%;
            margin: 10px auto;
            padding: 10px;
            border-radius: 5px;
            text-align: center;
        }

        .error {
            background-color: #ffdddd;
            color: #d8000c;
        }

        .success {
            background-color: #ddffdd;
            color: #4F8A10;
        }
    </style>
</head>
<body>

<h1>Consultar Ejemplares - Administrador</h1>

<c:if test="${not empty error}">
    <div class="message error">${error}</div>
</c:if>
<c:if test="${not empty mensaje}">
    <div class="message success">${mensaje}</div>
</c:if>

<form class="search-form" method="get" action="${pageContext.request.contextPath}/admin/consultarEjemplares">
    <label for="tipoEjemplar">Tipo de Ejemplar:</label>
    <select id="tipoEjemplar" name="tipoEjemplar" required>
        <option value="">-- Seleccione --</option>
        <option value="Libro" ${param.tipoEjemplar == 'Libro' ? 'selected' : ''}>Libro</option>
        <option value="Revista" ${param.tipoEjemplar == 'Revista' ? 'selected' : ''}>Revista</option>
        <option value="Obra" ${param.tipoEjemplar == 'Obra' ? 'selected' : ''}>Obra</option>
        <option value="Tesis" ${param.tipoEjemplar == 'Tesis' ? 'selected' : ''}>Tesis</option>
        <option value="CD" ${param.tipoEjemplar == 'CD' ? 'selected' : ''}>CD</option>
    </select>

    <label for="buscar">Buscar por título:</label>
    <input type="text" id="buscar" name="buscar" value="${param.buscar}" />

    <button type="submit" class="search-btn">Buscar</button>
</form>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Autor/Artista</th>
        <th>Ubicación</th>
        <th>Total</th>
        <th>Prestados</th>
        <th>Acción</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${listaEjemplares}" var="ejemplar">
        <tr>
            <td>${ejemplar.id}</td>
            <td>${ejemplar.titulo}</td>
            <td>${ejemplar.autorArtista}</td>
            <td>${ejemplar.ubicacion}</td>
            <td>${ejemplar.cantidadTotal}</td>
            <td>${ejemplar.cantidadPrestados}</td>
            <td>
                <form method="post" action="${pageContext.request.contextPath}/admin/consultarEjemplares"
                      onsubmit="return confirm('¿Está seguro de eliminar este ejemplar?')">
                    <input type="hidden" name="id" value="${ejemplar.id}">
                    <input type="hidden" name="tipoEjemplar" value="${ejemplar.tipo}">
                    <button type="submit" class="delete-btn">Eliminar</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty listaEjemplares}">
        <tr>
            <td colspan="7" class="no-results">No se encontraron resultados.</td>
        </tr>
    </c:if>
    </tbody>
</table>

<div class="button-container">
    <button type="button" class="back-btn"
            onclick="window.location.href='${pageContext.request.contextPath}/VentanaAdministrador.jsp';">
        Regresar
    </button>
</div>

</body>
</html>