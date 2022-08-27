package org.minbox.framework.ssh.agent.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.minbox.framework.ssh.agent.AgentConnection;
import org.minbox.framework.ssh.agent.AgentException;
import org.minbox.framework.ssh.agent.DefaultAgentConnection;
import org.minbox.framework.ssh.agent.config.AgentConfig;
import org.minbox.framework.ssh.agent.config.AgentConfigs;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 为Junit提供的Ssh代理单元测试类
 * <p>
 * 继承该类的Junit单元测试类在执行之前会自动根据配置代理ssh，省去了单独开启ssh代理的步骤
 * <p>
 * ssh-agent.yml Example：
 * <code>
 * configs:
 * - username: developer
 * serverIp: x.x.x.x
 * localPort: 3306
 * forwardTargetPort: 3306
 * - username: developer
 * serverIp: x.x.x.x
 * localPort: 6379
 * forwardTargetPort: 6379
 * </code>
 *
 * @author 恒宇少年
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Slf4j
public class SshAgentJunitTest {
    /**
     * At "src/test/resources/ssh-agent.yml"
     */
    private static final String SSH_AGENT_YAML_NAME = "ssh-agent.yml";
    private static final List<AgentConnection> connections = new ArrayList<>();
    private static List<AgentConfig> configs;

    static {
        ClassPathResource classPathResource = new ClassPathResource(SSH_AGENT_YAML_NAME);
        try {
            File file = classPathResource.getFile();
            Yaml yaml = new Yaml();
            AgentConfigs agentConfigs = yaml.loadAs(new FileInputStream(file), AgentConfigs.class);
            configs = agentConfigs.getConfigs();
        } catch (FileNotFoundException ffe) {
            throw new AgentException("无法加载ssh-agent.yml, 请在src/test/resources下创建该文件, 并根据AgentConfig类字段进行配置.");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @BeforeAll
    public static void beforeSetSshProxy() {
        configs.stream().forEach(config -> {
            try {
                AgentConnection connection = new DefaultAgentConnection(config);
                connections.add(connection);
                connection.connect();
            } catch (Exception e) {
                log.error("Connection：{}:{}，try agent failure.", config.getServerIp(), config.getForwardTargetPort(), e);
            }
        });
    }

    @AfterAll
    public static void afterCloseSshProxy() {
        connections.stream().forEach(connection -> connection.disconnect());
        log.info("All ssh proxy are disconnected.");
    }
}
