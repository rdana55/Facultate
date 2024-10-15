package ro.persistence;

public class HibernateUtils {
    /*
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                sessionFactory = createNewSessionFactory();
            } catch (Exception ex) {
                // Log the exception or handle it appropriately
                ex.printStackTrace();
                throw new RuntimeException("Error initializing Hibernate session factory", ex);
            }
        }
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Concurs.class)
                .addAnnotatedClass(Inscriere.class)
                .addAnnotatedClass(Organizator.class)
                .addAnnotatedClass(Participant.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

     */
}
