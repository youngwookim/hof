package com.spright.hof;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class HdfsOverFtpSystemTest {
  private static MiniDFSCluster CLUSTER;
  private static Configuration CONF;
  private static URI HDFS_URI;

  private final static String DEFAULT_FILE_PATH = "/home/file.txt";
  private final static FsPermission DEFAULT_PERMISSION = new FsPermission((short) 1023);

  public HdfsOverFtpSystemTest() {
  }

  @BeforeClass
  public static void setUpClass() throws IOException {
    System.out.println("Starte Create MiniDFSCluster");
    CONF = new HdfsConfiguration();
    CLUSTER = new MiniDFSCluster.Builder(CONF).build();
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
   * Test of getDfs method, of class HdfsOverFtpSystem.
   */
  @Test
  public void testGetDfs() throws IOException {
    HDFS_URI = CLUSTER.getURI();
    System.out.println("HDFS_URI====="+HDFS_URI);
    HdfsOverFtpSystem.setSuperuser("superuser");
    HdfsOverFtpSystem.setHDFS_URI(HDFS_URI.toString());

    DistributedFileSystem dfs1 = HdfsOverFtpSystem.getDfs();
    Path path = new Path(DEFAULT_FILE_PATH);
    dfs1.create(path);
    dfs1.setPermission(path, DEFAULT_PERMISSION);

    assertTrue(dfs1.exists(path));
    dfs1.delete(path, false);

    DistributedFileSystem dfs2 = HdfsOverFtpSystem.getDfs();
    assertEquals(dfs1,dfs2);
  }

  /**
   * Test of setHDFS_URI method, of class HdfsOverFtpSystem.
   */
  @Test
  public void testSetHDFS_URI(){
    System.out.println("setHDFS_URI");
    String sourceURI = HdfsOverFtpSystem.HDFS_URI;
    HdfsOverFtpSystem.setHDFS_URI("URI");
    String resultURI = HdfsOverFtpSystem.HDFS_URI;
    HdfsOverFtpSystem.setHDFS_URI(sourceURI);
    assertEquals("URI",resultURI);
  }
}
