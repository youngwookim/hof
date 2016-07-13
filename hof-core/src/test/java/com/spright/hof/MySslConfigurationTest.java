/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spright.hof;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.ftpserver.ssl.ClientAuth;
import org.apache.ftpserver.ssl.ExtendedAliasKeyManager;
import org.apache.ftpserver.util.ClassUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dslab
 */
public class MySslConfigurationTest {

  private final static String DEFAULT_KEYSTOREPASS = "333333";
  private final static File DEFAULT_KEYSTOREFILE = null;
  private final static String DEFAULT_KEYSTORETYPE = KeyStore.getDefaultType();
  private final static String DEFAULT_KEYSTOREALGORITHM = "SunX509";

  private final static String DEFAULT_TRUSTSTOREPASS = "333333";
  private final static File DEFAULT_TRUSTSTOREFILE = null;
  private final static String DEFAULT_TRUSTSTORETYPE = KeyStore.getDefaultType();
  private final static String DEFAULT_TRUSTSTOREALGORITHM = "SunX509";

  private final static String DEFAULT_PROTOCOL = "TLS";
  private final static String DEFAULT_KEYALIAS = "testkeyalias";

  private static char [] KEYSTOREPASS;
  private static KeyStore KS;
  private static KeyManagerFactory KMF;

  private static char [] TRUSTKEYSTOREPASS;
  private static KeyStore TS;
  private static TrustManagerFactory TMF;

  private static HashMap<String, SSLContext> SSLCONTEXTMAP;
  private static SSLContext CTX;
  private static KeyManager[] KEYMANAGERS;

    public MySslConfigurationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyManagementException {

      KEYSTOREPASS = DEFAULT_KEYSTOREPASS.toCharArray();
      KS = KeyStore.getInstance(DEFAULT_KEYSTORETYPE);
      KS.load(null,KEYSTOREPASS);

      KMF = KeyManagerFactory.getInstance(DEFAULT_KEYSTOREALGORITHM);
      KMF.init(KS, KEYSTOREPASS);

      TRUSTKEYSTOREPASS = DEFAULT_TRUSTSTOREPASS.toCharArray();
      TS=KS;

      TMF = TrustManagerFactory.getInstance(DEFAULT_TRUSTSTOREALGORITHM);
      TMF.init(TS);

      KEYMANAGERS = KMF.getKeyManagers();
      if (ClassUtils.extendsClass(KEYMANAGERS[0].getClass(),"javax.net.ssl.X509ExtendedKeyManager")) {
        KEYMANAGERS[0] = new ExtendedAliasKeyManager(KEYMANAGERS[0], DEFAULT_KEYALIAS);
      }
      CTX = SSLContext.getInstance(DEFAULT_PROTOCOL);
      CTX.init(KEYMANAGERS, TMF.getTrustManagers(), null);
    }

    @AfterClass
    public static void tearDownClass() {

    }
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setKeystoreFile method and getKeystoreFile method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetKeystoreFile() {
        System.out.println("SetAndGetKeystoreFile");
        MySslConfiguration instance = new MySslConfiguration();
        File expResult = new File("test.txt");
        instance.setKeystoreFile(expResult);
        assertEquals(expResult, instance.getKeystoreFile());
    }

