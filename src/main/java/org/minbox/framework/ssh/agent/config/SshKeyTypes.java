package org.minbox.framework.ssh.agent.config;

import lombok.Getter;

/**
 * The key type used to generate the ssh secret key
 * ssh-keygen -t ed25519
 *
 * @author 恒宇少年
 */
@Getter
public enum SshKeyTypes {
    /**
     * The default generate away
     */
    rsa("id_rsa");
    private String privateKeyFile;

    SshKeyTypes(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }
}
