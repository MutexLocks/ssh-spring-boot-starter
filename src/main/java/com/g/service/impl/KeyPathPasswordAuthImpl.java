package com.g.service.impl;

import com.g.autoconfigure.SSHProperties;
import com.g.exception.SSHClientException;
import com.g.service.AuthService;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("KEY_PATH")
public class KeyPathPasswordAuthImpl implements AuthService {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyPathPasswordAuthImpl.class);

    private SSHProperties sshProperties;

    @Autowired
    public KeyPathPasswordAuthImpl(SSHProperties sshProperties) {
        this.sshProperties = sshProperties;
    }

    @Override
    public void auth(SSHClient sshClient) {
        if(sshClient == null) {
            LOGGER.error("ssh client is null");
            throw new SSHClientException("auth failed , ssh client is null");
        }

        sshClient.addHostKeyVerifier(new PromiscuousVerifier());
        try {
            sshClient.authPublickey(sshProperties.getLoginName(), sshProperties.getKeyPath());
        } catch (Exception ex) {
            throw new SSHClientException("auth failed :", ex);
        }
    }
}
