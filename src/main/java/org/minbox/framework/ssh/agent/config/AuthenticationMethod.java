package org.minbox.framework.ssh.agent.config;

/**
 * Authentication method for logging in to the remote server
 *
 * @author 恒宇少年
 */
public enum AuthenticationMethod {
    /**
     * User name password authentication
     */
    USERNAME_PASSWORD,
    /**
     * Ssh private key authentication
     */
    SSH_PRIVATE_KEY
}
