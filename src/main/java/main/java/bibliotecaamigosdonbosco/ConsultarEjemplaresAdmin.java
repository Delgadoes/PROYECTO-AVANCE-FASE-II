package main.java.bibliotecaamigosdonbosco;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/consultarEjemplares")
public class ConsultarEjemplaresAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipoEjemplar = request.getParameter("tipoEjemplar");
        String buscar = request.getParameter("buscar");

        if (tipoEjemplar == null) tipoEjemplar = "";
        if (buscar == null) buscar = "";

        List<EjemplarAdmin> listaEjemplares = new ArrayList<>();
        String sql = construirConsulta(tipoEjemplar);

        if (!sql.isEmpty()) {
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, "%" + buscar + "%");
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    EjemplarAdmin ejemplar = new EjemplarAdmin(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor_artista"),
                            rs.getString("ubicacion_fisica"),
                            rs.getInt("cantidad_total"),
                            rs.getInt("cantidad_prestados"),
                            tipoEjemplar
                    );
                    listaEjemplares.add(ejemplar);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "Error al consultar ejemplares: " + e.getMessage());
            }
        }

        request.setAttribute("listaEjemplares", listaEjemplares);
        request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
        request.setAttribute("buscarTexto", buscar);
        request.getRequestDispatcher("/consultarEjemplaresadmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int idEjemplar = Integer.parseInt(request.getParameter("id"));
        String tipoEjemplar = request.getParameter("tipoEjemplar");

        try {
            if (eliminarEjemplar(idEjemplar, tipoEjemplar)) {
                request.setAttribute("mensaje", "Ejemplar eliminado correctamente");
            } else {
                request.setAttribute("error", "No se puede eliminar: tiene préstamos activos");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        // Redirigir de vuelta al GET para mostrar resultados actualizados
        response.sendRedirect(request.getContextPath() + "/admin/consultarEjemplares?tipoEjemplar=" + tipoEjemplar);
    }

    private String construirConsulta(String tipo) {
        switch (tipo) {
            case "Libro":
                return "SELECT l.id, l.titulo, l.autor AS autor_artista, l.ubicacion_fisica, l.cantidad_total, " +
                        "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = l.id AND p.tipo_ejemplar = 'Libro' AND p.fecha_devolucion IS NULL) AS cantidad_prestados " +
                        "FROM libros l WHERE l.titulo LIKE ?";
            case "Revista":
                return "SELECT r.id, r.titulo, r.editorial AS autor_artista, r.ubicacion_fisica, r.cantidad_total, " +
                        "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = r.id AND p.tipo_ejemplar = 'Revista' AND p.fecha_devolucion IS NULL) AS cantidad_prestados " +
                        "FROM revistas r WHERE r.titulo LIKE ?";
            case "Tesis":
                return "SELECT t.id, t.titulo, t.autor AS autor_artista, t.ubicacion_fisica, t.cantidad_total, " +
                        "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = t.id AND p.tipo_ejemplar = 'Tesis' AND p.fecha_devolucion IS NULL) AS cantidad_prestados " +
                        "FROM tesis t WHERE t.titulo LIKE ?";
            case "Obra":
                return "SELECT o.id, o.titulo, o.artista AS autor_artista, o.ubicacion_fisica, o.cantidad_total, " +
                        "(SELECT COUNT(*) FROM prestamos p WHERE p.id_ejemplar = o.id AND p.tipo_ejemplar = 'Obra' AND p.fecha_devolucion IS NULL) AS cantidad_prestados " +
                        "FROM obras o WHERE o.titulo LIKE ?";
            case "CD":
                return "SELECT c.id, c.titulo, c.artista AS autor_artista, c.ubicacion_fisica, c.cantidad_total, c.cantidad_prestados " +
                        "FROM CDs c WHERE c.titulo LIKE ?";
            default:
                return "";
        }
    }

    private boolean eliminarEjemplar(int id, String tipo) throws SQLException {
        // Verificar préstamos activos primero
        if (tienePrestamosActivos(id, tipo)) {
            return false;
        }

        String sql = "";
        switch (tipo) {
            case "Libro": sql = "DELETE FROM libros WHERE id = ?"; break;
            case "Revista": sql = "DELETE FROM revistas WHERE id = ?"; break;
            case "Tesis": sql = "DELETE FROM tesis WHERE id = ?"; break;
            case "Obra": sql = "DELETE FROM obras WHERE id = ?"; break;
            case "CD": sql = "DELETE FROM CDs WHERE id = ?"; break;
            default: return false;
        }

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        }
    }

    private boolean tienePrestamosActivos(int id, String tipo) throws SQLException {
        String sql = "SELECT COUNT(*) AS prestamos_activos FROM prestamos " +
                "WHERE id_ejemplar = ? AND tipo_ejemplar = ? AND fecha_devolucion IS NULL";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setString(2, tipo);
            ResultSet rs = pst.executeQuery();

            return rs.next() && rs.getInt("prestamos_activos") > 0;
        }
    }
}