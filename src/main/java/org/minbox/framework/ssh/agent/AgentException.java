package org.minbox.framework.ssh.agent;

import lombok.NoArgsConstructor;

/**
 * Define agent connection exception
 *
 * @author 恒宇少年
 */
@NoArgsConstructor
public class AgentException extends RuntimeException {
    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
