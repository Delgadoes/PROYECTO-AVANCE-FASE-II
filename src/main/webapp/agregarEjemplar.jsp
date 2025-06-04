<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Ejemplar - Administrador</title>
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

        .form-container {
            background-color: #ffffff;
            padding: 30px;
            width: 60%;
            margin: 20px auto;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            border-radius: 10px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            color: #34495e;
            margin-bottom: 8px;
        }

        select, input[type="text"], input[type="number"] {
            width: 100%;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ccc;
            border-radius: 6px;
            box-sizing: border-box;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }

        button {
            padding: 10px 20px;
            font-weight: bold;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .save-btn {
            background-color: #2ecc71;
            color: white;
        }

        .save-btn:hover {
            background-color: #27ae60;
        }

        .back-btn {
            background-color: #e74c3c;
            color: white;
        }

        .back-btn:hover {
            background-color: #c0392b;
        }

        .message {
            width: 60%;
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

        .campos-adicionales {
            margin-top: 20px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
            border: 1px solid #dee2e6;
        }
    </style>
</head>
<body>

<h1>Agregar Nuevo Ejemplar</h1>

<c:if test="${not empty error}">
    <div class="message error">${error}</div>
</c:if>
<c:if test="${not empty mensaje}">
    <div class="message success">${mensaje}</div>
</c:if>

<div class="form-container">
    <form method="post" action="${pageContext.request.contextPath}/admin/agregarEjemplar">
        <div class="form-group">
            <label for="tipoEjemplar">Tipo de Ejemplar:</label>
            <select id="tipoEjemplar" name="tipoEjemplar" required onchange="mostrarCamposAdicionales()">
                <option value="">-- Seleccione --</option>
                <option value="Libro">Libro</option>
                <option value="Revista">Revista</option>
                <option value="Tesis">Tesis</option>
                <option value="Obra">Obra</option>
                <option value="CD">CD</option>
            </select>
        </div>

        <div class="form-group">
            <label for="titulo">Título:</label>
            <input type="text" id="titulo" name="titulo" required>
        </div>

        <div class="form-group">
            <label id="labelAutorArtista" for="autorArtista">Autor/Artista:</label>
            <input type="text" id="autorArtista" name="autorArtista" required>
        </div>

        <div class="form-group">
            <label for="ubicacion">Ubicación Física:</label>
            <input type="text" id="ubicacion" name="ubicacion" required>
        </div>

        <div class="form-group">
            <label for="cantidadTotal">Cantidad Total:</label>
            <input type="number" id="cantidadTotal" name="cantidadTotal" min="1" value="1" required>
        </div>

        <!-- Campos adicionales para Libro -->
        <div id="camposLibro" class="campos-adicionales" style="display:none;">
            <div class="form-group">
                <label for="editorialLibro">Editorial:</label>
                <input type="text" id="editorialLibro" name="editorialLibro">
            </div>

            <div class="form-group">
                <label for="isbn">ISBN:</label>
                <input type="text" id="isbn" name="isbn">
            </div>

            <div class="form-group">
                <label for="anoPublicacionLibro">Año de Publicación:</label>
                <input type="text" id="anoPublicacionLibro" name="anoPublicacionLibro">
            </div>
        </div>

        <!-- Campos adicionales para Revista -->
        <div id="camposRevista" class="campos-adicionales" style="display:none;">
            <div class="form-group">
                <label for="editorialRevista">Editorial:</label>
                <input type="text" id="editorialRevista" name="editorialRevista">
            </div>

            <div class="form-group">
                <label for="issn">ISSN:</label>
                <input type="text" id="issn" name="issn">
            </div>

            <div class="form-group">
                <label for="anoPublicacionRevista">Año de Publicación:</label>
                <input type="text" id="anoPublicacionRevista" name="anoPublicacionRevista">
            </div>
        </div>

        <!-- Campos adicionales para Tesis -->
        <div id="camposTesis" class="campos-adicionales" style="display:none;">
            <div class="form-group">
                <label for="universidad">Universidad:</label>
                <input type="text" id="universidad" name="universidad">
            </div>

            <div class="form-group">
                <label for="anio">Año de la Tesis:</label>
                <input type="text" id="anio" name="anio">

            </div>
        </div>

        <!-- Campos adicionales para Obra -->
        <div id="camposObra" class="campos-adicionales" style="display:none;">
            <div class="form-group">
                <label for="generoObra">Género:</label>
                <input type="text" id="generoObra" name="generoObra">
            </div>

            <div class="form-group">
                <label for="anoCreacionObra">Año de Creación:</label>
                <input type="text" id="anoCreacionObra" name="anoCreacionObra">
            </div>
        </div>

        <!-- Campos adicionales para CD -->
        <div id="camposCD" class="campos-adicionales" style="display:none;">
            <div class="form-group">
                <label for="genero">Género:</label>
                <input type="text" id="genero" name="genero">
            </div>

            <div class="form-group">
                <label for="anoPublicacionCD">Año de Publicación:</label>
                <input type="text" id="anoPublicacionCD" name="anoPublicacionCD">
            </div>
        </div>

        <div class="button-group">
            <button type="button" class="back-btn"
                    onclick="window.location.href='${pageContext.request.contextPath}/admin/consultarEjemplares'">
                Cancelar
            </button>
            <button type="submit" class="save-btn">Guardar Ejemplar</button>
        </div>
    </form>
</div>

<script>
    function mostrarCamposAdicionales() {
        // Ocultar todos los campos adicionales primero
        document.querySelectorAll('.campos-adicionales').forEach(function(el) {
            el.style.display = 'none';
        });

        // Cambiar label según el tipo
        const tipo = document.getElementById('tipoEjemplar').value;
        const label = document.getElementById('labelAutorArtista');

        switch(tipo) {
            case 'Libro':
                document.getElementById('camposLibro').style.display = 'block';
                label.textContent = 'Autor:';
                break;
            case 'Revista':
                document.getElementById('camposRevista').style.display = 'block';
                label.textContent = 'Editorial:';
                document.getElementById('autorArtista').placeholder = "Nombre de la editorial";
                break;
            case 'Tesis':
                document.getElementById('camposTesis').style.display = 'block';
                label.textContent = 'Autor:';
                break;
            case 'Obra':
                document.getElementById('camposObra').style.display = 'block';
                label.textContent = 'Artista:';
                break;
            case 'CD':
                document.getElementById('camposCD').style.display = 'block';
                label.textContent = 'Artista:';
                break;
            default:
                label.textContent = 'Autor/Artista:';
        }
    }
</script>

</body>
</html>