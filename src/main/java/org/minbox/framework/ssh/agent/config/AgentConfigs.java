package org.minbox.framework.ssh.agent.config;

import lombok.Data;

import java.util.List;

/**
 * {@link AgentConfig} configs
 *
 * @author 恒宇少年
 */
@Data
public class AgentConfigs {
    private List<AgentConfig> configs;
}
