import utilidades.HibernateUtil;

public class App {

    public static void main(String[] args) {

        try {
            System.out.println("Iniciando aplicación...");

            HibernateUtil.getSessionFactory();

            System.out.println("Hibernate inicializado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación:");
            e.printStackTrace();

        } finally {
            HibernateUtil.cerrar();
        }
    }
}