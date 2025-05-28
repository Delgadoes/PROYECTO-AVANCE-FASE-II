
package main.java.bibliotecaamigosdonbosco;


public class Prestamo {
    
    private int id;
    private String titulo;

    public Prestamo(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    
}
