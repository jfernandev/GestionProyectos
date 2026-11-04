package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proyectos")
public class Proyecto {

    // Código alfanumérico que identifica al proyecto
    @Id
    @Column(name = "codigo", length = 20)
    private String codigo;

    // Nombre del proyecto
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Ciudad donde se desarrolla el proyecto
    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;

    // Constructor vacío requerido por Hibernate
    public Proyecto() {
    }

    // Constructor para crear proyectos desde la aplicación
    public Proyecto(String codigo, String nombre, String ciudad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}