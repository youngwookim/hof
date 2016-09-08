package com.spright.hof;

import org.apache.ftpserver.ftplet.FtpFile;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;

/**
 * Implemented FileSystemView to use HdfsFileObject
 */
public class HdfsFileSystemView implements FileSystemView {

  // the root directory will always end with '/'.
  private String rootDir = "/";

  // the first and the last character will always be '/'
  // It is always with respect to the root directory.
  private String currDir = "/";

  private HdfsUser user;

  // private boolean writePermission;
  private boolean caseInsensitive = false;

  /**
   * Constructor - set the user object.
   */
  protected HdfsFileSystemView(HdfsUser user) throws FtpException {
    this(user, true);
  }

  /**
   * Constructor - set the user object.
   */
  protected HdfsFileSystemView(HdfsUser user, boolean caseInsensitive)
          throws FtpException {
    if (user == null) {
      throw new IllegalArgumentException("user can not be null");
    }
    if (user.getHomeDirectory() == null) {
      throw new IllegalArgumentException(
              "User home directory can not be null");
    }

    this.caseInsensitive = caseInsensitive;

    // add last '/' if necessary
    String rootDir = user.getHomeDirectory();
    //  rootDir = NativeFileObject.normalizeSeparateChar(rootDir);
    if (!rootDir.endsWith("/")) {
      rootDir += '/';
    }
    this.rootDir = rootDir;
    this.user = user;
    this.currDir = rootDir;
  }

  /**
   * Get the user home directory. It would be the file system root for the user.
   */
  public FtpFile getHomeDirectory() {
    return new HdfsFileObject("/", user);
  }

  /**
   * Get the current directory.
   */
  public FtpFile getWorkingDirectory() {
    return new HdfsFileObject(currDir, user);
  }

  /**
   * Get file object.
   */
  public FtpFile getFile(String file) {
    String path;
    if (file.startsWith("/")) {
      path = file;
    } else if (currDir.length() > 1) {
      path = currDir + "/" + file;
    } else {
      path = "/" + file;
    }
    return new HdfsFileObject(path, user);
  }

  /**
   * Change directory.
   */
  public boolean changeWorkingDirectory(String dir) {
    String path;
    if (dir.startsWith("/")) {
      path = dir;
    } else if (currDir.length() > 1) {
      path = currDir + "/" + dir;
    } else {
      path = "/" + dir;
    }
    HdfsFileObject file = new HdfsFileObject(path, user);
    if (file.isDirectory() && file.isReadable()) {
      currDir = path;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Is the file content random accessible?
   */
  public boolean isRandomAccessible() {
    return true;
  }

  /**
   * Dispose file system view - does nothing.
   */
  public void dispose() {
  }
}
