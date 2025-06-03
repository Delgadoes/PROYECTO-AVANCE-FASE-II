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
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usuario = request.getParameter("usuario");
        String contraseña = request.getParameter("contraseña");
        String tipoUsuario = request.getParameter("tipoUsuario");

        // Depuración
        System.out.println("Datos recibidos:");
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contraseña);
        System.out.println("Tipo Usuario: " + tipoUsuario);

        // Validación de campos vacíos
        if (usuario == null || usuario.isEmpty() ||
                contraseña == null || contraseña.isEmpty() ||
                tipoUsuario == null || tipoUsuario.isEmpty()) {

            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND privilegio = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usuario);
                stmt.setString(2, tipoUsuario);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String contraseñaDB = rs.getString("contraseña");
                        if (contraseña.equals(contraseñaDB)) {
                            int idUsuario = rs.getInt("id");
                            String nombreUsuario = rs.getString("nombre");
                            

                            // Crear sesión y almacenar datos del usuario
                            HttpSession session = request.getSession();
                            session.setAttribute("idUsuario", idUsuario);
                            session.setAttribute("tipoUsuario", tipoUsuario);
                            session.setAttribute("nombreUsuario", nombreUsuario);
                            
                            // Redirigir según el tipo de usuario
                            switch (tipoUsuario) {
                                case "Administrador":
                                    response.sendRedirect("VentanaAdministrador.jsp");
                                    break;
                                case "Profesor":
                                    response.sendRedirect("profesor/inicio.jsp");
                                    break;
                                case "Alumno":
                                    response.sendRedirect("alumno.jsp");
                                    break;
                                default:
                                    request.setAttribute("error", "Tipo de usuario no reconocido.");
                                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                            }
                        } else {
                            request.setAttribute("error", "Contraseña incorrecta.");
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                        }
                    } else {
                        request.setAttribute("error", "Usuario no encontrado o privilegio incorrecto.");
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                    }
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error al validar el usuario: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}