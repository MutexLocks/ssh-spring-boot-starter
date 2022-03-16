package com.g.autoconfigure;

import com.g.model.AuthMethodEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ssh")
public class SSHProperties {
    private String host = "127.0.0.1";
    private int port = 22;
    private String loginName = "root";
    // 密码
    private String password;
    // 密钥路径
    private String keyPath;
    // 密钥
    private String keyRaw;
    // 执行ssh命令超时时间
    private int timeoutSeconds = 60;
    // 连接ssh超时时间
    private int connectTimeoutSeconds = 60;
    // socket超时时间
    private int socketTimeoutSeconds = 60;
    private AuthMethodEnum authMethod = AuthMethodEnum.USER_PASSWORD;


}
