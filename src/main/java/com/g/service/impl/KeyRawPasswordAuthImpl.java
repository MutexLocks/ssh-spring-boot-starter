package com.g.service.impl;

import com.g.autoconfigure.SSHProperties;
import com.g.exception.SSHClientException;
import com.g.service.AuthService;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.PKCS8KeyFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;

@Service("KEY_RAW")
public class KeyRawPasswordAuthImpl implements AuthService {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyRawPasswordAuthImpl.class);

    private SSHProperties sshProperties;

    @Autowired
    public KeyRawPasswordAuthImpl(SSHProperties sshProperties) {
        this.sshProperties = sshProperties;
    }

    @Override
    public void auth(SSHClient sshClient) {
        if (sshClient == null) {
            LOGGER.error("ssh client is null");
            throw new SSHClientException("auth failed , ssh client is null");
        }

        try {
            PKCS8KeyFile keyFile = new PKCS8KeyFile();
            Reader keyContent = new StringReader(sshProperties.getKeyRaw());
            keyFile.init(keyContent);
            sshClient.authPublickey(sshProperties.getLoginName(), keyFile);
        } catch (Exception ex) {
            LOGGER.error("auth failed :", ex);
            throw new SSHClientException("auth failed :", ex);
        }
    }
}
