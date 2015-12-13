package org.envtools.monitor.module.core.selection;

import java.io.Serializable;

/**
 * Created: 12/5/15 1:20 AM
 *
 * @author Yury Yakovlev
 */
public interface Extractor<T extends Serializable, S> {

    T extract(T source, S selector);

    T emptyExtractionResult();

}
