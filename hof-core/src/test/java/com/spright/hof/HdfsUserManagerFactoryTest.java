package com.spright.hof;

import java.io.File;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsUserManagerFactoryTest {

  private static final Logger LOG = LoggerFactory.getLogger(HdfsUserManagerFactoryTest.class);

  @BeforeClass
  public static void setUpClass() {
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
   * Test of setAdminName、getAdminName method, of class HdfsUserManagerFactory.
   */
  @Test
  public void testSetAndGetAdminName() {
    LOG.info("Start testSetAndGetFile");
    HdfsUserManagerFactory instance = new HdfsUserManagerFactory();
    String expResult = "";
    instance.setAdminName(expResult);
    String result = instance.getAdminName();

    assertEquals(expResult, result);
  }

  /**
   * Test of getFile、setFile method, of class HdfsUserManagerFactory.
   */
  @Test
  public void testSetAndGetFile() {
    LOG.info("Start testSetAndGetFile");
    HdfsUserManagerFactory instance = new HdfsUserManagerFactory();
    File expResult = new File("testFile");
    instance.setFile(expResult);
    File result = instance.getFile();

    assertEquals(expResult, result);
  }

  /**
   * Test of getPasswordEncryptor、setPasswordEncryptor method, of class
   * HdfsUserManagerFactory.
   */
  @Test
  public void testSetAndGetPasswordEncryptor() {
    LOG.info("Start testSetAndGetPasswordEncryptor");
    HdfsUserManagerFactory instance = new HdfsUserManagerFactory();
    PasswordEncryptor expResult = new Md5PasswordEncryptor();
    instance.setPasswordEncryptor(expResult);
    PasswordEncryptor result = instance.getPasswordEncryptor();

    assertEquals(expResult, result);
  }
}
