package DAO;

import databaseConnection.EntityManagerProvider;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import models.User;

public class UserDAO implements BaseDao<User, Integer> {

    @Override
    public void insert(User user) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(user);
        transaction.commit();
    }

    @Override
    public void delete(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(user);
        transaction.commit();
    }

    public void update(User userUpdated) {
        EntityManager em = getEntityManager();
        String query = "UPDATE User u SET u.name=:name, u.surname=:surname,"
                + "u.email=:email, u.userType=:userType, u.status=:status, "
                + "u.password=:password WHERE u.id=:id";
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createQuery(query)
                .setParameter("name", userUpdated.getName())
                .setParameter("surname", userUpdated.getSurname())
                .setParameter("email", userUpdated.getEmail())
                .setParameter("status", userUpdated.getStatus())
                .setParameter("userType", userUpdated.getUserType())
                .setParameter("id", userUpdated.getId())
                .setParameter("password", userUpdated.getPassword())
                .executeUpdate();
        transaction.commit();
    }

    @Override
    public EntityManager getEntityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    @Override
    public User getById(Integer id) {
        EntityManager em = getEntityManager();
        User user = (User) em.createNamedQuery("User.findById", User.class)
                .setParameter("id", id)
                .getSingleResult();
        return user;
    }

    public User getUserByEmail(String email) throws Exception {
        EntityManager em = getEntityManager();
        User user = (User) em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return user;
    }

    public List<User> getAllUsersStatusInsensitive() {
        String query = "SELECT u FROM User u";
        return getEntityManager().createQuery(query)
                .getResultList();
    }

    @Override
    public List<User> getAll() {
        return getEntityManager().createNamedQuery("User.findAll", User.class)
                .getResultList();
    }

    public List<User> filterUsers(String name, String surname,
            String email, String type) {
        String query = "SELECT u FROM User u WHERE u.status !='D' ";
        if (name != null) {
            query += " AND LOWER(u.name) LIKE LOWER(:name)";
        }
        if (surname != null && !surname.equals("")) {
            query += " AND LOWER(u.surname) LIKE LOWER(:surname)";
        }
        if (email != null && !email.equals("")) {
            query += " AND LOWER(u.email) LIKE LOWER(:email)";
        }
        if (type != null && !type.equals("")) {
            query += " AND u.userType= :userType";
        }

        TypedQuery<User> tQuery = getEntityManager().createQuery(query, User.class);
        if (name != null) {
            tQuery.setParameter("name", name + "%");
        }
        if (surname != null && !surname.equals("")) {
            tQuery.setParameter("surname", surname + "%");
        }
        if (email != null && !email.equals("")) {
            tQuery.setParameter("email", email + "%");
        }
        if (!"".equals(type)) {
            tQuery.setParameter("type", type);
        }

        return tQuery.getResultList();

    }
}
