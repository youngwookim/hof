package com.spright.hof;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystemException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FileObject;
import org.apache.ftpserver.ftplet.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
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

/**
 *
 * @author dslab
 */
public class HdfsFileObjectTest {

  private final static String DEFAULT_NAME = "user";
  private final static String DEFAULT_PASSWORD = "pwd";
  private final static Authority[] DEFAULT_AUTHORITIES = null;
  private final static int DEFAULT_MAXIDLETIME = 123;
  private final static String DEFAULT_HOME = "/home";
  private final static boolean DEFAULT_ENABLE = false;

  private static Path PATH;
  private static User USER;
  private static HdfsUser HDFSUSER;
  private static MiniDFSCluster CLUSTER;
  private static Configuration CONF;
  private static DistributedFileSystem DFS;

  private final static String DEFAULT_FILE_PATH = "/home/file.txt";
  private final static String DEFAULT_DIR_PATH = "/home";
  private final static FsPermission DEFAULT_PERMISSION = new FsPermission((short) 1023);

  public HdfsFileObjectTest() {
  }

  @BeforeClass
  public static void setUpClass() throws IOException {
    System.out.println("Start setUpClass");

    USER = Mockito.mock(User.class);
    Mockito.when(USER.getName()).thenReturn(DEFAULT_NAME);
    Mockito.when(USER.getPassword()).thenReturn(DEFAULT_PASSWORD);
    Mockito.when(USER.getAuthorities()).thenReturn(DEFAULT_AUTHORITIES);
    Mockito.when(USER.getMaxIdleTime()).thenReturn(DEFAULT_MAXIDLETIME);
    Mockito.when(USER.getHomeDirectory()).thenReturn(DEFAULT_HOME);
    Mockito.when(USER.getEnabled()).thenReturn(DEFAULT_ENABLE);
    HDFSUSER = new HdfsUser(USER);

    System.out.println("Start Create MiniDFSCluster");
    CONF = new HdfsConfiguration();
    MiniDFSCluster cluster = new MiniDFSCluster.Builder(CONF).build();
    DFS = cluster.getFileSystem();
    DFS.create(new Path(DEFAULT_FILE_PATH));
    DFS.setPermission(new Path(DEFAULT_FILE_PATH), DEFAULT_PERMISSION);
    //expect HdfsOverFtpSystem.getDfs() will return DFS.
    HdfsOverFtpSystem.setDfs(DFS);

    PATH = new Path(DEFAULT_DIR_PATH);
  }

