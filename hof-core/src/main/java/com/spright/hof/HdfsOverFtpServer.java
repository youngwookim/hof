package com.spright.hof;

import org.apache.ftpserver.DefaultDataConnectionConfiguration;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.interfaces.DataConnectionConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Start-up class of FTP server
 */
public class HdfsOverFtpServer {

  private static final Logger LOG = Logger.getLogger(HdfsOverFtpServer.class);

  private static int port = 0;
  private static int sslPort = 0;
  private static String passivePorts = null;
  private static String sslPassivePorts = null;
  private static String hdfsUri = null;

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage: <hdfs-over-ftp.properties> <user.properties>");
      System.exit(0);
    }
    File hdfsFile = new File(args[0]);
    File userFile = new File(args[1]);
    loadConfig(hdfsFile);

    if (port != 0) {
      startServer(userFile);
    }

    if (sslPort != 0) {
      startSSLServer();
    }
  }

  /**
   * Load configuration
   *
   * @throws IOException
   */
  private static void loadConfig(final File hdfsConfig) throws IOException {
    Properties props = new Properties();
    props.load(new FileInputStream(hdfsConfig));

    try {
      port = Integer.parseInt(props.getProperty("port"));
      LOG.info("port is set. ftp server will be started");
    } catch (Exception e) {
      LOG.info("port is not set. so ftp server will not be started");
    }

    try {
      sslPort = Integer.parseInt(props.getProperty("ssl-port"));
      LOG.info("ssl-port is set. ssl server will be started");
    } catch (Exception e) {
      LOG.info("ssl-port is not set. so ssl server will not be started");
    }

    if (port != 0) {
      passivePorts = props.getProperty("data-ports");
      if (passivePorts == null) {
        LOG.fatal("data-ports is not set");
        System.exit(1);
      }
    }

    if (sslPort != 0) {
      sslPassivePorts = props.getProperty("ssl-data-ports");
      if (sslPassivePorts == null) {
        LOG.fatal("ssl-data-ports is not set");
        System.exit(1);
      }
    }

    hdfsUri = props.getProperty("hdfs-uri");
    if (hdfsUri == null) {
      LOG.fatal("hdfs-uri is not set");
      System.exit(1);
    }

    String superuser = props.getProperty("superuser");
    if (superuser == null) {
      LOG.fatal("superuser is not set");
      System.exit(1);
    }
    HdfsOverFtpSystem.setSuperuser(superuser);
  }

  /**
   * Starts FTP server
   *
   * @param userFile
   * @throws Exception
   */
  public static void startServer(final File userFile) throws Exception {

    LOG.info(
            "Starting Hdfs-Over-Ftp server. port: " + port + " data-ports: " + passivePorts + " hdfs-uri: " + hdfsUri);

    HdfsOverFtpSystem.setHDFS_URI(hdfsUri);

    FtpServer server = new FtpServer();

    DataConnectionConfiguration dataCon = new DefaultDataConnectionConfiguration();
    dataCon.setPassivePorts(passivePorts);
    server.getListener("default").setDataConnectionConfiguration(dataCon);
    server.getListener("default").setPort(port);

    HdfsUserManager userManager = new HdfsUserManager();

    userManager.setFile(userFile);

    server.setUserManager(userManager);

    server.setFileSystem(new HdfsFileSystemManager());

    server.start();
  }

  /**
   * Starts SSL FTP server
   *
   * @throws Exception
   */
  public static void startSSLServer() throws Exception {

    LOG.info("Starting Hdfs-Over-Ftp SSL server. ssl-port: " + sslPort + " ssl-data-ports: " + sslPassivePorts + " hdfs-uri: " + hdfsUri);

    HdfsOverFtpSystem.setHDFS_URI(hdfsUri);

    FtpServer server = new FtpServer();

    DataConnectionConfiguration dataCon = new DefaultDataConnectionConfiguration();
    dataCon.setPassivePorts(sslPassivePorts);
    server.getListener("default").setDataConnectionConfiguration(dataCon);
    server.getListener("default").setPort(sslPort);

    MySslConfiguration ssl = new MySslConfiguration();
    ssl.setKeystoreFile(new File("ftp.jks"));
    ssl.setKeystoreType("JKS");
    ssl.setKeyPassword("333333");
    server.getListener("default").setSslConfiguration(ssl);
    server.getListener("default").setImplicitSsl(true);

    HdfsUserManager userManager = new HdfsUserManager();
    userManager.setFile(new File("users.conf"));

    server.setUserManager(userManager);

    server.setFileSystem(new HdfsFileSystemManager());

    server.start();
  }
}
