package DAO;


import javax.persistence.EntityManager;
import java.util.List;


public interface BaseDao<T, K> {
    EntityManager getEntityManager();

    void insert(T t) throws Exception;

    void delete(T t) throws Exception;

    List<T> getAll();

    T getById(K key);
}
