package com.spright.hof;

import java.io.IOException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FileObject;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class HdfsFileSystemViewTest {

  private static MiniDFSCluster CLUSTER;
  private static Configuration CONF;
  private static DistributedFileSystem DFS;

  private static User USER;
  private static HdfsUser HDFSUSER;
  private final static String DEFAULT_NAME = "user";
  private final static String DEFAULT_PASSWORD = "pwd";
  private final static Authority[] DEFAULT_AUTHORITIES = null;
  private final static int DEFAULT_MAXIDLETIME = 123;
  private final static String DEFAULT_HOME = "/home";
  private final static boolean DEFAULT_ENABLE = false;

  private final static String DEFAULT_FILE_PATH = "/home/file.txt";
  private final static String DEFAULT_DIR_PATH = "/home";
  private final static FsPermission DEFAULT_PERMISSION = new FsPermission((short) 1023);

  public HdfsFileSystemViewTest() {
  }

  @BeforeClass
  public static void setUpClass() throws IOException {
    System.out.println("setUpClass");

    USER = Mockito.mock(User.class);
    Mockito.when(USER.getName()).thenReturn(DEFAULT_NAME);
    Mockito.when(USER.getPassword()).thenReturn(DEFAULT_PASSWORD);
    Mockito.when(USER.getAuthorities()).thenReturn(DEFAULT_AUTHORITIES);
    Mockito.when(USER.getMaxIdleTime()).thenReturn(DEFAULT_MAXIDLETIME);
    Mockito.when(USER.getHomeDirectory()).thenReturn(DEFAULT_HOME);
    Mockito.when(USER.getEnabled()).thenReturn(DEFAULT_ENABLE);
    HDFSUSER = new HdfsUser(USER);

    System.out.println("Starte Create MiniDFSCluster");
    CONF = new HdfsConfiguration();
    MiniDFSCluster cluster = new MiniDFSCluster.Builder(CONF).build();
    DFS = cluster.getFileSystem();
    DFS.create(new Path(DEFAULT_FILE_PATH));
    DFS.setPermission(new Path(DEFAULT_FILE_PATH), DEFAULT_PERMISSION);
    //expect HdfsOverFtpSystem.getDfs() will return DFS.
    HdfsOverFtpSystem.setDfs(DFS);
  }

  @AfterClass
  public static void tearDownClass() throws IOException {
    System.out.println("tearDownClass");
    if (CLUSTER != null) {
      CLUSTER.shutdown();
      CLUSTER = null;
    }
    HdfsOverFtpSystem.setDfs(null);
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getHomeDirectory method, of class HdfsFileSystemView.
   */
  @Test
  public void testGetHomeDirectory() throws FtpException {
    System.out.println("getHomeDirectory");
    HdfsFileSystemView instance = new HdfsFileSystemView(HDFSUSER, true);
    FileObject result = instance.getHomeDirectory();

    // "/" was written as hard code in getHomeDirectory()
    assertEquals("/", result.getFullName());
    assertTrue(result.isDirectory());
  }

  /**
   * Test of getFileObject method, of class HdfsFileSystemView.
   */
  @Test
  public void testGetFileObject() throws FtpException {
    System.out.println("getFileObject");
    HdfsFileSystemView instance = new HdfsFileSystemView(HDFSUSER, true);

    FileObject result = instance.getFileObject(DEFAULT_FILE_PATH);
    assertEquals(DEFAULT_FILE_PATH, result.getFullName());
  }

  /**
   * Test of changeDirectory 、getCurrentDirectory method, of class
   * HdfsFileSystemView.
   */
  @Test
  public void testChangeANDGetCurrnentDirectory() throws FtpException {
    System.out.println("changeDirectory");
    HdfsFileSystemView instance = new HdfsFileSystemView(HDFSUSER, true);
    assertEquals("/", instance.getCurrentDirectory().getFullName());
    instance.changeDirectory(DEFAULT_DIR_PATH);
    assertEquals(DEFAULT_DIR_PATH, instance.getCurrentDirectory().getFullName());
  }

  /**
   * Test of changeDirectory ,getCurrentDirectory method, of class
   * HdfsFileSystemView.
   */
  @Test
  public void testIsRandomAccessible() throws FtpException {
    System.out.println("changeDirectory");
    HdfsFileSystemView instance = new HdfsFileSystemView(HDFSUSER, true);
    assertTrue(instance.isRandomAccessible());
  }
}
