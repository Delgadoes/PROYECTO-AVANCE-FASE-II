package main.java.bibliotecaamigosdonbosco;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsuarioSesion {
    public static int getIdUsuario(HttpSession session) {
        return (int) session.getAttribute("idUsuario");
    }

    public static String getTipoUsuario(HttpSession session) {
        return (String) session.getAttribute("tipoUsuario");
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("idUsuario") != null;
    }

    public static void verificarSesion(HttpSession session, String tipoRequerido, HttpServletResponse response)
            throws IOException {
        if (!isLoggedIn(session) || !getTipoUsuario(session).equals(tipoRequerido)) {
            response.sendRedirect("../login.jsp");
        }
    }
}