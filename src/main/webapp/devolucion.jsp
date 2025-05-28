<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Obtener la fecha actual en formato yyyy-MM-dd
    String fechaActual = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Realizar Devolución</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 40px auto;
            background-color: white;
            padding: 30px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            border-radius: 10px;
        }

        h1 {
            text-align: center;
            color: #2c3e50;
        }

        label {
            display: block;
            margin: 15px 0 5px;
            font-weight: bold;
        }

        select, input[type="date"], button {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }

        button {
            margin-top: 20px;
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

        button[name="accion"][value="cancelar"] {
            background-color: #e74c3c;
            margin-top: 10px;
        }

        button[name="accion"][value="cancelar"]:hover {
            background-color: #c0392b;
        }

        p {
            text-align: center;
            margin-top: 20px;
            font-weight: bold;
        }

        .success {
            color: green;
        }

        .error {
            color: red;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>REALIZAR DEVOLUCIÓN</h1>

    <form method="post" action="DevolucionServlet">
        <label>TIPO DE EJEMPLAR:</label>
        <select name="tipoEjemplar" onchange="this.form.submit()">
            <option value="">Seleccione</option>
            <option value="Libro" <c:if test="${param.tipoEjemplar == 'Libro'}">selected</c:if>>Libro</option>
            <option value="Revista" <c:if test="${param.tipoEjemplar == 'Revista'}">selected</c:if>>Revista</option>
            <option value="Tesis" <c:if test="${param.tipoEjemplar == 'Tesis'}">selected</c:if>>Tesis</option>
            <option value="Obra" <c:if test="${param.tipoEjemplar == 'Obra'}">selected</c:if>>Obra</option>
            <option value="CD" <c:if test="${param.tipoEjemplar == 'CD'}">selected</c:if>>CD</option>
        </select>

        <label>SELECCIONAR EJEMPLAR:</label>
        <select name="prestamoSeleccionado">
            <c:forEach var="prestamo" items="${prestamosPendientes}">
                <option value="${prestamo.id}">${prestamo.id} - ${prestamo.titulo}</option>
            </c:forEach>
        </select>

        <label>FECHA DE DEVOLUCIÓN:</label>
        <input type="date" name="fechaDevolucion" required value="<%= fechaActual %>">

        <button type="submit" name="accion" value="confirmar">CONFIRMAR DEVOLUCIÓN</button>
        <button type="submit" name="accion" value="cancelar">CANCELAR</button>
    </form>

    <c:if test="${not empty mensaje}">
        <p class="success">${mensaje}</p>
    </c:if>
</div>
</body>
</html>
