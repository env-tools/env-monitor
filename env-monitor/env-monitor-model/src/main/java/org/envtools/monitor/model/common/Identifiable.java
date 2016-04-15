package org.envtools.monitor.model.common;

import java.io.Serializable;

/**
 * Created: 11/20/15 11:26 PM
 *
 * @author Yury Yakovlev
 */
public interface Identifiable<T> extends Serializable {

    T getId();

    void setId(T id);

}
