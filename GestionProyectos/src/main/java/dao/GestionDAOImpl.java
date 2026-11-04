
package dao;

import modelo.Gestion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilidades.HibernateUtil;

import java.util.List;

public class GestionDAOImpl implements GestionDAO {

    @Override
    public void guardar(Gestion gestion) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Obtenemos las entidades gestionadas por Hibernate
            // para asegurar que las relaciones están asociadas a esta sesión
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

            // Buscamos la gestión mediante su identificador
            return session.find(Gestion.class, id);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la gestión.", e);
        }
    }

    @Override
    public List<Gestion> listarTodas() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Recuperamos todas las gestiones con sus relaciones
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

            // Recuperamos las gestiones de un proveedor concreto
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

            // Recuperamos las gestiones correspondientes a una pieza
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

            // Recuperamos las gestiones correspondientes a un proyecto
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

    @Override
    public void actualizar(Gestion gestion) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Comprobamos que la gestión existe
            Gestion gestionExistente = session.find(Gestion.class, gestion.getId());

            if (gestionExistente == null) {
                throw new RuntimeException("No existe ninguna gestión con ese identificador.");
            }

            // Recuperamos las entidades relacionadas dentro de esta sesión
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

            // Comprobamos que las relaciones existen
            if (gestion.getProveedor() == null ||
                    gestion.getPieza() == null ||
                    gestion.getProyecto() == null) {

                throw new RuntimeException(
                        "El proveedor, la pieza o el proyecto indicado no existe."
                );
            }

            // Actualizamos los datos de la gestión
            gestionExistente.setFecha(gestion.getFecha());
            gestionExistente.setCantidad(gestion.getCantidad());
            gestionExistente.setProveedor(gestion.getProveedor());
            gestionExistente.setPieza(gestion.getPieza());
            gestionExistente.setProyecto(gestion.getProyecto());

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
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

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Buscamos la gestión que queremos eliminar
            Gestion gestion = session.find(Gestion.class, id);

            if (gestion == null) {
                throw new RuntimeException("No existe ninguna gestión con ese identificador.");
            }

            // Eliminamos la gestión
            session.remove(gestion);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al eliminar la gestión.", e);
        }
    }
}
