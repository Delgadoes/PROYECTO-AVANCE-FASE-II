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

@WebServlet("/agregarUsuario")
public class AgregarUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión y privilegios
        if (!UsuarioSesion.isLoggedIn(request.getSession()) ||
                !"Administrador".equals(UsuarioSesion.getTipoUsuario(request.getSession()))) {
            response.sendRedirect("../login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String nombre = request.getParameter("nombre");
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String privilegio = request.getParameter("privilegio");

        // Validar campos
        if (nombre == null || nombre.isEmpty() ||
                usuario == null || usuario.isEmpty() ||
                contrasena == null || contrasena.isEmpty() ||
                privilegio == null || privilegio.isEmpty()) {

            request.setAttribute("error", "Todos los campos son obligatorios");
            request.getRequestDispatcher("agregarUsuario.jsp").forward(request, response);
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {
            // Verificar si el usuario ya existe
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, usuario);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        request.setAttribute("error", "El nombre de usuario ya existe");
                        request.getRequestDispatcher("agregarUsuario.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // Insertar nuevo usuario
            String insertSql = "INSERT INTO usuarios (nombre, usuario, contraseña, privilegio) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, nombre);
                insertStmt.setString(2, usuario);
                insertStmt.setString(3, contrasena);
                insertStmt.setString(4, privilegio);

                int filasInsertadas = insertStmt.executeUpdate();
                if (filasInsertadas > 0) {
                    request.setAttribute("message", "Usuario agregado exitosamente");
                    request.setAttribute("messageType", "success");
                    response.sendRedirect("gestionUsuarios.jsp");
                    return;
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error al agregar usuario: " + e.getMessage());
            request.getRequestDispatcher("agregarUsuario.jsp").forward(request, response);
            return;
        }

        request.setAttribute("error", "No se pudo agregar el usuario");
        request.getRequestDispatcher("agregarUsuario.jsp").forward(request, response);
    }
}