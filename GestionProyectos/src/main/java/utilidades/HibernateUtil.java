package utilidades;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY;

    static {
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new ExceptionInInitializerError(
                    "No se ha encontrado la variable de entorno DB_PASSWORD"
            );
        }

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .applySetting("hibernate.connection.password", password)
                .build();

        try {
            SESSION_FACTORY = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();

        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void cerrar() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}