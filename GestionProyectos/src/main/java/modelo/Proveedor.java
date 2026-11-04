package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    // Código alfanumérico que identifica al proveedor
    @Id
    @Column(name = "codigo", length = 20)
    private String codigo;

    // Nombre del proveedor
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Apellidos del proveedor
    @Column(name = "apellidos", nullable = false, length = 150)
    private String apellidos;

    // Dirección postal del proveedor
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    // Constructor vacío requerido por Hibernate
    public Proveedor() {
    }

    // Constructor para crear proveedores desde la aplicación
    public Proveedor(String codigo, String nombre, String apellidos, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}