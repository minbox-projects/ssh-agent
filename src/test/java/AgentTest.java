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
        AgentConfig config = new AgentConfig();
        config.setServerIp("xxx.xxx.xxx.xxx");
        config.setUsername("root");
        config.setLocalPort(3307);
        config.setForwardTargetPort(3306);
        AgentConnection connection = new DefaultAgentConnection(config);
        connection.connect();
        // 连接成功后，访问本地3307的数据库，其实就是访问远程服务器的3306
    }

    /**
     * 使用用户名密码方式连接
     */
    static void usernamePasswordConnect() {
        AgentConfig config = new AgentConfig();
        config.setServerIp("xxx.xxx.xxx.xxx");
        config.setUsername("root");
        config.setPassword("密码");
        config.setLocalPort(3307);
        config.setForwardTargetPort(3306);
        AgentConnection connection = new DefaultAgentConnection(config);
        connection.connect();
        // 连接成功后，访问本地3307的数据库，其实就是访问远程服务器的3306
    }

    public static void main(String[] args) {
        sshPrivateConnect();
    }
}
