package org.envtools.monitor.common.jaxb;

import edu.umd.cs.findbugs.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class JaxbHelper {

    public static <T> T unmarshallFromFile(File file, Class<T> clazz, @Nullable Schema schema) throws JAXBException {
        return (T) JaxbFactory.createUnmarshaller(schema, clazz).unmarshal(file);
    }

    public static <T> T unmarshallFromURL(URL url, Class<T> clazz, @Nullable Schema schema) throws JAXBException {
        return (T) JaxbFactory.createUnmarshaller(schema, clazz).unmarshal(url);
    }

    public static <T> T unmarshallFromInputStream(InputStream is, Class<T> clazz, @Nullable Schema schema) throws JAXBException {
        return (T) JaxbFactory.createUnmarshaller(schema, clazz).unmarshal(is);
    }

    public static <T> T unmarshallFromReader(Reader reader, Class<T> clazz, @Nullable Schema schema) throws JAXBException {
        return (T) JaxbFactory.createUnmarshaller(schema, clazz).unmarshal(reader);
    }

    public static <T> T unmarshallFromString(String source, Class<T> clazz) throws JAXBException {
        return unmarshallFromReader(new StringReader(source.trim()), clazz, null);
    }

    public static <T> T unmarshallFromString(String source, Class<T> clazz, @Nullable URL schemaURL) throws JAXBException, SAXException, URISyntaxException {
        Schema schema = schemaURL == null ? null : JaxbFactory.createSchema(schemaURL);
        return unmarshallFromReader(new StringReader(source.trim()), clazz, schema);
    }

    public static <T> String marshallToString(T object) throws JAXBException {
        StringWriter writer = new StringWriter();
        Marshaller marshaller = JaxbFactory.createMarshaller(object.getClass());
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    public static <T> void marshallToFile(T object, File file) throws JAXBException {
        Marshaller marshaller = JaxbFactory.createMarshaller(object.getClass());
        marshaller.marshal(object, file);
    }
}
