package org.envtools.monitor.common.ssh;

import org.apache.log4j.Logger;
import org.envtools.monitor.common.jaxb.JaxbHelper;
import org.junit.*;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * Created by esemenov on 14.07.16.
 */
public class SshCredentialsServiceTest {

    //TODO: Finish the test;
    private static final Logger LOG = Logger.getLogger(SshCredentialsServiceTest.class);

    private final String expectedPassword = "password";
    private final String fileName = "test.xml";
    private final String directoryPath = "src/test/resource/";
    private File testXml;


    @Before
    public void setUp() throws JAXBException, IOException {

        testXml = new File(String.format("%s%s", directoryPath, fileName));
        Credentials credentials = new Credentials();

        credentials.setHost("some host");
        credentials.setUser("some user");
        credentials.setLoadAtStartup(true);
        credentials.setEncryptedPassword("kXwtAlotRoiPCCy2KyteL8Pp1rPu5uba");

        testXml.createNewFile();
        JaxbHelper.marshallToFile(credentials, testXml);

    }

    @Ignore
    @Test
    public void decryptPasswordTest() {

        SshCredentialsServiceImpl credentialsService =
                new SshCredentialsServiceImpl(directoryPath);
        Credentials credentials = credentialsService.getCredentials(fileName);
        LOG.info("We have working encryptor service");
        Assert.assertEquals(credentials.getPassword(), expectedPassword);

    }


    @After
    public void cleanUp() {
        testXml.delete();
    }


}
