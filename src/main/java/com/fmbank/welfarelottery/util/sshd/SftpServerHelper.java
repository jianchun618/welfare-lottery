package com.fmbank.welfarelottery.util.sshd;

import com.fmbank.welfarelottery.util.sshd.bean.CommandRes;
import com.fmbank.welfarelottery.util.sshd.bean.SftpClientAndSession;
import com.fmbank.welfarelottery.util.sshd.crypt.Crypt;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: SftpServerHelper
 * @Description: Sftp 服务器 操作助手类
 * @author: jianchun
 * @date: 2022/8/18 11:38
 */
public final class SftpServerHelper {
    private static final Logger logger = LoggerFactory.getLogger(SftpServerHelper.class);

    private SftpServerHelper(ClientSession session, long timeoutMillis) {
        this.session = session;
        this.timeoutMillis = timeoutMillis;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (clientCache.isEmpty()) {
                    return;
                }

                for (Map.Entry<String, SftpClientAndSession> entry : clientCache.entrySet()) {
                    String key = entry.getKey();
                    SftpClientAndSession temp = entry.getValue();
                    try {
                        temp.getSession().close();
                    } catch (IOException e) {
                        logger.error(String.format("关闭[%s]的SshSession发生异常", key), e);
                    }

                    try {
                        temp.getClient().stop();
                    } catch (Exception e) {
                        logger.error(String.format("关闭[%s]的SshClient发生异常", key), e);
                    }
                }
            }
        });
    }

    private ClientSession session = null;
    private long timeoutMillis = 30000;
    private String sftpBaseDataDir = "/home/sftp/data/";

    private static Map<String, SftpClientAndSession> clientCache = new ConcurrentHashMap<>();

    public static SftpServerHelper instance(String username, String password,
                                            String host, int port) {
        return instance(username, password,
                host, port, 30000);
    }

    public static SftpServerHelper instance(String username, String password,
                                            String host, int port, long timeoutMillis) {
        ClientSession session = openSession(username, password, host, port, timeoutMillis);
        if (null == session) {
            return null;
        }

        return new SftpServerHelper(session, timeoutMillis);
    }

    /**
     * SftpServer连通性测试
     *
     * @param username
     * @param password
     * @param host
     * @param port
     * @param timeoutMillis
     * @return
     */
    public final boolean canConnect(String username, String password,
                                    String host, int port, long timeoutMillis) {
        ClientSession clientSession = openSession(username, password, host, port, timeoutMillis);

        if (null == clientSession)
            return false;

        CommandRes lsRes = exec("ls");

        return lsRes.isSuccess();
    }

    public final boolean canConnect() {

        CommandRes lsRes = exec("ls");

        return lsRes.isSuccess();
    }

    public final boolean canConnectSudo() {

        CommandRes lsRes = exec("ls");

        return lsRes.isSuccess();
    }

    /**
     * 新增ro用户
     *
     * @param group
     * @param userName
     * @param password
     * @return
     */
    public final boolean createRoUser(String group, String userName
            , String password, String dataPushDir) {
        // 1、创建用户分组
        if (!createUserGroup(group))
            return false;

        // 2、创建ro用户 并设置密码
        if (!createRoUserInfo(group, userName, password, dataPushDir)) {
            return false;
        }
        return true;
    }

    /**
     * 新增rw用户
     *
     * @param group
     * @param userName
     * @param password
     * @param dataPushDir
     * @return
     */
    public final boolean createRwUser(String group, String userName
            , String password, String dataPushDir) {
        // 1、创建用户分组
        if (!createUserGroup(group))
            return false;


        // 2、创建文件夹
        String createDataDir = String.format("mkdir -p %s", dataPushDir);
        CommandRes createDataDirRes = exec(createDataDir);
        if (!createDataDirRes.isSuccess()) {
            return false;
        }

        // 3、创建rw用户 并设置密码
        if (!createRwUserInfo(group, userName, password, dataPushDir)) {
            return false;
        }

        setPasswdX(userName);

        // 4、修改文件归属
        String chown = String.format("chown %s:%s %s", userName, group, dataPushDir);
        CommandRes chownRes = exec(chown);
        if (!chownRes.isSuccess()) {
            return false;
        }

        // 5、修改文件授权
        String chmod = String.format("chmod 750 %s", dataPushDir);
        CommandRes chmodRes = exec(chmod);

        return chmodRes.isSuccess();
    }

    public final boolean dirAuto(String group, String userName, String dataPushDir) {
        boolean dataDir = createDataDir(dataPushDir);
        if (!dataDir) {
            return false;
        }

        // 修改文件归属
        String chown = String.format("chown %s:%s %s", userName, group, dataPushDir);
        CommandRes chownRes = exec(chown);
        if (!chownRes.isSuccess()) {
            logger.info("设置文件归属[{},{}:{}]失败.[{}]", dataPushDir, userName, group, chownRes.toString());
            return false;
        }

        // 修改文件授权
        String chmod = String.format("chmod 750 %s", dataPushDir);
        CommandRes chmodRes = exec(chmod);
        if (!chmodRes.isSuccess()) {
            logger.info("设置文件权限失败[{}，750]失败.[{}]", dataPushDir, userName, group, chmodRes.toString());
            return false;
        }

        return true;
    }

    public final boolean createDataDir(String dataDir) {
        String createDataDir = String.format("mkdir -p %s", dataDir);
        CommandRes createDataDirRes = exec(createDataDir);
        if (createDataDirRes.isSuccess() ||
                (null != createDataDirRes.getErr())
                        && createDataDirRes.getErr().contains("File exists")) {
            logger.info("已完成目录[{}]的创建.", dataDir);
            return true;
        }

        logger.error("创建目录[{}]失败.[{}]", dataDir, createDataDirRes.getErr());
        return false;

    }


    /**
     * @param userName
     * @param newUserName
     * @param password
     * @return
     */
    public final boolean modUser(String userName, String newUserName, String password, String dataPushDir) {
        String crypted = cryptedPwd(password);
        String modUserCommand =
                String.format("usermod -l %s %s -p %s -d %s", newUserName, userName, crypted, dataPushDir);
        CommandRes addUserRes = exec(modUserCommand);

        if (addUserRes.isSuccess()) {
            return true;
        }

        logger.error("修改用户[{}]失败.[{}]", userName, addUserRes.toString());
        return false;
    }

    /**
     * 创建用户
     *
     * @param group
     * @param userName
     * @param password
     * @return
     */
    public final boolean createRoUserInfo(String group, String userName, String password, String dataPushDir) {
        String crypted = cryptedPwd(password);

        String addUserCommand =
                String.format("useradd %s -p %s -d %s", userName, crypted, dataPushDir);
        CommandRes addUserRes = exec(addUserCommand);

        boolean result = addUserRes.isSuccess() || (null != addUserRes.getErr() && addUserRes.getErr().contains("already exists"));
        if (result) {
            modUserGroup(userName, group);
        }

        if (!result) {
            logger.error("创建用户[{}, {}, {}]失败.[{}]", group, userName, dataPushDir, addUserRes.toString());
        }

        return result;
    }

    /**
     * 创建用户
     *
     * @param group
     * @param userName
     * @param password
     * @return
     */
    public final boolean createRwUserInfo(String group, String userName, String password, String dataPushDir) {
        String crypted = cryptedPwd(password);
        String addUserCommand =
                String.format("useradd %s -g %s -p %s -d %s", userName, group, crypted, dataPushDir);
        CommandRes addUserRes = exec(addUserCommand);

        if (addUserRes.isSuccess() ||
                (null != addUserRes.getErr())
                        && addUserRes.getErr().contains("already exists")) {
            logger.info("已完成完成用户[{}, {}, {}]的创建.", group, userName, dataPushDir);
            return true;
        }

        logger.error("创建用户[{}, {}, {}]失败.[{}]", group, userName, dataPushDir, addUserRes.getErr());
        return false;
    }

    /**
     * 创建用户分组
     *
     * @param group
     * @return
     */
    public final boolean createUserGroup(String group) {
        String addGroupCommand = String.format("groupadd %s", group);
        CommandRes addGroupRes = exec(addGroupCommand);
        if (addGroupRes.isSuccess() ||
                (null != addGroupRes.getErr())
                        && addGroupRes.getErr().contains("already exists")) {
            logger.info("已完成完成用户分组[{}]的创建.", group);
            return true;
        }

        logger.error("创建用户组[{}]失败.[{}]", group, addGroupRes.toString());
        return false;
    }

    /**
     * 添加用户到组
     *
     * @param group
     * @return
     */
    public final boolean modUserGroup(String userName, String group) {
        String modUserGroup = String.format("usermod -a -G %s %s", group, userName);
        CommandRes addGroupRes = exec(modUserGroup);
        if (addGroupRes.isSuccess()) {
            logger.info("已完成完成用户[{}]添加到组[{}]的操作.", userName, group);
            return true;
        }

        logger.error("添加用户[{}]到组[{}]失败.[{}]", userName, group, addGroupRes.toString());
        return false;
    }

    public final boolean alreadyExistsUser(String userName) {
        String alreadyExist = String.format("id %s", userName);
        CommandRes alreadyExistRes = exec(alreadyExist);
        if (alreadyExistRes.isSuccess()) {
            return true;
        }

        logger.error("用户[{}]不存在.[{}]", userName, alreadyExistRes.toString());
        return false;
    }


    /**
     * 用户锁定-停用
     *
     * @param userName
     * @return
     */
    public final boolean lockUser(String userName) {
        String lock = String.format("passwd -l %s", userName);
        CommandRes lockRes = exec(lock);
        if (lockRes.isSuccess()) {
            return true;
        }
        if (lockRes.getErr() != null && lockRes.getErr().indexOf("Unknown user name") > -1) {
            logger.info("用户 '{}' 不在在 '{}'", userName);
            return true;
        }

        return false;
    }

    /**
     * 删除用户
     *
     * @param userName
     * @return
     */
    public boolean deleteUser(String userName) {
        String hasDir = String.format("userdel %s", userName);
        CommandRes deleteRes = exec(hasDir);
        if (deleteRes.isSuccess() ||
                (deleteRes.getErr() != null && deleteRes.getErr().indexOf("does not exist") > -1)) {
            return true;
        }
        logger.error("用户[{}]删除失败.[{}]", userName, deleteRes.toString());
        return false;
    }


    public final boolean setPasswdX(String userName) {
        String passwdX = String.format("passwd -x 99999 %s", userName);
        CommandRes passwdXRes = exec(passwdX);
        if (passwdXRes.isSuccess()) {
            logger.info("已完成用户[{}]密码永久有效设置.", userName);
            return true;
        }
        logger.error("设置用户[{}]密码永久有效失败.[{}]", userName, passwdXRes.toString());
        return false;
    }

    /**
     * 删除用户
     *
     * @param userName
     * @return
     */
    public boolean gpasswd(String userName, String group) {
        String gpasswd = String.format("gpasswd -d %s %s", userName, group);
        CommandRes gpasswdRes = exec(gpasswd);
        if (gpasswdRes.isSuccess()) {
            return true;
        }
        if (gpasswdRes.getErr() != null && gpasswdRes.getErr().indexOf("is not a member of") > -1) {
            logger.info("用户 '{}' 不在该组 '{}'", userName, group);
            return true;
        }

        logger.error("用户[{}]移除组[{}]失败.[{}]", userName, group, gpasswdRes.toString());
        return false;
    }

    /**
     * 目录是否存在
     *
     * @param fullDirPath
     * @return
     */
    public boolean hasDir(String fullDirPath) {
        String hasDir = String.format("cd %s", fullDirPath);
        CommandRes hasDirRes = exec(hasDir);
        return hasDirRes.isSuccess();
    }


    /**
     * 是否root权限
     *
     * @param userName
     * @return
     */
    public boolean isRootRight(String userName) {
        String cmd = "id " + userName;
        CommandRes commandRes = exec(cmd);
        if (!commandRes.isSuccess()) {
            return false;
        }
        String resOut = commandRes.getOut();
        return resOut.indexOf("uid=0") > -1 && resOut.indexOf("gid=0") > -1 && resOut.indexOf("groups=0") > -1;
    }


    /**
     * 目录下文件最新时间 格式 "yyyy-MM-DD HH:mm:ss"
     *
     * @param dirPath
     * @return
     */
    public Date lastedDateTime(String dirPath) {
        String lastedTime = String.format("ls -l %s --full-time", dirPath);
        CommandRes lastedTimeRes = exec(lastedTime);

        if (!lastedTimeRes.isSuccess()) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date lastedDateTime = null;
        String[] outLines = lastedTimeRes.getOut().toString().split("\n");
        for (String outLine : outLines) {
            if (outLine.startsWith("total")) {
                continue;
            }

            try {
                List<String> wordList = splitOutLine(outLine);
                logger.info("从ssh结果解析到的行数据为{}", wordList);
                String dateStr = wordList.get(5) + " " + wordList.get(6).substring(0, 8);
                logger.info("时间字符串为{}", dateStr);
                Date temp = sdf.parse(dateStr);
                if (null == lastedDateTime || lastedDateTime.before(temp)) {
                    lastedDateTime = temp;
                }
            } catch (Exception e) {
                logger.error("解析最新时间发生异常", e);
            }
        }

        return lastedDateTime;
    }

    private List<String> splitOutLine(String outLine) {
        List<String> wordList = new ArrayList<>();
        char[] chars = outLine.toCharArray();
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 32 && index <= i) {
                wordList.add(outLine.substring(index, i));
                index = i + 1;
                while (chars[index] == 32) {
                    index++;
                }
            }
        }
        return wordList;
    }

    private String cryptedPwd(String pwd) {
        String salt = "$6$7yBiyb9Y$";

        String crypted = null;
        try {
            crypted = Crypt.crypt(pwd.toCharArray(), salt);
            crypted = crypted.replace("$", "\\$");
        } catch (Exception e) {
            logger.error("加密失败", e);
        }

        return crypted;
    }

    private CommandRes exec(String command) {
        CommandRes commandRes = new CommandRes();

        try (ChannelExec ce = session.createExecChannel(command)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            ce.setOut(out);
            ce.setErr(err);
            ce.open();

            Set<ClientChannelEvent> events =
                    ce.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(timeoutMillis));

            // Check if timed out
            if (events.contains(ClientChannelEvent.TIMEOUT)) {
                commandRes.setTimeOut(true);
            } else {
                commandRes.setSuccess(true);
                commandRes.setErr(err.toString());
                commandRes.setOut(out.toString());
                commandRes.setExitStatus(ce.getExitStatus());
            }
            logger.info("命令[{}]的执行结果为[{}]", command, commandRes);
        } catch (IOException e) {
            logger.error("执行命令异常" + command, e);
        }

        return commandRes;
    }

    private static String createKey(String username, String password,
                                    String host, int port) {
        return String.format("%s:%s@%s:%d", username, password, host, port);
    }

    private static ClientSession openSession(String username, String password,
                                             String host, int port, long timeoutMillis) {
        String msg = String.format("打开连接[用户名%s, 主机名%s, 端口%d, 超时%d]", username, host, port, timeoutMillis);

        String key = createKey(username, password, host, port);

        SftpClientAndSession temp = clientCache.get(key);

        boolean open = false;
        if (temp == null) {
            open = true;
        } else {
            ClientSession clientSession = temp.getSession();
            if (!clientSession.isOpen()) {
                clientCache.remove(key);
                open = true;
            }
        }

        try {

            if (open) {
                synchronized (SftpServerHelper.class) {
                    SshClient client = SshClient.setUpDefaultClient();
                    client.start();

                    ClientSession session =
                            client.connect(username, host, port)
                                    .verify(timeoutMillis)
                                    .getSession();
                    session.addPasswordIdentity(password); // for password-based authentication
                    session.auth().verify(timeoutMillis);

                    logger.info(msg);

                    temp = new SftpClientAndSession(client, session);

                    clientCache.put(key, temp);
                }
            }

            return temp.getSession();
        } catch (IOException e) {
            logger.error(msg + " 失败.", e);
        }

        return null;
    }
    public static void main(String[] args) throws Exception {

        //String decrypt = AESUtil.decrypt("h9ax41m1Ni4K41VrP3sYMw==", "1bb95c2609e31b6c875efe65a4bd0785");

        SftpServerHelper instance = SftpServerHelper.instance("root", "123456", "192.168.134.8", 22);
        //  instance.gpasswd("testadd8", "testx5");\
        //  instance.alreadyExistsUser("testx50000");
        System.out.println(instance.hasDir("/opt"));

    }

}
