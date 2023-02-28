package DAO;

import databaseConnection.EntityManagerProvider;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import models.Book;

public class BookDAO implements BaseDao<Book, Integer> {

    @Override
    public EntityManager getEntityManager() {
        return EntityManagerProvider.getEntityManager();
    }

    @Override
    public void insert(Book b) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transcation = em.getTransaction();

        transcation.begin();
        em.persist(b);
        transcation.commit();
    }

    @Override
    public void delete(Book b) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction transcation = em.getTransaction();
        boolean exists = bookExists(b.getTitle(), b.getPublicationYear()) != null;
        if (!exists) {
            transcation.begin();
            em.remove(b);
            transcation.commit();
        }
    }
    

    public void update(Book updatedBook) throws Exception {
        EntityManager em = getEntityManager();
        Book original = em.find(Book.class, updatedBook.getId());
        updateBook(original, updatedBook);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(original);
        transaction.commit();
    }

    private void updateBook(Book original, Book updated) {
        original.setTitle(updated.getTitle());
        original.setAuthor(updated.getAuthor());
        original.setGenre(updated.getGenre());
        original.setPublicationYear(updated.getPublicationYear());
        original.setPublishingHouse(updated.getPublishingHouse());
        original.setSynopsis(updated.getSynopsis());
        original.setStatus(updated.getStatus());
    }

    @Override
    public Book getById(Integer id) {
        return getEntityManager().find(Book.class, id);
    }

    @Override
    public List<Book> getAll() {
        return getEntityManager().createNamedQuery("Book.findAll")
                .getResultList();
    }

    public List<Book> getLastFive() {
            return getEntityManager().createQuery("SELECT b FROM Book b WHERE b.status!='D' ORDER BY b.dateAdded DESC", Book.class)
                .setMaxResults(5)
                .getResultList();
    }


    public List<Book> getTopFive() {
        return getEntityManager().createQuery("SELECT b "
                        + "FROM Book b JOIN Review r on b.id=r.reviewPK.bookID "
                        + "WHERE b.status!='D' "
                        + "GROUP By b.id "
                        + "ORDER BY AVG(r.rating) DESC ", Book.class)
                .setMaxResults(5)
                .getResultList();
    }

    public double getAverageRating(int id) {
        String query = "SELECT r.rating FROM Review r WHERE r.reviewPK.bookID = :bookID";

        return getEntityManager().createQuery(query, Integer.class)
                .setParameter("bookID", id)
                .getResultList()
                .stream()
                .mapToInt(r -> r)
                .average()
                .orElse(0);
    }

    public Book bookExists(String title, String publicationYear) {
        String query = "SELECT b FROM Book b WHERE b.title=:title "
                + "AND b.publicationYear=:publicationYear";
        try {
            Book foundBook = getEntityManager().createQuery(query, Book.class)
                    .setParameter("title", title)
                    .setParameter("publicationYear", publicationYear)
                    .getSingleResult();

            return foundBook;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Book> getAllBookWithAverageRatingGreaterThan(int averageRating) {
        String query = "SELECT b "
                + "FROM Book b join Review r ON b.id=r.reviewPK.bookID "
                + "WHERE b.status!='D'"
                + "GROUP BY r.reviewPK.bookID "
                + "HAVING avg(r.rating)>:value";

        return getEntityManager().createQuery(query)
                .setParameter("value", averageRating)
                .getResultList();
    }

    public List<Book> filterBooks(String title, String author,
            int min, int max, String genre){
        List<Book> filteredBooks = 
                filterBooksByAverageRatingRange(min, max);
        if(isValueSet(title))
            filteredBooks = filteredBooks.stream()
                    .filter(b->b.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        if(isValueSet(author))
            filteredBooks = filteredBooks.stream()
                    .filter(b->b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    .collect(Collectors.toList());
        if(isValueSet(genre))
            filteredBooks = filteredBooks.stream()
                    .filter(b->b.getGenre().toLowerCase().contains(genre.toLowerCase()))
                    .collect(Collectors.toList());
        
        return filteredBooks;
    
    }

    public boolean isValueSet(String value){
        return value!=null && !value.equals("");
    }

    private List<Book> filterBooksByAverageRatingRange(int minAverageRating, int maxAverageRating) {
        BookDAO bookDAO = new BookDAO();
        List<Book> listOfBooks = bookDAO.getAll();
        if(minAverageRating!=0 && maxAverageRating!=0){
            return listOfBooks.stream()
                    .filter(b->bookDAO.getAverageRating(b.getId())>=minAverageRating)
                    .filter(b->bookDAO.getAverageRating(b.getId())<=maxAverageRating)
                    .collect(Collectors.toList());
        }
        else if(minAverageRating!=0){
            return listOfBooks.stream()
                    .filter(b->bookDAO.getAverageRating(b.getId())>=minAverageRating)
                    .collect(Collectors.toList());
        }
        else if(maxAverageRating!=0){
            return listOfBooks.stream()
                    .filter(b->bookDAO.getAverageRating(b.getId())<=maxAverageRating)
                    .filter(b->bookDAO.getAverageRating(b.getId())>0)
                    .collect(Collectors.toList());
        }
        return listOfBooks;
    }
    

}
