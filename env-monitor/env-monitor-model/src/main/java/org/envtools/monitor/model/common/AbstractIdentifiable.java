package org.envtools.monitor.model.common;

/**
 * Created: 11/20/15 11:29 PM
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractIdentifiable <T> implements Identifiable<T> {

    protected T id;

    protected AbstractIdentifiable() {
    }

    protected AbstractIdentifiable(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

}
