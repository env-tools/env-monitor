package org.envtools.monitor.model.messaging.content;

/**
 * Created: 01.04.16 22:21
 *
 * @author Yury Yakovlev
 */
public abstract class AbstractContent <T>{

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
