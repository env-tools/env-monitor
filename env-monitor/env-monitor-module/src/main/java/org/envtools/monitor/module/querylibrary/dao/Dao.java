package org.envtools.monitor.module.querylibrary.dao;

import edu.umd.cs.findbugs.annotations.Nullable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * Created: 05.03.16 1:55
 *
 * @author Yury Yakovlev
 * @see org.springframework.data.repository.CrudRepository
 * @see org.springframework.data.jpa.repository.JpaRepository
 */

public interface Dao<T, ID extends Serializable> {

    List<T> findAll();

    List<T> findAll(Iterable<ID> ids);

    <S extends T> List<S> save(Iterable<S> entities);

    void flush();

    <S extends T> S saveAndFlush(S entity);

    <S extends T> S save(S entity);

    void delete(T entity);

    void deleteById(ID id);

    @Nullable
    T getOne(ID id);

}
