package org.minbox.framework.ssh.agent;

import lombok.Getter;

/**
 * ssh-agent实现支持定义
 *
 * @author 恒宇少年
 * @since 2.3.11
 */
@Getter
public enum AgentSupport {
    /**
     * Jsch
     */
    jsch("org.minbox.framework.ssh.agent.jsch.JSchAgentConnection"),
    /**
     * Apache mina
     */
    mina("org.minbox.framework.ssh.agent.apache.ApacheMinaSshdAgentConnection");
    private final String className;

    AgentSupport(String className) {
        this.className = className;
    }
}
