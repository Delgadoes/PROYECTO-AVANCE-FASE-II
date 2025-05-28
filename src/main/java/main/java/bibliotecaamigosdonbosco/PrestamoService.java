package main.java.bibliotecaamigosdonbosco;

import java.sql.*;
import java.util.Date;

public class PrestamoService {

    // Ejemplo simplificado: verifica mora, disponibilidad y límites
    public static String realizarPrestamo(int idUsuario, int ejemplarId, Date fechaPrestamo, String tipoEjemplar) {
        try (Connection conn = ConexionBD.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1. Verificar mora pendiente
              if (tieneMora(idUsuario)) {
               conn.rollback();
              return "No se puede realizar el préstamo. El usuario tiene mora pendiente.";
                  }

                // 2. Verificar ejemplar disponible
                if (!hayEjemplarDisponible(conn, ejemplarId, tipoEjemplar)) {
                    conn.rollback();
                    return "No hay ejemplares disponibles.";
                }

                // 3. Verificar límite de préstamos activos
               if (contarPrestamosActivos(conn, idUsuario) >= obtenerLimitePrestamos(idUsuario)) {

                    conn.rollback();
                    return "Ha alcanzado el límite de préstamos.";
                }
               
               // 3.5. Verificar si ya tiene un préstamo activo de este ejemplar
              if (yaTienePrestamoActivo(conn, idUsuario, ejemplarId)) {
              conn.rollback();
                      return "Ya tiene un préstamo activo de este ejemplar.";
                     }

                // 4. Insertar préstamo
                String insertSql = "INSERT INTO prestamos (id_ejemplar, fecha_prestamo, tipo_ejemplar, id_usuario) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, ejemplarId);
                    ps.setDate(2, new java.sql.Date(fechaPrestamo.getTime()));
                    ps.setString(3, tipoEjemplar);
                    ps.setInt(4, idUsuario);
                    ps.executeUpdate();
                }

                // 5. Actualizar disponibilidad del ejemplar y validar que se actualizó
                if (!actualizarCantidadEjemplar(conn, ejemplarId, tipoEjemplar)) {
                    conn.rollback();
                    return "No se pudo actualizar la cantidad del ejemplar (quizá ya no está disponible).";
                }

                conn.commit();
                return "Préstamo realizado con éxito.";

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return "Error al realizar el préstamo: " + e.getMessage();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al conectar con la base de datos: " + e.getMessage();
        }
    }

private static  boolean tieneMora(int idUsuario) {
    String diasMoraQuery = "SELECT dias_antes_mora FROM configuracion_sistema ORDER BY id DESC LIMIT 1";
    int diasAntesMora = 30;  // Valor predeterminado en caso de error

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement psDias = conn.prepareStatement(diasMoraQuery);
         ResultSet rsDias = psDias.executeQuery()) {

        if (rsDias.next()) {
            diasAntesMora = rsDias.getInt("dias_antes_mora");
        }

        String moraQuery = "SELECT COUNT(*) AS mora FROM prestamos WHERE id_usuario = ? AND fecha_devolucion IS NULL AND fecha_prestamo < DATE_SUB(CURDATE(), INTERVAL ? DAY)";
        
        try (PreparedStatement psMora = conn.prepareStatement(moraQuery)) {
            psMora.setInt(1, idUsuario);
            psMora.setInt(2, diasAntesMora);

            try (ResultSet rsMora = psMora.executeQuery()) {
                if (rsMora.next()) {
                    return rsMora.getInt("mora") > 0;
                }
            }
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}


    private static boolean hayEjemplarDisponible(Connection conn, int ejemplarId, String tipoEjemplar) throws SQLException {
        String sql = "";
        switch (tipoEjemplar) {
            case "Libro":
                sql = "SELECT cantidad_total FROM libros WHERE id = ?";
                break;
            case "Revista":
                sql = "SELECT cantidad_total FROM revistas WHERE id = ?";
                break;
            case "Tesis":
                sql = "SELECT cantidad_total FROM tesis WHERE id = ?";
                break;
            case "Obra":
                sql = "SELECT cantidad_total FROM obras WHERE id = ?";
                break;
            case "CD":
                sql = "SELECT cantidad_total FROM CDs WHERE id = ?";
                break;
            default:
                return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ejemplarId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cantidad_total") > 0;
                }
            }
        }
        return false;
    }

private static int contarPrestamosActivos(Connection conn, int idUsuario) throws SQLException {
    String sql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND fecha_devolucion IS NULL";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idUsuario);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    return 0;
}


private static int obtenerLimitePrestamos(int idUsuario) {
    int limitePrestamos = 2; // Valor predeterminado en caso de error

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT limite_prestamos FROM configuracion_sistema ORDER BY id DESC LIMIT 1")) {

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                limitePrestamos = rs.getInt("limite_prestamos");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return limitePrestamos;
}

private static boolean yaTienePrestamoActivo(Connection conn, int idUsuario, int ejemplarId) throws SQLException {
    String sql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND id_ejemplar = ? AND fecha_devolucion IS NULL";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idUsuario);
        ps.setInt(2, ejemplarId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    }
    return false;
}





    private static boolean actualizarCantidadEjemplar(Connection conn, int ejemplarId, String tipoEjemplar) throws SQLException {
        String sql = "";
        switch (tipoEjemplar) {
            case "Libro":
                sql = "UPDATE libros SET cantidad_total = cantidad_total - 1, cantidad_prestados = cantidad_prestados + 1 WHERE id = ?";
                break;
            case "Revista":
                sql = "UPDATE revistas SET cantidad_total = cantidad_total - 1, cantidad_prestados = cantidad_prestados + 1 WHERE id = ?";
                break;
            case "Tesis":
                sql = "UPDATE tesis SET cantidad_total = cantidad_total - 1, cantidad_prestados = cantidad_prestados + 1 WHERE id = ?";
                break;
            case "Obra":
                sql = "UPDATE obras SET cantidad_total = cantidad_total - 1, cantidad_prestados = cantidad_prestados + 1 WHERE id = ?";
                break;
            case "CD":
                sql = "UPDATE cds SET cantidad_total = cantidad_total - 1, cantidad_prestados = cantidad_prestados + 1 WHERE id = ?";
                break;
            default:
                return false;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ejemplarId);
            int filasActualizadas = ps.executeUpdate();
            return filasActualizadas > 0;
        }
    }
}
