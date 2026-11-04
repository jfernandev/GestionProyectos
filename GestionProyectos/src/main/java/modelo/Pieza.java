package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "piezas")
public class Pieza {

    // Código alfanumérico que identifica la pieza
    @Id
    @Column(name = "codigo", length = 20)
    private String codigo;

    // Nombre de la pieza
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Precio de la pieza
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Descripción de la pieza
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    // Una pieza puede aparecer en muchas gestiones
    @OneToMany(mappedBy = "pieza")
    private List<Gestion> gestiones = new ArrayList<>();

    // Constructor vacío requerido por Hibernate
    public Pieza() {
    }

    // Constructor para crear piezas desde la aplicación
    public Pieza(String codigo, String nombre, BigDecimal precio, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Gestion> getGestiones() {
        return gestiones;
    }

    public void setGestiones(List<Gestion> gestiones) {
        this.gestiones = gestiones;
    }

    @Override
    public String toString() {
        return "Pieza{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}