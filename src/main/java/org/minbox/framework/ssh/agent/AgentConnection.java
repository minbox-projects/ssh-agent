package org.minbox.framework.ssh.agent;

/**
 * Agent link communication interface definition
 *
 * @author 恒宇少年
 */
public interface AgentConnection {
    /**
     * Perform connection
     * <p>
     * bind the local port number with the remote server port number
     * when accessing the local port number,
     * the corresponding forwarding to the remote server specifies the IP + Port
     */
    void connect() throws AgentException;

    /**
     * Close forwarding connection
     */
    void disconnect();
}
