package com.g.autoconfigure;

import com.g.service.AuthService;
import com.g.service.SSHCommandClient;
import com.g.service.impl.SSHCommandClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties({SSHProperties.class})
@ComponentScan(value = "com.g.service.impl")
public class SSHAutoConfiguration {

    private SSHProperties sshProperties;
    private Map<String, AuthService> authServiceMap;

    @Autowired
    public SSHAutoConfiguration(SSHProperties sshProperties, Map<String, AuthService> authServiceMap) {
        this.sshProperties = sshProperties;
        this.authServiceMap = authServiceMap;
    }

    @Bean
    public SSHCommandClient getSSHCommandClient() {
        return new SSHCommandClientImpl(sshProperties, authServiceMap);
    }
}
