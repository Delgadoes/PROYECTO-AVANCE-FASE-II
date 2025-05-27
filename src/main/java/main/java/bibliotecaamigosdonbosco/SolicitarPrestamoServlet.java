package main.java.bibliotecaamigosdonbosco;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/SolicitarPrestamoServlet")
public class SolicitarPrestamoServlet extends HttpServlet {

    public static class Ejemplar {
        private int id;
        private String titulo;
        private String autorArtista;
        private String ubicacion;

        public Ejemplar(int id, String titulo, String autorArtista, String ubicacion) {
            this.id = id;
            this.titulo = titulo;
            this.autorArtista = autorArtista;
            this.ubicacion = ubicacion;
        }

        public int getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getAutorArtista() { return autorArtista; }
        public String getUbicacion() { return ubicacion; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        String tipoEjemplar = request.getParameter("tipoEjemplar");

        if ("Cancelar".equals(accion)) {
            response.sendRedirect("alumno.jsp");
            return;
        }

        if (tipoEjemplar == null || tipoEjemplar.isEmpty()) {
            request.setAttribute("mensaje", "Debe seleccionar un tipo de ejemplar.");
            request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
            return;
        }

        // Cuando solo se cambia el tipo de ejemplar
        if (accion == null || !accion.equals("Confirmar Prestamo")) {
            cargarEjemplaresDisponibles(request, tipoEjemplar);
            request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
            request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
            return;
        }

        // Confirmar préstamo
        String ejemplarIdStr = request.getParameter("ejemplarSeleccionado");
        String fechaPrestamoStr = request.getParameter("fechaPrestamo");

        if (ejemplarIdStr == null || fechaPrestamoStr == null) {
            request.setAttribute("mensaje", "Faltan datos para realizar el préstamo.");
            cargarEjemplaresDisponibles(request, tipoEjemplar);
            request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
            request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
            return;
        }

        int ejemplarId;
        try {
            ejemplarId = Integer.parseInt(ejemplarIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("mensaje", "ID de ejemplar inválido.");
            cargarEjemplaresDisponibles(request, tipoEjemplar);
            request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
            request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
            return;
        }

        Date fechaPrestamo;
        try {
            fechaPrestamo = new SimpleDateFormat("yyyy-MM-dd").parse(fechaPrestamoStr);
        } catch (ParseException e) {
            request.setAttribute("mensaje", "Formato de fecha inválido.");
            cargarEjemplaresDisponibles(request, tipoEjemplar);
            request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
            request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
            return;
        }

        Integer idUsuario = (Integer) request.getSession().getAttribute("idUsuario");
        if (idUsuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String mensaje = PrestamoService.realizarPrestamo(idUsuario, ejemplarId, fechaPrestamo, tipoEjemplar);

        request.setAttribute("mensaje", mensaje);
        cargarEjemplaresDisponibles(request, tipoEjemplar);
        request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
        request.getRequestDispatcher("/solicitarPrestamo.jsp").forward(request, response);
    }

    private void cargarEjemplaresDisponibles(HttpServletRequest request, String tipoEjemplar) {
        List<Ejemplar> listaEjemplares = new ArrayList<>();
        String sql = "";

        switch (tipoEjemplar) {
            case "Libro":
                sql = "SELECT id, titulo, autor AS autor_artista, ubicacion_fisica FROM libros WHERE cantidad_total > 0";
                break;
            case "Revista":
                sql = "SELECT id, titulo, editorial AS autor_artista, ubicacion_fisica FROM revistas WHERE cantidad_total > 0";
                break;
            case "Tesis":
                sql = "SELECT id, titulo, autor AS autor_artista, ubicacion_fisica FROM tesis WHERE cantidad_total > 0";
                break;
            case "Obra":
                sql = "SELECT id, titulo, artista AS autor_artista, ubicacion_fisica FROM obras WHERE cantidad_total > 0";
                break;
            case "CD":
                sql = "SELECT id, titulo, artista AS autor_artista, ubicacion_fisica FROM CDs WHERE cantidad_total > 0";
                break;
            default:
                request.setAttribute("mensaje", "Tipo de ejemplar no reconocido.");
                return;
        }

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Ejemplar ejemplar = new Ejemplar(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor_artista"),
                        rs.getString("ubicacion_fisica")
                );
                listaEjemplares.add(ejemplar);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "Error al consultar ejemplares: " + e.getMessage());
        }

        request.setAttribute("listaEjemplares", listaEjemplares);
    }
}