  @AfterClass
  public static void tearDownClass() throws IOException {
    System.out.println("Start tearDownClass");
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
   * Test of getFullName method, of class HdfsFileObject.
   */
  @Test
  public void testGetFullName() {
    System.out.println("Start testGetFullName");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertEquals(DEFAULT_DIR_PATH, instance.getFullName());
  }

  /**
   * Test of getShortName method, of class HdfsFileObject.
   */
  @Test
  public void testGetShortName() {
    System.out.println("Start testGetShortName");
    HdfsFileObject instance = new HdfsFileObject("/", HDFSUSER);
    assertEquals("/", instance.getShortName());

    HdfsFileObject instance2 = new HdfsFileObject("123/test.txt", HDFSUSER);
    assertEquals("test.txt", instance2.getShortName());
  }

  /**
   * Test of isHidden method, of class HdfsFileObject.
   */
  @Test
  public void testIsHidden() {
    System.out.println("Start testIsHidden");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertFalse(instance.isHidden());
  }

  /**
   * Test of isDirectory method, of class HdfsFileObject.
   */
  @Test
  public void testIsDirectory() throws IOException {
    System.out.println("Start testIsDirectory");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertTrue(instance.isDirectory());

    HdfsFileObject instance2 = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    assertFalse(instance2.isDirectory());
  }

  /**
   * Test of isFile method, of class HdfsFileObject.
   */
  @Test
  public void testIsFile() {
    System.out.println("Start testIsFile");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertFalse(instance.isFile());

    HdfsFileObject instance2 = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    assertTrue(instance2.isFile());
  }

  /**
   * Test of doesExist method, of class HdfsFileObject.
   */
  @Test
  public void testDoesExist() {
    System.out.println("Start testDoesExist");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertTrue(instance.doesExist());

    HdfsFileObject instance2 = new HdfsFileObject("/123/456/", HDFSUSER);
    assertFalse(instance2.doesExist());
  }

  /**
   * Test of hasReadPermission method, of class HdfsFileObject.
   */
  @Test
  public void testHasReadPermission() throws IOException {
    System.out.println("Start testHasReadPermission");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertTrue(instance.hasReadPermission());
    HdfsFileObject instance2 = new HdfsFileObject("..", HDFSUSER);
    assertFalse(instance2.hasReadPermission());
  }

  /**
   * Test of hasWritePermission method, of class HdfsFileObject.
   */
  @Test
  public void testHasWritePermission() {
    System.out.println("Start testHasWritePermission");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    assertTrue(instance.hasWritePermission());
    HdfsFileObject instance2 = new HdfsFileObject("/", HDFSUSER);
    assertFalse(instance2.hasWritePermission());
  }

  /**
   * Test of hasDeletePermission method, of class HdfsFileObject.
   */
  @Test
  public void testHasDeletePermission() {
    System.out.println("Start testHasDeletePermissionn");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    boolean expResult = instance.hasWritePermission();
    assertEquals(expResult, instance.hasDeletePermission());
  }

  /**
   * Test of getOwnerName method, of class HdfsFileObject.
   */
  @Test
  public void testGetOwnerName() throws IOException {
    System.out.println("Start testGetOwnerName");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    FileStatus fs = DFS.getFileStatus(PATH);
    assertEquals(fs.getOwner(), instance.getOwnerName());
  }

  /**
   * Test of getGroupName method, of class HdfsFileObject.
   */
  @Test
  public void testGetGroupName() throws IOException {
    System.out.println("Start testgetGroupName");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    FileStatus fs = DFS.getFileStatus(PATH);
    assertEquals(fs.getGroup(), instance.getGroupName());
  }

  /**
   * Test of getLinkCount method, of class HdfsFileObject.
   */
  @Test
  public void testGetLinkCount() {
    System.out.println("Start testGetLinkCount");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertEquals(3, instance.getLinkCount());

    HdfsFileObject instance2 = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    assertEquals(1, instance2.getLinkCount());
  }

  /**
   * Test of getLastModified method, of class HdfsFileObject.
   */
  @Test
  public void testGetLastModified() throws IOException {
    System.out.println("Start testGetLastModified");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    FileStatus fs = DFS.getFileStatus(PATH);
    assertEquals(fs.getModificationTime(), instance.getLastModified());
  }

  /**
   * Test of getSize method, of class HdfsFileObject.
   */
  @Test
  public void testGetSize() throws IOException {
    System.out.println("Start testGetSize");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    FileStatus fs = DFS.getFileStatus(PATH);
    assertEquals(fs.getLen(), instance.getSize());
  }

  /**
   * Test of mkdir method and delete method, of class HdfsFileObject.
   */
  @Test
  public void testMkdirAndDelete() throws FileSystemException, IOException{
    System.out.println("Start testMkdirAndDelete");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    assertFalse(instance.mkdir());

    DFS.setPermission(new Path(DEFAULT_DIR_PATH), DEFAULT_PERMISSION);
    HdfsFileObject instance2 = new HdfsFileObject("/home/123/", HDFSUSER);
    assertTrue(instance2.mkdir());
    assertTrue(instance2.doesExist());
    instance2.delete();
    assertFalse(instance2.doesExist());
  }


  /**
   * Test of move method, of class HdfsFileObject.
   */
  @Test
  public void testMove() {
    System.out.println("Start testMove");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    String testpath = "/testmove.txt";
    FileObject fileObject = new HdfsFileObject(testpath, HDFSUSER);
    assertTrue(instance.move(fileObject));
    assertFalse(instance.doesExist());
    HdfsFileObject instance2 = new HdfsFileObject(testpath, HDFSUSER);
    FileObject fileObject2 = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    instance2.move(fileObject2);
  }

  /**
   * Test of listFiles method, of class HdfsFileObject.
   */
  @Test
  public void testListFiles() {
    System.out.println("Start testListFiles");
    HdfsFileObject instance = new HdfsFileObject("..", HDFSUSER);
    assertNull(instance.listFiles());
    HdfsFileObject instance2 = new HdfsFileObject(DEFAULT_DIR_PATH, HDFSUSER);
    FileObject[] result = instance2.listFiles();
    assertEquals("file.txt", result[0].getShortName());
  }

  /**
   * Test of createOutputStream and createInputStream method, of class HdfsFileObject.
   */
  @Test
  public void testCreateOutputStreamAndInputStream() throws Exception {
    System.out.println("Start testCreateOutputStreamAndInputStream");
    HdfsFileObject instance = new HdfsFileObject(DEFAULT_FILE_PATH, HDFSUSER);
    OutputStream out = instance.createOutputStream(1);
    out.write(6);
    out.close();
    InputStream in = instance.createInputStream(1);
    assertEquals(6, in.read());
    in.close();
  }

}
