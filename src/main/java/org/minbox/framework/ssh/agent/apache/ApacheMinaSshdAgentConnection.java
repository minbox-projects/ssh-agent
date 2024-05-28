package org.minbox.framework.ssh.agent.apache;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.session.forward.ExplicitPortForwardingTracker;
import org.apache.sshd.common.forward.PortForwardingEventListener;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionHeartbeatController;
import org.apache.sshd.common.signature.BuiltinSignatures;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.minbox.framework.ssh.agent.AgentConnection;
import org.minbox.framework.ssh.agent.AgentException;
import org.minbox.framework.ssh.agent.config.AgentConfig;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

/**
 * 基于Apache mina sshd实现ssh通道的端口转发
 * <p>
 * GitHub：<a href="https://github.com/apache/mina-sshd">mina-sshd</a>
 *
 * @author 恒宇少年
 * @since 1.0.4
 */
@Slf4j
public class ApacheMinaSshdAgentConnection implements AgentConnection {
    private static final String FORWARD_LOCAL_IP = "127.0.0.1";
    private final AgentConfig config;
    private ClientSession clientSession;
    private SshClient client;
    private SshdSocketAddress sshdSocketAddress;

    public ApacheMinaSshdAgentConnection(AgentConfig config) {
        this.config = config;
        if (config == null) {
            throw new AgentException("The AgentConfig cannot be null.");
        }
    }

    @Override
    public void connect() throws AgentException {
        try {
            client = SshClient.setUpDefaultClient();
            client.start();
            clientSession = client.connect(config.getUsername(), config.getServerIp(), config.getSshPort()).verify().getSession();
            boolean result = clientSession.auth().verify().await();
            if (!result) {
                throw new AgentException("Remote server ssh login failed.");
            } else {
                log.info("Connection to the remote server [{}({}):{}] is successful.", config.getServerIp(), config.getSshPort(), config.getForwardTargetPort());
                // Local and remote port number binding forwarding
                sshdSocketAddress = clientSession.startLocalPortForwarding(new SshdSocketAddress(FORWARD_LOCAL_IP, config.getLocalPort()),
                        new SshdSocketAddress(config.getForwardTargetIp(), config.getForwardTargetPort()));
                log.info("Port forwarding binding is completed, local port : {}, forward IP: {}, forward port : {}",
                        config.getLocalPort(), config.getForwardTargetIp(), config.getForwardTargetPort());
            }
        } catch (Exception e) {
            throw new AgentException("Remote server port number forwarding failed.", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            if (client != null) {
                client.stop();
                if (clientSession != null) {
                    if (sshdSocketAddress != null) {
                        clientSession.stopLocalPortForwarding(sshdSocketAddress);
                    }
                    clientSession.close();
                }
            }
        } catch (Exception e) {
            throw new AgentException("Failed to close remote server port number forwarding.", e);
        }
    }
}
