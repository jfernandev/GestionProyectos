package modelo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "gestiones")
public class Gestion {

    // Identificador único generado automáticamente por MariaDB
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha en la que se realiza el suministro
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Número de unidades suministradas
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    // Cada gestión pertenece a un único proveedor
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proveedor_codigo", nullable = false)
    private Proveedor proveedor;

    // Cada gestión corresponde a una única pieza
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pieza_codigo", nullable = false)
    private Pieza pieza;

    // Cada gestión pertenece a un único proyecto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proyecto_codigo", nullable = false)
    private Proyecto proyecto;

    // Constructor vacío requerido por Hibernate
    public Gestion() {
    }

    // Constructor para crear una gestión
    public Gestion(LocalDate fecha, Integer cantidad,
                   Proveedor proveedor, Pieza pieza, Proyecto proyecto) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
        this.pieza = pieza;
        this.proyecto = proyecto;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public String toString() {
        return "Gestion{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", cantidad=" + cantidad +
                ", proveedor=" + proveedor.getCodigo() +
                ", pieza=" + pieza.getCodigo() +
                ", proyecto=" + proyecto.getCodigo() +
                '}';
    }
}