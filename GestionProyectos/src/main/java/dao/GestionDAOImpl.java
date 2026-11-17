
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

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Recuperamos las entidades gestionadas por Hibernate
            gestion.setProveedor(
                    session.find(
                            modelo.Proveedor.class,
                            gestion.getProveedor().getCodigo()
                    )
            );

            gestion.setPieza(
                    session.find(
                            modelo.Pieza.class,
                            gestion.getPieza().getCodigo()
                    )
            );

            gestion.setProyecto(
                    session.find(
                            modelo.Proyecto.class,
                            gestion.getProyecto().getCodigo()
                    )
            );

            // Comprobamos que las tres entidades existen
            if (gestion.getProveedor() == null ||
                    gestion.getPieza() == null ||
                    gestion.getProyecto() == null) {

                throw new RuntimeException(
                        "El proveedor, la pieza o el proyecto indicado no existe."
                );
            }

            // Guardamos la gestión
            session.persist(gestion);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
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

            // Consulta inicial con las relaciones necesarias
            StringBuilder hql = new StringBuilder(
                    "SELECT g FROM Gestion g " +
                            "JOIN FETCH g.proveedor " +
                            "JOIN FETCH g.pieza " +
                            "JOIN FETCH g.proyecto " +
                            "WHERE 1=1 "
            );

            // Añadimos únicamente los filtros que se hayan indicado
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

            // Ordenamos los resultados por fecha descendente
            hql.append("ORDER BY g.fecha DESC, g.id DESC");

            // Creamos la consulta Hibernate
            var consulta = session.createQuery(hql.toString(), Gestion.class);

            // Asignamos los parámetros de los filtros utilizados
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
            throw new RuntimeException("Error al realizar la búsqueda avanzada de gestiones.", e);
        }
    }

    @Override
    public void actualizar(Gestion gestion) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Gestion gestionExistente = session.find(Gestion.class, gestion.getId());

            if (gestionExistente == null) {
                throw new RuntimeException("No existe ninguna gestión con ese identificador.");
            }

            gestion.setProveedor(
                    session.find(
                            modelo.Proveedor.class,
                            gestion.getProveedor().getCodigo()
                    )
            );

            gestion.setPieza(
                    session.find(
                            modelo.Pieza.class,
                            gestion.getPieza().getCodigo()
                    )
            );

            gestion.setProyecto(
                    session.find(
                            modelo.Proyecto.class,
                            gestion.getProyecto().getCodigo()
                    )
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
                throw new RuntimeException("No existe ninguna gestión con ese identificador.");
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
