package com.spright.hof;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.apache.ftpserver.ftplet.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import java.net.URISyntaxException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HdfsOverFtpSystem.class)
public class HdfsFileObjectTest {

    static String stringpath="/";
    static Path path;
    static User user;
    static HdfsUser hdfsuser;
    static DistributedFileSystem dfs;
    public HdfsFileObjectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException, URISyntaxException {
        System.out.println("setUpClass:");

        path = new Path(stringpath);

        user = Mockito.mock(User.class);
        Mockito.when(user.getName()).thenReturn("user");
        Mockito.when(user.getPassword()).thenReturn("pwd");
        Mockito.when(user.getAuthorities()).thenReturn(null);
        Mockito.when(user.getMaxIdleTime()).thenReturn(123);
        Mockito.when(user.getHomeDirectory()).thenReturn("/home");
        Mockito.when(user.getEnabled()).thenReturn(false);

        dfs = new DistributedFileSystem();
        Configuration conf = new Configuration();
        conf.set("testhadoop.job.ugi", "testhadoop.job.ugi,error,supergroup");
        //dfs.initialize(new URI("hdfs://localhost:5000"), conf);
        PowerMockito.mockStatic(HdfsOverFtpSystem.class);
        PowerMockito.when(HdfsOverFtpSystem.getDfs()).thenReturn(dfs);
        hdfsuser = new HdfsUser(user);
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
     * Test of getFullName method, of class HdfsFileObject.
     */
    @Test
    public void testGetFullName() {
        System.out.println("getFullName");
        HdfsFileObject instance;
        instance = new HdfsFileObject(stringpath,hdfsuser);
        assertEquals("/", instance.getFullName());
    }

    /**
     * Test of getShortName method, of class HdfsFileObject.
     */
    @Test
    public void testGetShortName() {
        System.out.println("getShortName");
        HdfsFileObject instance = new HdfsFileObject(stringpath,hdfsuser);
        assertEquals("/", instance.getShortName());
    }

    /**
     * Test of isHidden method, of class HdfsFileObject.
     */
    @Test
    public void testIsHidden() {
        System.out.println("isHidden");
        HdfsFileObject instance = new HdfsFileObject(stringpath,hdfsuser);
        assertEquals(false, instance.isHidden());
    }
}