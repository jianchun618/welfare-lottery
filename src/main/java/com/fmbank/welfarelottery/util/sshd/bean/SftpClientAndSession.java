package com.fmbank.welfarelottery.util.sshd.bean;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;

/**
 * @ClassName: SftpClientAndSession
 * @Description: 包上SftpClient和Session
 * @author: XiongHao
 * @date: 2022/8/19 16:49
 */
public class SftpClientAndSession {
    private SshClient client;
    private ClientSession session;

    public SftpClientAndSession(SshClient client, ClientSession session) {
        this.client = client;
        this.session = session;
    }

    public SshClient getClient() {
        return client;
    }

    public void setClient(SshClient client) {
        this.client = client;
    }

    public ClientSession getSession() {
        return session;
    }

    public void setSession(ClientSession session) {
        this.session = session;
    }
}
