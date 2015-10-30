package org.envtools.monitor.common.serialization;

import java.io.Serializable;

/**
 * Created: 10/31/15 2:23 AM
 *
 * @author Yury Yakovlev
 */
public interface Serializer {

    String serialize(Serializable object);

    Serializable deserialize(String serializedForm);

}
