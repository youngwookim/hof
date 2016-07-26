package com.spright.hof;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.RuleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

public class HdfsOverFtpServerTest {

  private static final Logger LOG = LoggerFactory.getLogger(HdfsUserManagerTest.class);

  public final SystemOutRule SYSTEMOUT_RULE = new SystemOutRule().enableLog();
  public final ExpectedSystemExit EXIT_RULE = ExpectedSystemExit.none();

  @Rule
  public RuleChain chain = RuleChain.outerRule(SYSTEMOUT_RULE).around(EXIT_RULE);

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
    String mainLog = SYSTEMOUT_RULE.getLog();
    assertEquals("Usage: <hdfs-over-ftp.properties> <user.properties>" + System.getProperty("line.separator"), mainLog);
  }

  /**
   * Test of main method, of class HdfsOverFtpServer.
   */
  @Test
  public void testOneArgsMain() throws Exception {
    LOG.info("Start testOneArgsMain");
    EXIT_RULE.expectSystemExit();
    String[] args1 = {"arg1"};
    HdfsOverFtpServer.main(args1);
  }

  /**
   * Test of main method, of class HdfsOverFtpServer.
   */
  @Test
  public void testThreeArgsMain() throws Exception {
    LOG.info("Start testThreeArgsMain");
    EXIT_RULE.expectSystemExit();
    String[] args1 = {"arg1", "arg2", "arg3"};
    HdfsOverFtpServer.main(args1);
  }
}
