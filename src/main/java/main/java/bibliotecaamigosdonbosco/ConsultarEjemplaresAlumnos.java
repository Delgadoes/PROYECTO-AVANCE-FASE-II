package main.java.bibliotecaamigosdonbosco;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/consultarEjemplares")
public class ConsultarEjemplaresAlumnos extends HttpServlet {

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

        // Getters
        public int getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getAutorArtista() { return autorArtista; }
        public String getUbicacion() { return ubicacion; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tipoEjemplar = request.getParameter("tipoEjemplar");
        String buscar = request.getParameter("buscar");

        if (tipoEjemplar == null) tipoEjemplar = "";
        if (buscar == null) buscar = "";

        List<Ejemplar> listaEjemplares = new ArrayList<>();

        String sql = "";

        switch (tipoEjemplar) {
            case "Libro":
                sql = "SELECT id, titulo, autor AS autor_artista, ubicacion_fisica FROM libros WHERE titulo LIKE ?";
                break;
            case "Revista":
                sql = "SELECT id, titulo, editorial AS autor_artista, ubicacion_fisica FROM revistas WHERE titulo LIKE ?";
                break;
            case "Tesis":
                sql = "SELECT id, titulo, autor AS autor_artista, ubicacion_fisica FROM tesis WHERE titulo LIKE ?";
                break;
            case "Obra":
                sql = "SELECT id, titulo, artista AS autor_artista, ubicacion_fisica FROM obras WHERE titulo LIKE ?";
                break;
            case "CD":
                sql = "SELECT id, titulo, artista AS autor_artista, ubicacion_fisica FROM CDs WHERE titulo LIKE ?";
                break;
            default:
                // Sin tipo válido, no hacer consulta
                break;
        }

        if (!sql.isEmpty()) {
            try (Connection conn = ConexionBD.getConnection();
                 PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setString(1, "%" + buscar + "%");
                ResultSet rs = pst.executeQuery();

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
                request.setAttribute("error", "Error al consultar ejemplares: " + e.getMessage());
            }
        }

        request.setAttribute("listaEjemplares", listaEjemplares);
        request.setAttribute("tipoEjemplarSeleccionado", tipoEjemplar);
        request.setAttribute("buscarTexto", buscar);
        request.getRequestDispatcher("/consultarEjemplares.jsp").forward(request, response);
    }
}
