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

public class HdfsUserTest {

  private static User USER;
  private static HdfsUser HDFSUSER;

  private final static String DEFAULT_USERNAME = "user";
  private final static String DEFAULT_PASSWORD = "pwd";
  private final static Authority[] DEFAULT_AUTHORITIES = null;
  private final static int DEFAULT_MAXIDLETIME = 123;
  private final static String DEFAULT_HOME = "/home";
  private final static boolean DEFAULT_ENABLE = false;

  public HdfsUserTest() {
  }

  @BeforeClass
  public static void setUpClass() {
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
    System.out.println("getMainGroup");
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
    System.out.println("isGroupMember");
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
    System.out.println("SetAndGetGroups");
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
    System.out.println("SetAndGetName");
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
    System.out.println("setPassword");
    HdfsUser instance = new HdfsUser(USER);
    instance.setPassword("passwordTest");

    assertEquals("passwordTest", instance.getPassword());
  }

  /**
   * Test of setAuthorities、getAuthorities method, of class HdfsUser.
   */
  @Test
  public void testSetAndgetAuthorities() {
    System.out.println("testSetAndgetAuthorities");
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
    System.out.println("testSetAndGetMaxIdleTime");
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
    System.out.println("testSetAndGetEnabled");
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
    System.out.println("testSetAndGetHomeDirectory");
    HdfsUser instance = new HdfsUser(USER);
    instance.setHomeDirectory("/home/dir");

    assertEquals("/home/dir", instance.getHomeDirectory());
  }

  /**
   * Test of toString method, of class HdfsUser.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    HdfsUser instance = new HdfsUser(USER);

    assertEquals(DEFAULT_USERNAME, instance.toString());
  }

  /**
   * Test of authorize method, of class HdfsUser.
   */
  @Test
  public void testAuthorize() {
    System.out.println("authorize");
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
    System.out.println("getAuthorities");
    Authority authority1 = Mockito.mock(Authority.class);
    Authority authority2 = Mockito.mock(Authority.class);
    Authority[] authorities = {authority1, authority2};

    HdfsUser instance = new HdfsUser();
    instance.setAuthorities(authorities);
    Authority[] result = instance.getAuthorities(authority1.getClass());

    assertArrayEquals(authorities, result);
  }
}