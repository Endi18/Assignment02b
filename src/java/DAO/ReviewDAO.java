package DAO;

import databaseConnection.EntityManagerProvider;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import models.Book;
import models.Review;
import models.ReviewPK;
import models.User;

public class ReviewDAO implements BaseDao<Review, ReviewPK>{
 
    @Override
    public EntityManager getEntityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    @Override
    public void insert(Review r) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(r);
        transaction.commit();
    }

    @Override
    public void delete(Review r) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        if(!em.contains(r)){
            r = em.merge(r);
        }
        em.remove(r);
        em.flush();
        transaction.commit();
    }

    public void removeReview(ReviewPK reviewPK){
        getEntityManager().createQuery("DELETE FROM Review r WHERE r.reviewPK=:reviewPK")
                .setParameter("reviewPK", reviewPK);
    }

    public Review getByIds(int userId, int bookID) {
        return getEntityManager().createQuery("SELECT r FROM Review r WHERE r.reviewPK.userID=:userID AND r.reviewPK.bookID=:bookID", Review.class)
                .setParameter("userID", userId)
                .setParameter("bookID", bookID)
                .getSingleResult();
    }
    
    @Override
    public Review getById(ReviewPK reviewPK){
        return getEntityManager().createQuery("SELECT r FROM Review r WHERE r.reviewPK=:reviewPK", Review.class)
                .setParameter("reviewPK", reviewPK)
                .getSingleResult();
    }

    @Override
    public List<Review> getAll() {
        return getEntityManager().createNamedQuery("Review.findAll")
                .getResultList();
    }

    public List<Review> getBookReviews(int id){
        return getEntityManager().createQuery("SELECT r FROM Review r WHERE r.reviewPK.bookID=:bookID", Review.class)
                .setParameter("bookID", id)
                .getResultList();
    }
    

    public List<Review> getUserReviews(int id){
        return getEntityManager().createQuery("SELECT r FROM Review r WHERE r.reviewPK.userID=:userId", Review.class)
                .setParameter("userId", id)
                .getResultList();
    }

    public boolean reviewExists(int userID, int bookID) {
        try {
            Review foundReview = getEntityManager().createQuery("SELECT r FROM Review r "
                            + "WHERE r.reviewPK.userID=:userID AND r.reviewPK.bookID=:bookID", Review.class)
                    .setParameter("userID", userID)
                    .setParameter("bookID", bookID)
                    .getSingleResult();

            return foundReview != null;
        } catch (NoResultException e) {
            return false;
        }
    }
    
}
