package org.envtools.monitor.common.serialization;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created: 10/31/15 2:24 AM
 *
 * @author Yury Yakovlev
 */
public class JSONSerializer implements Serializer {

    private static final Logger LOGGER = Logger.getLogger(JSONSerializer.class);

    @Override
    public String serialize(Serializable object) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(object);
            return json;
        } catch (IOException e) {
            LOGGER.error("Could not serialize object to JSON : " + object);
            return null;
        }
    }

    @Override
    public Serializable deserialize(String serializedForm) {
        //TODO implement
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
