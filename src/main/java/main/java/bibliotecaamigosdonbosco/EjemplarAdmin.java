package main.java.bibliotecaamigosdonbosco;

public class EjemplarAdmin {
    private int id;
    private String titulo;
    private String autorArtista;
    private String ubicacion;
    private int cantidadTotal;
    private int cantidadPrestados;
    private String tipo;

    public EjemplarAdmin(int id, String titulo, String autorArtista, String ubicacion,
                         int cantidadTotal, int cantidadPrestados, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.autorArtista = autorArtista;
        this.ubicacion = ubicacion;
        this.cantidadTotal = cantidadTotal;
        this.cantidadPrestados = cantidadPrestados;
        this.tipo = tipo;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutorArtista() { return autorArtista; }
    public String getUbicacion() { return ubicacion; }
    public int getCantidadTotal() { return cantidadTotal; }
    public int getCantidadPrestados() { return cantidadPrestados; }
    public String getTipo() { return tipo; }
}