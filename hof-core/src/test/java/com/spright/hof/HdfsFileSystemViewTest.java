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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsFileSystemViewTest {
  private static final Logger LOG = LoggerFactory.getLogger(HdfsFileSystemViewTest.class);
  private static MiniDFSCluster CLUSTER;
  private static Configuration CONF;
  private static DistributedFileSystem DFS;

  private static User USER;
  private static HdfsUser HDFSUSER;
  private final static String DEFAULT_SUPERUSER = "superuser";
  private final static String DEFAULT_NAME = "user";
  private final static String DEFAULT_PASSWORD = "pwd";
  private final static Authority[] DEFAULT_AUTHORITIES = null;
  private final static int DEFAULT_MAXIDLETIME = 123;
  private final static String DEFAULT_HOME = "/home";
  private final static boolean DEFAULT_ENABLE = false;

  private final static String DEFAULT_FILE_PATH = "/home/file.txt";
  private final static String DEFAULT_DIR_PATH = "/home";
  private final static FsPermission DEFAULT_PERMISSION = new FsPermission((short) 1023);

  @BeforeClass
  public static void setUpClass() throws IOException {
    LOG.info("Start test HdfsFileSystemView.java");
    USER = Mockito.mock(User.class);
    Mockito.when(USER.getName()).thenReturn(DEFAULT_NAME);
    Mockito.when(USER.getPassword()).thenReturn(DEFAULT_PASSWORD);
    Mockito.when(USER.getAuthorities()).thenReturn(DEFAULT_AUTHORITIES);
    Mockito.when(USER.getMaxIdleTime()).thenReturn(DEFAULT_MAXIDLETIME);
    Mockito.when(USER.getHomeDirectory()).thenReturn(DEFAULT_HOME);
    Mockito.when(USER.getEnabled()).thenReturn(DEFAULT_ENABLE);
    HDFSUSER = new HdfsUser(USER);

    LOG.info("Create MiniDFSCluster.");
    CONF = new HdfsConfiguration();
    CLUSTER = new MiniDFSCluster.Builder(CONF).build();
    HdfsOverFtpSystem.setSuperuser(DEFAULT_SUPERUSER);
    HdfsOverFtpSystem.setHDFS_URI(CLUSTER.getURI().toString());
    DFS = HdfsOverFtpSystem.getDfs();
    DFS.create(new Path(DEFAULT_FILE_PATH));
    DFS.setPermission(new Path(DEFAULT_FILE_PATH), DEFAULT_PERMISSION);
  }

  @AfterClass
  public static void tearDownClass() throws IOException {
    if (CLUSTER != null) {
      LOG.info("Closing MiniDFSCluster");
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
    LOG.info("Start testGetHomeDirectory");
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
    LOG.info("Start testGetFileObject");
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
    LOG.info("Start testChangeANDGetCurrnentDirectory");
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
    LOG.info("Start testIsRandomAccessible");
    HdfsFileSystemView instance = new HdfsFileSystemView(HDFSUSER, true);
    assertTrue(instance.isRandomAccessible());
  }
}
