package main.java.bibliotecaamigosdonbosco;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/actualizarUsuario")
public class ActualizarUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión y privilegios
        if (!UsuarioSesion.isLoggedIn(request.getSession()) ||
                !"Administrador".equals(UsuarioSesion.getTipoUsuario(request.getSession()))) {
            response.sendRedirect("../login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String privilegio = request.getParameter("privilegio");

        // Validar campos
        if (idStr == null || idStr.isEmpty() ||
                nombre == null || nombre.isEmpty() ||
                usuario == null || usuario.isEmpty() ||
                privilegio == null || privilegio.isEmpty()) {

            request.setAttribute("error", "Todos los campos obligatorios deben estar completos");
            request.getRequestDispatcher("editarUsuario.jsp?id=" + idStr).forward(request, response);
            return;
        }

        int id = Integer.parseInt(idStr);

        try (Connection conn = ConexionBD.getConnection()) {
            // Verificar si el nuevo nombre de usuario ya existe (excluyendo el usuario actual)
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ? AND id != ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, usuario);
                checkStmt.setInt(2, id);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        request.setAttribute("error", "El nombre de usuario ya está en uso");
                        request.getRequestDispatcher("editarUsuario.jsp?id=" + idStr).forward(request, response);
                        return;
                    }
                }
            }

            // Actualizar usuario (con o sin contraseña)
            String updateSql;
            if (contrasena != null && !contrasena.isEmpty()) {
                updateSql = "UPDATE usuarios SET nombre = ?, usuario = ?, contraseña = ?, privilegio = ? WHERE id = ?";
            } else {
                updateSql = "UPDATE usuarios SET nombre = ?, usuario = ?, privilegio = ? WHERE id = ?";
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                if (contrasena != null && !contrasena.isEmpty()) {
                    updateStmt.setString(1, nombre);
                    updateStmt.setString(2, usuario);
                    updateStmt.setString(3, contrasena);
                    updateStmt.setString(4, privilegio);
                    updateStmt.setInt(5, id);
                } else {
                    updateStmt.setString(1, nombre);
                    updateStmt.setString(2, usuario);
                    updateStmt.setString(3, privilegio);
                    updateStmt.setInt(4, id);
                }

                int filasActualizadas = updateStmt.executeUpdate();
                if (filasActualizadas > 0) {
                    request.setAttribute("message", "Usuario actualizado exitosamente");
                    request.setAttribute("messageType", "success");
                    response.sendRedirect("gestionUsuarios.jsp");
                    return;
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error al actualizar usuario: " + e.getMessage());
            request.getRequestDispatcher("editarUsuario.jsp?id=" + idStr).forward(request, response);
            return;
        }

        request.setAttribute("error", "No se pudo actualizar el usuario");
        request.getRequestDispatcher("editarUsuario.jsp?id=" + idStr).forward(request, response);
    }
}