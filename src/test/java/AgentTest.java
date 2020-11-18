import org.minbox.framework.ssh.agent.AgentConnection;
import org.minbox.framework.ssh.agent.DefaultAgentConnection;
import org.minbox.framework.ssh.agent.config.AgentConfig;
import org.minbox.framework.ssh.agent.config.AuthenticationMethod;

/**
 * 测试ssh agent
 *
 * @author 恒宇少年
 */
public class AgentTest {

    /**
     * 使用ssh private key方式代理连接远程服务
     * <p>
     * 本地 "3307" 端口号绑定远程服务器 "3306"端口号，实现本地访问远程服务器上的MySQL服务
     */
    static void sshPrivateConnect() {
        AgentConfig config2 = new AgentConfig();
        config2.setServerIp("xxx.xxx.xxx.xxx");
        config2.setUsername("root");
        config2.setLocalPort(3307);
        config2.setForwardTargetPort(3306);
        AgentConnection connection2 = new DefaultAgentConnection(config2);
        connection2.connect();
        // 连接成功后，访问本地3307的数据库，其实就是访问远程服务器的3306
    }

    public static void main(String[] args) {
        sshPrivateConnect();
    }
}
