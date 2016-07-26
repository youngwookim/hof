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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HdfsOverFtpSystemTest {

  private static final Logger LOG = LoggerFactory.getLogger(HdfsOverFtpSystemTest.class);
  private static MiniDFSCluster CLUSTER;
  private static Configuration CONF;
  private static URI HDFS_URI;

  private static final String DEFAULT_FILE_PATH = "/home/file.txt";
  private static final FsPermission DEFAULT_PERMISSION = new FsPermission((short) 1023);

  @BeforeClass
  public static void setUpClass() throws IOException {
    LOG.info("Start test HdfsOverFtpSystem.java");
    LOG.info("Create MiniDFSCluster.");
    CONF = new HdfsConfiguration();
    CLUSTER = new MiniDFSCluster.Builder(CONF).build();
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
   * Test of getDfs method, of class HdfsOverFtpSystem.
   */
  @Test
  public void testGetDfs() throws IOException {
    LOG.info("Start testGetDfs");
    HDFS_URI = CLUSTER.getURI();
    HdfsOverFtpSystem.setSuperuser("superuser");
    HdfsOverFtpSystem.setHDFS_URI(HDFS_URI.toString());

    DistributedFileSystem dfs1 = HdfsOverFtpSystem.getDfs();
    Path path = new Path(DEFAULT_FILE_PATH);
    dfs1.create(path);
    dfs1.setPermission(path, DEFAULT_PERMISSION);

    assertTrue(dfs1.exists(path));
    dfs1.delete(path, false);

    DistributedFileSystem dfs2 = HdfsOverFtpSystem.getDfs();
    assertEquals(dfs1, dfs2);
  }

  /**
   * Test of setHDFS_URI method, of class HdfsOverFtpSystem.
   */
  @Test
  public void testSetHDFS_URI() {
    LOG.info("Start testSetHDFS_URI");
    String sourceURI = HdfsOverFtpSystem.HDFS_URI;
    HdfsOverFtpSystem.setHDFS_URI("URI");
    String resultURI = HdfsOverFtpSystem.HDFS_URI;
    HdfsOverFtpSystem.setHDFS_URI(sourceURI);
    assertEquals("URI", resultURI);
  }
}
