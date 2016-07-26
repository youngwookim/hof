package com.spright.hof;

import java.util.ArrayList;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.WriteRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsUserTest {
  private static final Logger LOG = LoggerFactory.getLogger(HdfsUserTest.class);
  private static User USER;
  private static HdfsUser HDFSUSER;

  private static final String DEFAULT_USERNAME = "user";
  private static final String DEFAULT_PASSWORD = "pwd";
  private static final Authority[] DEFAULT_AUTHORITIES = null;
  private static final int DEFAULT_MAXIDLETIME = 123;
  private static final String DEFAULT_HOME = "/home";
  private static final boolean DEFAULT_ENABLE = false;

  @BeforeClass
  public static void setUpClass() {
    LOG.info("Start test HdfsUser.java");
    USER = Mockito.mock(User.class);
    Mockito.when(USER.getName()).thenReturn(DEFAULT_USERNAME);
    Mockito.when(USER.getPassword()).thenReturn(DEFAULT_PASSWORD);
    Mockito.when(USER.getAuthorities()).thenReturn(DEFAULT_AUTHORITIES);
    Mockito.when(USER.getMaxIdleTime()).thenReturn(DEFAULT_MAXIDLETIME);
    Mockito.when(USER.getHomeDirectory()).thenReturn(DEFAULT_HOME);
    Mockito.when(USER.getEnabled()).thenReturn(DEFAULT_ENABLE);

    HDFSUSER = new HdfsUser(USER);
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
   * Test of getMainGroup method, of class HdfsUser.
   */
  @Test
  public void testGetMainGroup() {
    LOG.info("Start testGetMainGroup");
    HdfsUser instance = new HdfsUser(USER);
    ArrayList<String> groups = new ArrayList<String>();
    groups.add("g1");
    groups.add("g2");
    instance.setGroups(groups);

    assertEquals("g1", instance.getMainGroup());
  }

  /**
   * Test of isGroupMember method, of class HdfsUser.
   */
  @Test
  public void testIsGroupMember() {
    LOG.info("Start testIsGroupMember");
    HdfsUser instance = new HdfsUser(USER);
    ArrayList<String> groups = new ArrayList<String>();
    groups.add("g1");
    groups.add("g2");
    instance.setGroups(groups);

    assertTrue(instance.isGroupMember("g2"));
    assertFalse(instance.isGroupMember("g3"));
  }

  /**
   * Test of setGroups、getGroups method, of class HdfsUser.
   */
  @Test
  public void testSetAndGetGroups() {
    LOG.info("Start testSetAndGetGroups");
    HdfsUser instance = new HdfsUser(USER);
    ArrayList<String> groups = new ArrayList<String>();
    groups.add("g1");
    instance.setGroups(groups);

    assertEquals(groups, instance.getGroups());
  }

  /**
   * Test of setName 、getName method, of class HdfsUser.
   */
  @Test
  public void testSetAndGetName() {
    LOG.info("Start testSetAndGetName");
    String name = "";
    HdfsUser instance = new HdfsUser(USER);
    instance.setName("userTest");

    assertEquals("userTest", instance.getName());
  }

  /**
   * Test of setPassword、getPassword method, of class HdfsUser.
   */
  @Test
  public void testSetAndGetPassword() {
    LOG.info("Start testSetAndGetPassword");
    HdfsUser instance = new HdfsUser(USER);
    instance.setPassword("passwordTest");

    assertEquals("passwordTest", instance.getPassword());
  }

  /**
   * Test of setAuthorities、getAuthorities method, of class HdfsUser.
   */
  @Test
  public void testSetAndgetAuthorities() {
    LOG.info("Start testSetAndgetAuthorities");
    Authority[] authorities = new Authority[0];
    HdfsUser instance = new HdfsUser(USER);

    instance.setAuthorities(authorities);
    assertArrayEquals(authorities, instance.getAuthorities());

    instance.setAuthorities(null);
    assertNull(instance.getAuthorities());
  }

  /**
   * Test of setMaxIdleTime 、getMaxIdleTime method, of class HdfsUser.
   */
  @Test
  public void testSetAndGetMaxIdleTime() {
    LOG.info("Start testSetAndGetMaxIdleTime");
    HdfsUser instance = new HdfsUser(USER);
    instance.setMaxIdleTime(5566);
    assertEquals(5566, instance.getMaxIdleTime());

    instance.setMaxIdleTime(-5566);
    assertEquals(0, instance.getMaxIdleTime());
  }

  /**
   * Test of setEnabled 、getEnabled method, of class HdfsUser.
   */
  @Test
  public void testSetAndGetEnabled() {
    LOG.info("Start testSetAndGetEnabled");
    HdfsUser instance = new HdfsUser(USER);
    instance.setEnabled(false);
    assertFalse(instance.getEnabled());

    instance.setEnabled(true);
    assertTrue(instance.getEnabled());
  }

  /**
   * Test of setHomeDirectory 、 getHomeDirectory method, of class HdfsUser.
   */
  @Test
  public void testSetAndGetHomeDirectory() {
    LOG.info("Start testSetAndGetHomeDirectory");
    HdfsUser instance = new HdfsUser(USER);
    instance.setHomeDirectory("/home/dir");

    assertEquals("/home/dir", instance.getHomeDirectory());
  }

  /**
   * Test of toString method, of class HdfsUser.
   */
  @Test
  public void testToString() {
    LOG.info("Start testToString");
    HdfsUser instance = new HdfsUser(USER);

    assertEquals(DEFAULT_USERNAME, instance.toString());
  }

  /**
   * Test of authorize method, of class HdfsUser.
   */
  @Test
  public void testAuthorize() {
    LOG.info("Start testAuthorize");
    AuthorizationRequest request = new WriteRequest();
    HdfsUser instance = new HdfsUser(USER);

    assertNull(instance.authorize(request));

    Authority authority = Mockito.mock(Authority.class);
    Mockito.when(authority.canAuthorize(request)).thenReturn(true);
    Mockito.when(authority.authorize(request)).thenReturn(request);
    Authority[] authorityArray = {authority};
    instance.setAuthorities(authorityArray);

    assertEquals(request, instance.authorize(request));
  }

  /**
   * Test of getAuthorities method, of class HdfsUser.
   */
  @Test
  public void testGetAuthorities_Class() {
    LOG.info("Start testGetAuthorities_Class");
    Authority authority1 = Mockito.mock(Authority.class);
    Authority authority2 = Mockito.mock(Authority.class);
    Authority[] authorities = {authority1, authority2};

    HdfsUser instance = new HdfsUser();
    instance.setAuthorities(authorities);
    Authority[] result = instance.getAuthorities(authority1.getClass());

    assertArrayEquals(authorities, result);
  }
}