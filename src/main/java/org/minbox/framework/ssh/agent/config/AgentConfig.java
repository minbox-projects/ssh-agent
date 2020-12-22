package org.minbox.framework.ssh.agent.config;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.Properties;

/**
 * The agent connection config parameters
 *
 * @author 恒宇少年
 */
@Data
@Accessors(chain = true)
public class AgentConfig {
    /**
     * Java "user.home" property key
     */
    private static final String USER_HOME_PROPERTY_KEY = "user.home";
    /**
     * The system directory separator
     */
    private static final String PATH_SEPARATOR = File.separator;
    /**
     * The default ssh directory of current user
     */
    private static final String SSH_HOME_DIR = System.getProperty(USER_HOME_PROPERTY_KEY) + PATH_SEPARATOR + ".ssh";
    /**
     * Authentication method for logging in to the remote server
     * <p>
     * the default is {@link AuthenticationMethod#SSH_PRIVATE_KEY}
     */
    private AuthenticationMethod authenticationMethod = AuthenticationMethod.SSH_PRIVATE_KEY;
    /**
     * username to log in to the server
     */
    private String username;
    /**
     * password to log in to the server
     */
    private String password;
    /**
     * IP address of the login server
     */
    private String serverIp;
    /**
     * The SSH port number of the server
     * <p>
     * the default is: 22
     */
    private int sshPort = 22;
    /**
     * Private key for logging in using ssh
     * <p>
     * the default use "RSA" private key file path
     */
    private String sshPrivateKeyPath = SSH_HOME_DIR + PATH_SEPARATOR + "id_rsa";
    /**
     * Known hosts file path
     */
    private String sshKnownHostsPath = SSH_HOME_DIR + PATH_SEPARATOR + "known_hosts";
    /**
     * Port of the local agent
     */
    private int localPort;
    /**
     * Remote server port number for forwarding
     */
    private int forwardTargetPort;
    /**
     * IP address of the forwarded target server
     * <p>
     * the default IP address of the machine is used: "127.0.0.1",
     * which can be modified to the LAN IP
     */
    private String forwardTargetIp = "127.0.0.1";
    /**
     * Additional configuration parameters
     */
    private Properties addition = new Properties();
}
