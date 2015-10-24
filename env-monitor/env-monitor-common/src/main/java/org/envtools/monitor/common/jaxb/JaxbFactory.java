package org.envtools.monitor.common.jaxb;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class JaxbFactory {

    public static Marshaller createMarshaller(Class... clazzes) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazzes);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        return marshaller;
    }

    public static Marshaller createMarshaller(Schema schema, Class... clazzes) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazzes);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);
        return marshaller;
    }

    public static Unmarshaller createUnmarshaller(Class... clazzes) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazzes);
        return jc.createUnmarshaller();
    }

    public static Unmarshaller createUnmarshaller(Schema schema, Class... clazzes) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(clazzes);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }

    public static Unmarshaller createUnmarshaller(File schemaFile, Class... clazzes) throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(clazzes);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(createSchema(schemaFile));
        return unmarshaller;
    }

    public static Schema createSchema(File file) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        sf.newSchema();
        return sf.newSchema(file);
    }

    public static Schema createSchema(URL resource) throws SAXException, URISyntaxException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return sf.newSchema(resource);
    }
}