    /**
     * Test of setKeystorePassword method and getKeystorePassword method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetKeystorePassword() {
        System.out.println("SetAndGetKeystorePassword");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "123";
        instance.setKeystorePassword(expResult);
        assertEquals(expResult, instance.getKeystorePassword());
    }

    /**
     * Test of setKeystoreType method and getKeystoreType method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetKeystoreType() {
        System.out.println("SetAndGetKeystoreType");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "txt";
        instance.setKeystoreType(expResult);
        assertEquals(expResult, instance.getKeystoreType());
    }

    /**
     * Test of setKeystoreAlgorithm method and getKeystoreAlgorithm method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetKeystoreAlgorithm() {
        System.out.println("SetAndGetKeystoreAlgorithm");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "ibmX509";
        instance.setKeystoreAlgorithm(expResult);
        assertEquals(expResult, instance.getKeystoreAlgorithm());
    }

    /**
     * Test of setSslProtocol method and getSslProtocol method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetSslProtocol() {
        System.out.println("SetAndGetSslProtocol");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "SSL";
        instance.setSslProtocol(expResult);
        assertEquals(expResult, instance.getSslProtocol());
    }

    /**
     * Test of SetClientAuthentication method and getClientAuth method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetClientAuthentication() {
        System.out.println("SetAndGetClientAuthentication");
        MySslConfiguration instance = new MySslConfiguration();
        String clientAuthReqdString = "true";
        instance.setClientAuthentication(clientAuthReqdString);
        assertEquals(ClientAuth.NEED,instance.getClientAuth());
        clientAuthReqdString = "WaNt";
        instance.setClientAuthentication(clientAuthReqdString);
        assertEquals(ClientAuth.WANT,instance.getClientAuth());
        clientAuthReqdString = "qwer";
        instance.setClientAuthentication(clientAuthReqdString);
        assertEquals(ClientAuth.NONE,instance.getClientAuth());
    }

    /**
     * Test of setKeyPassword method and getKeyPassword method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetKeyPassword() {
        System.out.println("SetAndGetKeyPassword");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "456";
        instance.setKeyPassword(expResult);
        assertEquals(expResult, instance.getKeyPassword());
    }

    /**
     * Test of setTruststoreFile method and getTruststoreFile method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetTruststoreFile() {
        System.out.println("SetAndGetTruststoreFile");
        MySslConfiguration instance = new MySslConfiguration();
        File expResult = new File("TestTrustStore.txt");
        instance.setTruststoreFile(expResult);
        assertEquals(expResult, instance.getTruststoreFile());
    }

    /**
     * Test of setTruststorePassword method and getTruststorePassword method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetTruststorePassword() {
        System.out.println("SetAndGetTruststorePassword");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "789";
        instance.setTruststorePassword(expResult);
        assertEquals(expResult, instance.getTruststorePassword());
    }

    /**
     * Test of setTruststoreType method and getTruststoreType method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetTruststoreType() {
        System.out.println("SetAndGetTruststoreType");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = KeyStore.getDefaultType();
        instance.setTruststoreType(expResult);
        System.out.println(expResult);
        assertEquals(expResult, instance.getTruststoreType());
    }

    /**setSslProtocol
     * Test of setTruststoreAlgorithm method and getTruststoreAlgorithm method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetTruststoreAlgorithm() {
        System.out.println("SetAndGetTruststoreAlgorithm");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "ibmX509";
        instance.setTruststoreAlgorithm(expResult);
        assertEquals(expResult, instance.getTruststoreAlgorithm());
    }

    /**
     * Test of setEnabledCipherSuites method and getEnabledCipherSuites method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetEnabledCipherSuites() {
        System.out.println("SetAndGetEnabledCipherSuites");
        MySslConfiguration instance = new MySslConfiguration();
        String[] expResult={"0","1","2"};
        instance.setEnabledCipherSuites(expResult);
        assertArrayEquals(expResult, instance.getEnabledCipherSuites());
        expResult=null;
        instance.setEnabledCipherSuites(expResult);
        assertNull(instance.getEnabledCipherSuites());
    }

    /**
     * Test of setKeyAlias method and getKeyAlias method, of class MySslConfiguration.
     */
    @Test
    public void testSetAndGetKeyAlias() {
        System.out.println("SetAndGetKeyAlias");
        MySslConfiguration instance = new MySslConfiguration();
        String expResult = "testkeyalias";
        instance.setKeyAlias(expResult);
        assertEquals(expResult, instance.getKeyAlias());
    }

    /**
     * Test of getSSLContext method, of class MySslConfiguration.
     */
    @Test
    public void testInitAndGetSSLContext() throws GeneralSecurityException {
      System.out.println("testInit and testGetSSLContext");

      MySslConfiguration instance = new MySslConfiguration();

      instance.setKeystoreFile(DEFAULT_KEYSTOREFILE);
      instance.setKeystoreType(DEFAULT_KEYSTORETYPE);
      instance.setKeystorePassword(DEFAULT_KEYSTOREPASS);
      instance.setKeystoreAlgorithm(DEFAULT_KEYSTOREALGORITHM);

      instance.setTruststoreFile(DEFAULT_TRUSTSTOREFILE);
      instance.setTruststoreType(DEFAULT_TRUSTSTORETYPE);
      instance.setTruststorePassword(DEFAULT_TRUSTSTOREPASS);
      instance.setTruststoreAlgorithm(DEFAULT_TRUSTSTOREALGORITHM);

      instance.init();

      instance.setSslProtocol(DEFAULT_PROTOCOL);
      instance.setKeyAlias(DEFAULT_KEYALIAS);

      SSLContext result = instance.getSSLContext(null);
      assertEquals(CTX.getProtocol(),result.getProtocol() );
      assertEquals(instance.getSSLContext(),result);
    }

}
