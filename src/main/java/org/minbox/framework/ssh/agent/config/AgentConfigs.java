package org.minbox.framework.ssh.agent.config;

import lombok.Data;
import org.minbox.framework.ssh.agent.AgentSupport;

import java.util.List;

/**
 * {@link AgentConfig} configs
 *
 * @author 恒宇少年
 */
@Data
public class AgentConfigs {
    /**
     * 运行环境
     * 读取该运行环境的后缀配置文件，如：运行环境为"dev"则加载"ssh-agent-dev.yml"
     */
    private String profile;
    /**
     * 支持的代理
     */
    private AgentSupport support = AgentSupport.mina;
    /**
     * 配置列表
     */
    private List<AgentConfig> configs;
}
