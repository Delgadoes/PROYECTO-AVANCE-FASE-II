package main.java.bibliotecaamigosdonbosco;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/eliminarUsuario")
public class EliminarUsuarioServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión y privilegios
        if (!UsuarioSesion.isLoggedIn(request.getSession()) ||
                !"Administrador".equals(UsuarioSesion.getTipoUsuario(request.getSession()))) {
            response.sendRedirect("../login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("message", "ID de usuario no proporcionado");
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("gestionUsuarios.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            try (Connection conn = ConexionBD.getConnection()) {
                // Eliminar el usuario
                String sql = "DELETE FROM usuarios WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    int filasEliminadas = stmt.executeUpdate();

                    if (filasEliminadas > 0) {
                        request.setAttribute("message", "Usuario eliminado exitosamente");
                        request.setAttribute("messageType", "success");
                    } else {
                        request.setAttribute("message", "No se encontró el usuario con ID: " + id);
                        request.setAttribute("messageType", "error");
                    }
                }

                // Reiniciar el contador de autoincremento (opcional)
                try (PreparedStatement stmt = conn.prepareStatement("ALTER TABLE usuarios AUTO_INCREMENT = 1")) {
                    stmt.executeUpdate();
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("message", "ID de usuario inválido");
            request.setAttribute("messageType", "error");
        } catch (SQLException e) {
            request.setAttribute("message", "Error al eliminar usuario: " + e.getMessage());
            request.setAttribute("messageType", "error");
        }

        response.sendRedirect("gestionUsuarios.jsp");
    }
}