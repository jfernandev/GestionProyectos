import utilidades.HibernateUtil;

public class App {

    public static void main(String[] args) {

        try {
            // Comprobamos que Hibernate puede inicializarse
            System.out.println("Iniciando aplicación...");

            HibernateUtil.getSessionFactory();

            System.out.println("Hibernate inicializado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación:");
            e.printStackTrace();

        } finally {
            // Cerramos Hibernate al finalizar la aplicación
            HibernateUtil.cerrar();
        }
    }
}