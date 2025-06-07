package main.java.bibliotecaamigosdonbosco;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ConfiguracionMoraServlet")
public class ConfiguracionMoraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Cargar configuración actual
            cargarConfiguracionSistema(request);

            // Cargar configuraciones de mora por año
            List<ConfiguracionMora> configuraciones = cargarConfiguracionesMora();
            request.setAttribute("configuraciones", configuraciones);

            request.getRequestDispatcher("/configuracionMora.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("mensajeError", "Error al cargar configuraciones: " + e.getMessage());
            request.getRequestDispatcher("/configuracionMora.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("guardarMora".equals(accion)) {
            guardarConfiguracionMora(request, response);
        } else if ("guardarSistema".equals(accion)) {
            guardarConfiguracionSistema(request, response);
        } else {
            response.sendRedirect("ConfiguracionMoraServlet");
        }
    }

    private void cargarConfiguracionSistema(HttpServletRequest request) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT dias_antes_mora, limite_prestamos FROM configuracion_sistema ORDER BY id DESC LIMIT 1");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                request.setAttribute("diasAntesMora", rs.getInt("dias_antes_mora"));
                request.setAttribute("limitePrestamos", rs.getInt("limite_prestamos"));
            }
        }
    }

    private List<ConfiguracionMora> cargarConfiguracionesMora() throws SQLException {
        List<ConfiguracionMora> configuraciones = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT anio, mora_diaria FROM configuracion_mora");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                configuraciones.add(new ConfiguracionMora(
                        rs.getString("anio"),
                        rs.getDouble("mora_diaria")
                ));
            }
        }

        return configuraciones;
    }

    private void guardarConfiguracionMora(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String anio = request.getParameter("anio");
        String moraDiariaStr = request.getParameter("moraDiaria");

        if (anio == null || moraDiariaStr == null || anio.isEmpty() || moraDiariaStr.isEmpty()) {
            request.setAttribute("mensajeError", "Por favor, complete todos los campos.");
            doGet(request, response);
            return;
        }

        try {
            double moraDiaria = Double.parseDouble(moraDiariaStr);

            try (Connection conn = ConexionBD.getConnection()) {
                // Verificar si el año ya existe
                String verificarSql = "SELECT COUNT(*) FROM configuracion_mora WHERE anio = ?";
                try (PreparedStatement verificarStmt = conn.prepareStatement(verificarSql)) {
                    verificarStmt.setString(1, anio);
                    try (ResultSet rs = verificarStmt.executeQuery()) {
                        rs.next();
                        int existe = rs.getInt(1);

                        // Insertar o actualizar según el caso
                        if (existe > 0) {
                            String updateSql = "UPDATE configuracion_mora SET mora_diaria = ? WHERE anio = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                updateStmt.setDouble(1, moraDiaria);
                                updateStmt.setString(2, anio);
                                updateStmt.executeUpdate();
                                request.setAttribute("mensajeExito", "Configuración de mora actualizada exitosamente.");
                            }
                        } else {
                            String insertSql = "INSERT INTO configuracion_mora (anio, mora_diaria) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                insertStmt.setString(1, anio);
                                insertStmt.setDouble(2, moraDiaria);
                                insertStmt.executeUpdate();
                                request.setAttribute("mensajeExito", "Configuración de mora guardada exitosamente.");
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("mensajeError", "El valor de la mora diaria debe ser numérico.");
        } catch (SQLException e) {
            request.setAttribute("mensajeError", "Error al guardar la configuración: " + e.getMessage());
        }

        doGet(request, response);
    }

    private void guardarConfiguracionSistema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String diasAntesMoraStr = request.getParameter("diasAntesMora");
        String limitePrestamosStr = request.getParameter("limitePrestamos");

        try {
            int diasAntesMora = Integer.parseInt(diasAntesMoraStr);
            int limitePrestamos = Integer.parseInt(limitePrestamosStr);

            if (diasAntesMora <= 0 || limitePrestamos <= 0) {
                request.setAttribute("mensajeError", "Los valores deben ser números positivos.");
                doGet(request, response);
                return;
            }

            try (Connection conn = ConexionBD.getConnection()) {
                String sql = "UPDATE configuracion_sistema SET dias_antes_mora = ?, limite_prestamos = ? WHERE id = 1";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, diasAntesMora);
                    ps.setInt(2, limitePrestamos);

                    int filasAfectadas = ps.executeUpdate();

                    if (filasAfectadas > 0) {
                        request.setAttribute("mensajeExito", "Configuración del sistema actualizada correctamente.");
                    } else {
                        request.setAttribute("mensajeError", "No se encontró la configuración del sistema.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("mensajeError", "Por favor, ingrese números válidos.");
        } catch (SQLException e) {
            request.setAttribute("mensajeError", "Error al actualizar la configuración: " + e.getMessage());
        }

        doGet(request, response);
    }

    public static class ConfiguracionMora {
        private String anio;
        private double moraDiaria;

        public ConfiguracionMora(String anio, double moraDiaria) {
            this.anio = anio;
            this.moraDiaria = moraDiaria;
        }

        public String getAnio() { return anio; }
        public double getMoraDiaria() { return moraDiaria; }
    }
}