package main.java.bibliotecaamigosdonbosco;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DevolucionServlet")
public class DevolucionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idUsuario = UsuarioSesion.getIdUsuario(session);

        String accion = request.getParameter("accion");
        String tipoEjemplar = request.getParameter("tipoEjemplar");

        if ("cancelar".equals(accion)) {
            response.sendRedirect("alumno.jsp");
            return;
        }

        if (tipoEjemplar != null && !"confirmar".equals(accion)) {
            // Cargar préstamos pendientes para el tipo seleccionado
            List<Prestamo> prestamos = cargarPrestamosPendientes(tipoEjemplar, idUsuario);
            request.setAttribute("prestamosPendientes", prestamos);
            request.setAttribute("tipoEjemplar", tipoEjemplar);
            if (prestamos.isEmpty()) {
                request.setAttribute("mensaje", "No tienes préstamos pendientes para este tipo de ejemplar.");
            }
            request.getRequestDispatcher("devolucion.jsp").forward(request, response);
            return;
        }

        if ("confirmar".equals(accion)) {
            String prestamoSeleccionado = request.getParameter("prestamoSeleccionado");
            String fechaDevStr = request.getParameter("fechaDevolucion");

            if (prestamoSeleccionado == null || prestamoSeleccionado.isEmpty() || fechaDevStr == null || fechaDevStr.isEmpty()) {
                request.setAttribute("mensaje", "Debe seleccionar un préstamo y una fecha de devolución válidos.");
                request.setAttribute("tipoEjemplar", tipoEjemplar);
                request.getRequestDispatcher("devolucion.jsp").forward(request, response);
                return;
            }

            int prestamoId;
            try {
                prestamoId = Integer.parseInt(prestamoSeleccionado);
            } catch (NumberFormatException e) {
                request.setAttribute("mensaje", "El préstamo seleccionado no es válido.");
                request.setAttribute("tipoEjemplar", tipoEjemplar);
                request.getRequestDispatcher("devolucion.jsp").forward(request, response);
                return;
            }

            Date fechaDevolucion;
            try {
                fechaDevolucion = Date.valueOf(fechaDevStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("mensaje", "La fecha de devolución no es válida.");
                request.setAttribute("tipoEjemplar", tipoEjemplar);
                request.getRequestDispatcher("devolucion.jsp").forward(request, response);
                return;
            }

            try (Connection conn = ConexionBD.getConnection()) {
                String sqlVerificar = "SELECT fecha_prestamo FROM prestamos WHERE id = ? AND fecha_devolucion IS NULL";
                try (PreparedStatement pst = conn.prepareStatement(sqlVerificar)) {
                    pst.setInt(1, prestamoId);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        Date fechaPrestamo = rs.getDate("fecha_prestamo");
                        int plazoMaximo = obtenerPlazoMaximo();
                        double mora = calcularMora(fechaPrestamo, fechaDevolucion, plazoMaximo);

                        String sqlUpdate = "UPDATE prestamos SET fecha_devolucion = ?, mora = ? WHERE id = ?";
                        try (PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate)) {
                            pstUpdate.setDate(1, fechaDevolucion);
                            pstUpdate.setDouble(2, mora);
                            pstUpdate.setInt(3, prestamoId);
                            pstUpdate.executeUpdate();
                        }

                        actualizarCantidadPrestados(conn, prestamoId, tipoEjemplar);

                        request.setAttribute("mensaje", "Devolución registrada con éxito. Mora: $" + mora);
                    } else {
                        request.setAttribute("mensaje", "El préstamo seleccionado no existe o ya fue devuelto.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("mensaje", "Error al procesar la devolución: " + e.getMessage());
            }

            // Recargar lista para seguir mostrando al usuario
            List<Prestamo> prestamos = cargarPrestamosPendientes(tipoEjemplar, idUsuario);
            request.setAttribute("prestamosPendientes", prestamos);
            request.setAttribute("tipoEjemplar", tipoEjemplar);
            request.getRequestDispatcher("devolucion.jsp").forward(request, response);
        }
    }

    private List<Prestamo> cargarPrestamosPendientes(String tipo, int usuarioId) {
        List<Prestamo> lista = new ArrayList<>();
        String tabla = "";

        switch (tipo) {
            case "Libro":
                tabla = "libros";
                break;
            case "Revista":
                tabla = "revistas";
                break;
            case "Tesis":
                tabla = "tesis";
                break;
            case "Obra":
                tabla = "obras";
                break;
            case "CD":
                tabla = "CDs";
                break;
            default:
                tabla = "";
                break;
        }

        if (tabla.isEmpty()) {
            return lista;
        }

        String sql = "SELECT p.id, e.titulo " +
                     "FROM prestamos p " +
                     "JOIN " + tabla + " e ON p.id_ejemplar = e.id " +
                     "WHERE p.fecha_devolucion IS NULL AND p.id_usuario = ? AND p.tipo_ejemplar = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, usuarioId);
            pst.setString(2, tipo);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(new Prestamo(rs.getInt("id"), rs.getString("titulo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private int obtenerPlazoMaximo() {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT dias_antes_mora FROM configuracion_sistema ORDER BY id DESC LIMIT 1")) {
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("dias_antes_mora");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 30; // default
    }

    private double obtenerTasaMora() {
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT mora_diaria FROM configuracion_mora WHERE anio = ?")) {
            pst.setInt(1, anioActual);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getDouble("mora_diaria");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private double calcularMora(Date fechaPrestamo, Date fechaDevolucion, int plazo) {
        long limite = fechaPrestamo.toLocalDate().plusDays(plazo).toEpochDay();
        long dev = fechaDevolucion.toLocalDate().toEpochDay();
        long diasRetraso = dev - limite;
        return diasRetraso > 0 ? diasRetraso * obtenerTasaMora() : 0;
    }

    private void actualizarCantidadPrestados(Connection conn, int prestamoId, String tipoEjemplar) throws SQLException {
        String tabla = "";

        switch (tipoEjemplar) {
            case "Libro":
                tabla = "libros";
                break;
            case "Revista":
                tabla = "revistas";
                break;
            case "Tesis":
                tabla = "tesis";
                break;
            case "Obra":
                tabla = "obras";
                break;
            case "CD":
                tabla = "CDs";
                break;
            default:
                tabla = "";
                break;
        }

        if (tabla.isEmpty()) {
            return;
        }

        // Aquí asumo que al devolver solo disminuye la cantidad prestados
        String sqlUpdate = "UPDATE " + tabla + 
                       " SET cantidad_prestados = cantidad_prestados - 1, " +
                       "cantidad_total = cantidad_total + 1 " +
                       "WHERE id IN (SELECT id_ejemplar FROM prestamos WHERE id = ?)";

        try (PreparedStatement pst = conn.prepareStatement(sqlUpdate)) {
            pst.setInt(1, prestamoId);
            pst.executeUpdate();
        }
    }
}
