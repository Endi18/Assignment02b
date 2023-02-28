package databaseConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
public class EntityManagerProvider {

    private static final String persistanceUnit = "Assignment2PU";
    

    public static EntityManager getEntityManager(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnit);
        return emf.createEntityManager();
    }
}
