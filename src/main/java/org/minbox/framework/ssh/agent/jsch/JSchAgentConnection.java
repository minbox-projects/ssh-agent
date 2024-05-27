package org.minbox.framework.ssh.agent.jsch;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.minbox.framework.ssh.agent.AgentConnection;
import org.minbox.framework.ssh.agent.AgentException;
import org.minbox.framework.ssh.agent.config.AgentConfig;
import org.minbox.framework.ssh.agent.config.AuthenticationMethod;
import org.minbox.framework.ssh.agent.utils.StringUtils;

/**
 * The {@link AgentConnection} default support
 *
 * @author 恒宇少年
 */
@Slf4j
public class JSchAgentConnection implements AgentConnection {
    private static final String STRICT_HOST_KEY_CHECK = "StrictHostKeyChecking";
    private static final String NO_STRICT_HOST_KEY_CHECK = "no";
    private final AgentConfig config;
    private Session session;

    public JSchAgentConnection(AgentConfig config) {
        this.config = config;
        if (config == null) {
            throw new AgentException("The AgentConfig cannot be null.");
        }
    }

    /**
     * Check {@link AgentConfig} config parameters validity
     */
    private void checkConfigValidity() {
        if (StringUtils.isEmpty(config.getServerIp())) {
            throw new AgentException("The remote server ip cannot be null.");
        }
        if (StringUtils.isEmpty(config.getUsername())) {
            throw new AgentException("The username cannot be null.");
        }
        if (config.getLocalPort() <= 0 || config.getLocalPort() > 65535) {
            throw new AgentException("The parameter configuration range of the local port number is incorrect.");
        }
        if (config.getForwardTargetPort() <= 0 || config.getForwardTargetPort() > 65535) {
            throw new AgentException("The parameter configuration range of the target port number is incorrect.");
        }
        AuthenticationMethod authenticationMethod = config.getAuthenticationMethod();
        // Password
        if (AuthenticationMethod.USERNAME_PASSWORD == authenticationMethod && StringUtils.isEmpty(config.getPassword())) {
            throw new AgentException("The username or password for logging in to the server is empty.");
        }
    }

    @Override
    public void connect() throws AgentException {
        this.checkConfigValidity();
        try {
            JSch jSch = new JSch();
            if (AuthenticationMethod.SSH_PRIVATE_KEY == config.getAuthenticationMethod()) {
                jSch.addIdentity(config.getSshPrivateKeyPath());
                jSch.setKnownHosts(config.getSshKnownHostsPath());
            }
            this.session = jSch.getSession(config.getUsername(), config.getServerIp(), config.getSshPort());
            if (AuthenticationMethod.USERNAME_PASSWORD == config.getAuthenticationMethod()) {
                this.session.setPassword(config.getPassword());
            }
            config.getAddition().put(STRICT_HOST_KEY_CHECK, NO_STRICT_HOST_KEY_CHECK);
            this.session.setConfig(config.getAddition());
            this.session.connect();
            log.info("Connection to the remote server [{}] is successful.", config.getServerIp());
            // Local and remote port number binding forwarding
            this.session.setPortForwardingL(config.getLocalPort(), config.getForwardTargetIp(), config.getForwardTargetPort());
            log.info("Port forwarding binding is completed, local port : {}, forward IP: {}, forward port : {}",
                    config.getLocalPort(), config.getForwardTargetIp(), config.getForwardTargetPort());
        } catch (JSchException e) {
            throw new AgentException(e.getMessage(), e);
        }
    }

    @Override
    public void disconnect() {
        session.disconnect();
    }
}
