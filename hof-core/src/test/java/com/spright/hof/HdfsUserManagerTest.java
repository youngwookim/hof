package com.spright.hof;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.Md5PasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.WriteRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsUserManagerTest {

  private static final Logger LOG = LoggerFactory.getLogger(HdfsUserManagerTest.class);
  private static User USER1;
  private static User USER2;
  private static File TEMP_CONF_FILE;
  private PasswordEncryptor PASSWORD_ENCRYPTOR = new Md5PasswordEncryptor();
  String ADMIN_NAME = "amdin";

  private static final String DEFAULT_PASSWORD = "pwd";
  private static final List<Authority> DEFAULT_AUTHORITIES = null;
  private static final int DEFAULT_MAXIDLETIME = 123;
  private static final String DEFAULT_HOME = "/home";
  private static final boolean DEFAULT_ENABLE = false;

  @BeforeClass
  public static void setUpClass() throws IOException {
    LOG.info("Start test HdfsUserManager.java");
    USER1 = Mockito.mock(User.class);
    Mockito.when(USER1.getName()).thenReturn("user1");
    Mockito.when(USER1.getPassword()).thenReturn(DEFAULT_PASSWORD);
    Mockito.when(USER1.getAuthorities()).thenReturn(DEFAULT_AUTHORITIES);
    Mockito.when(USER1.getMaxIdleTime()).thenReturn(DEFAULT_MAXIDLETIME);
    Mockito.when(USER1.getHomeDirectory()).thenReturn(DEFAULT_HOME);
    Mockito.when(USER1.getEnabled()).thenReturn(DEFAULT_ENABLE);

    USER2 = Mockito.mock(User.class);
    Mockito.when(USER2.getName()).thenReturn("user2");
    Mockito.when(USER2.getPassword()).thenReturn(DEFAULT_PASSWORD);
    Mockito.when(USER2.getAuthorities()).thenReturn(DEFAULT_AUTHORITIES);
    Mockito.when(USER2.getMaxIdleTime()).thenReturn(DEFAULT_MAXIDLETIME);
    Mockito.when(USER2.getHomeDirectory()).thenReturn(DEFAULT_HOME);
    Mockito.when(USER2.getEnabled()).thenReturn(DEFAULT_ENABLE);

    TEMP_CONF_FILE = File.createTempFile("userConfTest", ".tmp");
    try (FileWriter fw = new FileWriter(TEMP_CONF_FILE)) {
      fw.write("ftpserver.user.confUsr.userpassword=310dcbbf4cce62f762a2aaa148d556bd\n");
      fw.write("ftpserver.user.confUsr.homedirectory=/\n");
      fw.write("ftpserver.user.confUsr.enableflag=true\n");
      fw.write("ftpserver.user.confUsr.writepermission=true\n");
      fw.write("ftpserver.user.confUsr.maxloginnumber=0\n");
      fw.write("ftpserver.user.confUsr.maxloginperip=0\n");
      fw.write("ftpserver.user.confUsr.idletime=0\n");
      fw.write("ftpserver.user.confUsr.uploadrate=0\n");
      fw.write("ftpserver.user.confUsr.downloadrate=0\n");
      fw.write("ftpserver.user.confUsr.groups=confUsr,users");
    }
  }

  @AfterClass
  public static void tearDownClass() {
    if (TEMP_CONF_FILE != null) {
      TEMP_CONF_FILE.delete();
    }
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of save method, of class HdfsUserManager.
   */
  @Test
  public void testSave() throws Exception {
    LOG.info("Start testSave");

    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);
    User expUser = instance.getUserByName("user1");

    assertEquals(expUser.getName(), "user1");
    //null means no permission
    assertNull(expUser.authorize(new WriteRequest()));
    assertEquals(DEFAULT_MAXIDLETIME, expUser.getMaxIdleTime());
    assertEquals(DEFAULT_HOME, expUser.getHomeDirectory());
    assertEquals(DEFAULT_ENABLE, expUser.getEnabled());
  }

  /**
   * Test of delete method, of class HdfsUserManager.
   */
  @Test
  public void testDelete() throws Exception {
    LOG.info("Start testDelete");
    String usrName = "user1";
    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);
    instance.delete(usrName);

    assertNull(instance.getUserByName(usrName));
  }

  /**
   * Test of getAllUserNames method, of class HdfsUserManager.
   */
  @Test
  public void testGetAllUserNames() throws FtpException {
    LOG.info("Start testGetAllUserNames");
    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);
    instance.save(USER2);
    String[] result = instance.getAllUserNames();
    String[] expResult = {"confUsr", "user1", "user2"};

    assertArrayEquals(expResult, result);
  }

  /**
   * Test of getUserByName method, of class HdfsUserManager.
   */
  @Test
  public void testGetUserByName() throws FtpException {
    LOG.info("Start testGetUserByName");
    String userName = "user1";
    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);

    User expUser = instance.getUserByName(userName);
    assertEquals("user1", expUser.getName());
    //null means no permission
    assertNull(expUser.authorize(new WriteRequest()));
    assertEquals(DEFAULT_MAXIDLETIME, expUser.getMaxIdleTime());
    assertEquals(DEFAULT_HOME, expUser.getHomeDirectory());
    assertEquals(DEFAULT_ENABLE, expUser.getEnabled());
  }

  /**
   * Test of doesExist method, of class HdfsUserManager.
   */
  @Test
  public void testDoesExist() throws FtpException {
    LOG.info("Start testDoesExist");
    String name = "user1";
    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);
    boolean result = instance.doesExist(name);

    assertTrue(result);
  }

  /**
   * Test of authenticate method, of class HdfsUserManager.
   */
  @Test
  public void testAuthenticate() throws Exception {
    LOG.info("Start testAuthenticate");
    UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(USER1.getName(), USER1.getPassword());
    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);
    User expUser = instance.authenticate(authentication);

    assertEquals(expUser.getName(), "user1");
    //null means no permission
    assertNull(expUser.authorize(new WriteRequest()));
    assertEquals(DEFAULT_MAXIDLETIME, expUser.getMaxIdleTime());
    assertEquals(DEFAULT_HOME, expUser.getHomeDirectory());
    assertEquals(DEFAULT_ENABLE, expUser.getEnabled());
  }

  /**
   * Test of dispose method, of class HdfsUserManager.
   */
  @Test(expected = NullPointerException.class)
  public void testDoesExistAfterDispose() throws FtpException {
    LOG.info("Start testDoesExistAfterDispose");
    HdfsUserManager instance = new HdfsUserManager(PASSWORD_ENCRYPTOR, TEMP_CONF_FILE, ADMIN_NAME);
    instance.save(USER1);

    assertTrue(instance.doesExist("user1"));
    instance.dispose();
    //Set userDataProp null,so many function will cause NullPointerException
    instance.doesExist("user1");
  }
}
