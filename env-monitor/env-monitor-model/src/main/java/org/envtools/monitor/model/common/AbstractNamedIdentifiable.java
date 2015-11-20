package org.envtools.monitor.model.common;

/**
 * Created: 11/20/15 11:30 PM
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractNamedIdentifiable <T> extends AbstractIdentifiable <T> implements HasName{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
