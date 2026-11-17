
package dao;

import modelo.Gestion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilidades.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class GestionDAOImpl implements GestionDAO {

    @Override
    public void guardar(Gestion gestion) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            gestion.setProveedor(
                    session.find(modelo.Proveedor.class,
                            gestion.getProveedor().getCodigo())
            );

            gestion.setPieza(
                    session.find(modelo.Pieza.class,
                            gestion.getPieza().getCodigo())
            );

            gestion.setProyecto(
                    session.find(modelo.Proyecto.class,
                            gestion.getProyecto().getCodigo())
            );

            if (gestion.getProveedor() == null ||
                    gestion.getPieza() == null ||
                    gestion.getProyecto() == null) {

                throw new RuntimeException(
                        "El proveedor, la pieza o el proyecto indicado no existe."
                );
            }

            session.persist(gestion);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al guardar la gestión.", e);
        }
    }

    @Override
    public Gestion buscarPorId(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Gestion.class, id);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la gestión.", e);
        }
    }

    @Override
    public List<Gestion> listarTodas() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT g FROM Gestion g " +
                            "JOIN FETCH g.proveedor " +
                            "JOIN FETCH g.pieza " +
                            "JOIN FETCH g.proyecto " +
                            "ORDER BY g.fecha DESC, g.id DESC",
                    Gestion.class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar las gestiones.", e);
        }
    }

    @Override
    public List<Gestion> buscarPorProveedor(String codigoProveedor) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT g FROM Gestion g " +
                                    "JOIN FETCH g.proveedor " +
                                    "JOIN FETCH g.pieza " +
                                    "JOIN FETCH g.proyecto " +
                                    "WHERE g.proveedor.codigo = :codigo " +
                                    "ORDER BY g.fecha DESC",
                            Gestion.class
                    )
                    .setParameter("codigo", codigoProveedor)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar gestiones por proveedor.", e);
        }
    }

    @Override
    public List<Gestion> buscarPorPieza(String codigoPieza) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT g FROM Gestion g " +
                                    "JOIN FETCH g.proveedor " +
                                    "JOIN FETCH g.pieza " +
                                    "JOIN FETCH g.proyecto " +
                                    "WHERE g.pieza.codigo = :codigo " +
                                    "ORDER BY g.fecha DESC",
                            Gestion.class
                    )
                    .setParameter("codigo", codigoPieza)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar gestiones por pieza.", e);
        }
    }

    @Override
    public List<Gestion> buscarPorProyecto(String codigoProyecto) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT g FROM Gestion g " +
                                    "JOIN FETCH g.proveedor " +
                                    "JOIN FETCH g.pieza " +
                                    "JOIN FETCH g.proyecto " +
                                    "WHERE g.proyecto.codigo = :codigo " +
                                    "ORDER BY g.fecha DESC",
                            Gestion.class
                    )
                    .setParameter("codigo", codigoProyecto)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar gestiones por proyecto.", e);
        }
    }

    // Búsqueda avanzada con filtros opcionales
    @Override
    public List<Gestion> buscarAvanzado(
            String codigoProveedor,
            String codigoPieza,
            String codigoProyecto,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Integer cantidadMinima,
            Integer cantidadMaxima
    ) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            StringBuilder hql = new StringBuilder(
                    "SELECT g FROM Gestion g " +
                            "JOIN FETCH g.proveedor " +
                            "JOIN FETCH g.pieza " +
                            "JOIN FETCH g.proyecto " +
                            "WHERE 1=1 "
            );

            if (codigoProveedor != null && !codigoProveedor.isBlank()) {
                hql.append("AND g.proveedor.codigo = :codigoProveedor ");
            }

            if (codigoPieza != null && !codigoPieza.isBlank()) {
                hql.append("AND g.pieza.codigo = :codigoPieza ");
            }

            if (codigoProyecto != null && !codigoProyecto.isBlank()) {
                hql.append("AND g.proyecto.codigo = :codigoProyecto ");
            }

            if (fechaDesde != null) {
                hql.append("AND g.fecha >= :fechaDesde ");
            }

            if (fechaHasta != null) {
                hql.append("AND g.fecha <= :fechaHasta ");
            }

            if (cantidadMinima != null) {
                hql.append("AND g.cantidad >= :cantidadMinima ");
            }

            if (cantidadMaxima != null) {
                hql.append("AND g.cantidad <= :cantidadMaxima ");
            }

            hql.append("ORDER BY g.fecha DESC, g.id DESC");

            var consulta = session.createQuery(hql.toString(), Gestion.class);

            if (codigoProveedor != null && !codigoProveedor.isBlank()) {
                consulta.setParameter("codigoProveedor", codigoProveedor);
            }

            if (codigoPieza != null && !codigoPieza.isBlank()) {
                consulta.setParameter("codigoPieza", codigoPieza);
            }

            if (codigoProyecto != null && !codigoProyecto.isBlank()) {
                consulta.setParameter("codigoProyecto", codigoProyecto);
            }

            if (fechaDesde != null) {
                consulta.setParameter("fechaDesde", fechaDesde);
            }

            if (fechaHasta != null) {
                consulta.setParameter("fechaHasta", fechaHasta);
            }

            if (cantidadMinima != null) {
                consulta.setParameter("cantidadMinima", cantidadMinima);
            }

            if (cantidadMaxima != null) {
                consulta.setParameter("cantidadMaxima", cantidadMaxima);
            }

            return consulta.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al realizar la búsqueda avanzada de gestiones.", e
            );
        }
    }

    // Calcula el coste total de los suministros agrupados por proyecto
    @Override
    public List<Object[]> calcularCostesPorProyecto() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT g.proyecto.codigo, " +
                            "g.proyecto.nombre, " +
                            "SUM(g.cantidad * g.pieza.precio) " +
                            "FROM Gestion g " +
                            "JOIN g.pieza " +
                            "JOIN g.proyecto " +
                            "GROUP BY g.proyecto.codigo, g.proyecto.nombre " +
                            "ORDER BY g.proyecto.nombre",
                    Object[].class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al calcular los costes de los proyectos.", e
            );
        }
    }

    // Calcula la cantidad total de unidades suministradas
    @Override
    public Long calcularCantidadTotalSuministrada() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT COALESCE(SUM(g.cantidad), 0) FROM Gestion g",
                    Long.class
            ).getSingleResult();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al calcular la cantidad total suministrada.", e
            );
        }
    }

    // Obtiene los proveedores ordenados por cantidad suministrada
    @Override
    public List<Object[]> obtenerProveedoresMasActivos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT g.proveedor.codigo, " +
                            "g.proveedor.nombre, " +
                            "SUM(g.cantidad) " +
                            "FROM Gestion g " +
                            "JOIN g.proveedor " +
                            "GROUP BY g.proveedor.codigo, g.proveedor.nombre " +
                            "ORDER BY SUM(g.cantidad) DESC",
                    Object[].class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al obtener los proveedores más activos.", e
            );
        }
    }

    // Obtiene las piezas ordenadas por cantidad utilizada
    @Override
    public List<Object[]> obtenerPiezasMasUtilizadas() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT g.pieza.codigo, " +
                            "g.pieza.nombre, " +
                            "SUM(g.cantidad) " +
                            "FROM Gestion g " +
                            "JOIN g.pieza " +
                            "GROUP BY g.pieza.codigo, g.pieza.nombre " +
                            "ORDER BY SUM(g.cantidad) DESC",
                    Object[].class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al obtener las piezas más utilizadas.", e
            );
        }
    }

    @Override
    public void actualizar(Gestion gestion) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Gestion gestionExistente = session.find(Gestion.class, gestion.getId());

            if (gestionExistente == null) {
                throw new RuntimeException(
                        "No existe ninguna gestión con ese identificador."
                );
            }

            gestion.setProveedor(
                    session.find(modelo.Proveedor.class,
                            gestion.getProveedor().getCodigo())
            );

            gestion.setPieza(
                    session.find(modelo.Pieza.class,
                            gestion.getPieza().getCodigo())
            );

            gestion.setProyecto(
                    session.find(modelo.Proyecto.class,
                            gestion.getProyecto().getCodigo())
            );

            if (gestion.getProveedor() == null ||
                    gestion.getPieza() == null ||
                    gestion.getProyecto() == null) {

                throw new RuntimeException(
                        "El proveedor, la pieza o el proyecto indicado no existe."
                );
            }

            gestionExistente.setFecha(gestion.getFecha());
            gestionExistente.setCantidad(gestion.getCantidad());
            gestionExistente.setProveedor(gestion.getProveedor());
            gestionExistente.setPieza(gestion.getPieza());
            gestionExistente.setProyecto(gestion.getProyecto());

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al actualizar la gestión.", e);
        }
    }

    @Override
    public void eliminar(Long id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Gestion gestion = session.find(Gestion.class, id);

            if (gestion == null) {
                throw new RuntimeException(
                        "No existe ninguna gestión con ese identificador."
                );
            }

            session.remove(gestion);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al eliminar la gestión.", e);
        }
    }
}
