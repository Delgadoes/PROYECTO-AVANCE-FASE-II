package main.java.bibliotecaamigosdonbosco;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/admin/agregarEjemplar")
public class AgregarEjemplarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/agregarEjemplar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipoEjemplar = request.getParameter("tipoEjemplar");
        String titulo = request.getParameter("titulo");
        String autorArtista = request.getParameter("autorArtista");
        String ubicacion = request.getParameter("ubicacion");
        int cantidadTotal = Integer.parseInt(request.getParameter("cantidadTotal"));

        // Validación básica
        if (titulo == null || titulo.isEmpty() ||
                ubicacion == null || ubicacion.isEmpty()) {
            request.setAttribute("error", "Todos los campos básicos son obligatorios");
            request.getRequestDispatcher("/agregarEjemplar.jsp").forward(request, response);
            return;
        }

        // Validación específica por tipo
        if (tipoEjemplar.equals("Revista") && (autorArtista == null || autorArtista.isEmpty())) {
            request.setAttribute("error", "El campo Editorial es obligatorio para revistas");
            request.getRequestDispatcher("/agregarEjemplar.jsp").forward(request, response);
            return;
        } else if (!tipoEjemplar.equals("Revista") && (autorArtista == null || autorArtista.isEmpty())) {
            request.setAttribute("error", "El campo Autor/Artista es obligatorio");
            request.getRequestDispatcher("/agregarEjemplar.jsp").forward(request, response);
            return;
        }

        try {
            boolean success = insertarEjemplar(tipoEjemplar, titulo, autorArtista, ubicacion, cantidadTotal, request);

            if (success) {
                request.setAttribute("mensaje", "Ejemplar agregado correctamente");
            } else {
                request.setAttribute("error", "Error al agregar el ejemplar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
        }

        // Redirigir para evitar reenvío del formulario
        response.sendRedirect(request.getContextPath() + "/admin/consultarEjemplares");
    }

    private boolean insertarEjemplar(String tipo, String titulo, String autorArtista, String ubicacion,
                                     int cantidadTotal, HttpServletRequest request) throws SQLException {
        switch (tipo) {
            case "Libro":
                String editorialLibro = request.getParameter("editorialLibro");
                String isbn = request.getParameter("isbn");
                String anoPublicacionLibro = request.getParameter("anoPublicacionLibro");

                String sqlLibro = "INSERT INTO libros (titulo, autor, editorial, isbn, ano_publicacion, ubicacion_fisica, cantidad_total) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = ConexionBD.getConnection();
                     PreparedStatement pst = conn.prepareStatement(sqlLibro)) {
                    pst.setString(1, titulo);
                    pst.setString(2, autorArtista);
                    pst.setString(3, editorialLibro);
                    pst.setString(4, isbn);
                    pst.setString(5, anoPublicacionLibro);
                    pst.setString(6, ubicacion);
                    pst.setInt(7, cantidadTotal);
                    return pst.executeUpdate() > 0;
                }

            case "Revista":
                String editorialRevista = request.getParameter("editorialRevista");
                String issn = request.getParameter("issn");
                String anoPublicacionRevista = request.getParameter("anoPublicacionRevista");

                String sqlRevista = "INSERT INTO revistas (titulo, editorial, isbn, ano_publicacion, ubicacion_fisica, cantidad_total) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = ConexionBD.getConnection();
                     PreparedStatement pst = conn.prepareStatement(sqlRevista)) {
                    pst.setString(1, titulo);
                    pst.setString(2, autorArtista); // En revistas, autorArtista es la editorial
                    pst.setString(3, issn); // Cambiado de issn a isbn si es necesario
                    pst.setString(4, anoPublicacionRevista);
                    pst.setString(5, ubicacion);
                    pst.setInt(6, cantidadTotal);
                    return pst.executeUpdate() > 0;
                }

            case "Tesis":
                String universidad = request.getParameter("universidad");
                String anio = request.getParameter("anio");

                String sqlTesis = "INSERT INTO tesis (titulo, autor, año, universidad, ubicacion_fisica, cantidad_total) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = ConexionBD.getConnection();
                     PreparedStatement pst = conn.prepareStatement(sqlTesis)) {
                    pst.setString(1, titulo);
                    pst.setString(2, autorArtista);
                    pst.setString(3, anio);
                    pst.setString(4, universidad);
                    pst.setString(5, ubicacion);
                    pst.setInt(6, cantidadTotal);
                    return pst.executeUpdate() > 0;
                }

            case "Obra":
                String anoCreacionObra = request.getParameter("anoCreacionObra");

                String sqlObra = "INSERT INTO obras (titulo, artista, ano_creacion, ubicacion_fisica, cantidad_total, genero) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = ConexionBD.getConnection();
                     PreparedStatement pst = conn.prepareStatement(sqlObra)) {
                    String genero = request.getParameter("genero"); // Declarada dentro del bloque
                    pst.setString(1, titulo);
                    pst.setString(2, autorArtista);
                    pst.setString(3, anoCreacionObra);
                    pst.setString(4, ubicacion);
                    pst.setInt(5, cantidadTotal);
                    pst.setString(6, genero != null ? genero : "");
                    return pst.executeUpdate() > 0;
                }

            case "CD":
                String anoPublicacionCD = request.getParameter("anoPublicacionCD");

                String sqlCD = "INSERT INTO CDs (titulo, artista, genero, ano_publicacion, ubicacion_fisica, cantidad_total) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = ConexionBD.getConnection();
                     PreparedStatement pst = conn.prepareStatement(sqlCD)) {
                    String genero = request.getParameter("genero"); // Declarada dentro del bloque
                    pst.setString(1, titulo);
                    pst.setString(2, autorArtista);
                    pst.setString(3, genero);
                    pst.setString(4, anoPublicacionCD);
                    pst.setString(5, ubicacion);
                    pst.setInt(6, cantidadTotal);
                    return pst.executeUpdate() > 0;
                }

            default:
                return false;
        }
    }
}