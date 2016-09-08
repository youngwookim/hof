package com.spright.hof;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.ftpserver.FtpServerConfigurationException;
import org.apache.ftpserver.ftplet.FileSystemFactory;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;

import org.apache.ftpserver.util.BaseProperties;
import org.apache.ftpserver.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Impelented FileSystemManager to use HdfsFileSystemView
 */
public class HdfsFileSystemManager implements FileSystemFactory {

  private BaseProperties userDataProp;
  private File userDataFile;
  private final static String PREFIX = "ftpserver.user.";
  private final static String ATTR_GROUPS = "groups";
  private final Logger LOG = LoggerFactory
          .getLogger(HdfsFileSystemManager.class);

  public HdfsFileSystemManager(File userDataFile) {
    if (!userDataFile.exists()) {
      System.out.println("Usage: userDataFile not exist.");
      System.exit(0);
    }
    loadFromFile(userDataFile);
    this.userDataFile = userDataFile;
  }

  public FileSystemView createFileSystemView(User user) throws FtpException {
    HdfsUser hdfsUser = new HdfsUser(user);
    hdfsUser.setGroups(getGroupsArray(user.getName()));
    return new HdfsFileSystemView(hdfsUser);
  }

  private void loadFromFile(File userDataFile) {
    try {
      userDataProp = new BaseProperties();

      if (userDataFile != null) {
        LOG.debug("File configured, will try loading");

        if (userDataFile.exists()) {
          this.userDataFile = userDataFile;

          LOG.debug("File found on file system");
          FileInputStream fis = null;
          try {
            fis = new FileInputStream(userDataFile);
            userDataProp.load(fis);
          } finally {
            IoUtils.close(fis);
          }
        } else {
          // try loading it from the classpath
          LOG
                  .debug("File not found on file system, try loading from classpath");

          InputStream is = getClass().getClassLoader()
                  .getResourceAsStream(userDataFile.getPath());

          if (is != null) {
            try {
              userDataProp.load(is);
            } finally {
              IoUtils.close(is);
            }
          } else {
            throw new FtpServerConfigurationException(
                    "User data file specified but could not be located, "
                    + "neither on the file system or in the classpath: "
                    + userDataFile.getPath());
          }
        }
      }
    } catch (IOException e) {
      throw new FtpServerConfigurationException(
              "Error loading user data file : " + userDataFile, e);
    }
  }

  /**
   * Load user data.
   */
  public ArrayList<String> getGroupsArray(String userName) {
    String baseKey = PREFIX + userName + '.';
    String groupsLine = userDataProp.getProperty(baseKey + ATTR_GROUPS, "/");
    ArrayList<String> groups = parseGroups(groupsLine);
    return groups;
  }

  private ArrayList<String> parseGroups(String groupsLine) {
    String groupsArray[] = groupsLine.split(",");
    return new ArrayList(Arrays.asList(groupsArray));
  }

}
