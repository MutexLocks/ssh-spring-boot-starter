package com.g.service.impl;

import com.g.autoconfigure.SSHProperties;
import com.g.model.SSHResponse;
import com.g.service.AuthService;
import com.g.service.SSHCommandClient;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SSHCommandClientImpl implements SSHCommandClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(SSHCommandClientImpl.class);
    private static SSHClient sshClient ;


    private SSHProperties sshProperties;
    private Map<String, AuthService> authServiceMap;


    public SSHCommandClientImpl(SSHProperties sshProperties, Map<String, AuthService> authServiceMap) {
        this.sshProperties = sshProperties;
        this.authServiceMap = authServiceMap;
    }

    @Override
    public SSHResponse exec(String command) {
        SSHResponse response = new SSHResponse();
        if (StringUtils.isNotBlank(command)) {

            try {
                Session session = getSession();

                Session.Command cmd = session.exec(command);
                cmd.join(sshProperties.getTimeoutSeconds(), TimeUnit.SECONDS);
                LOGGER.info("execute command: {}", command);
                Integer status = cmd.getExitStatus();
                response.setStatus(status);
                if (status != 0) {
                    String errorMessage = IOUtils.readFully(cmd.getErrorStream()).toString();
                    response.setErrorMessage(errorMessage);
                    return response;
                }
                String message = IOUtils.readFully(cmd.getInputStream()).toString();
                response.setData(message);
            } catch (Exception ex) {
                LOGGER.error("failed to exec command: " + command, ex);
                response.setStatus(-1);
                response.setErrorMessage(String.format("failed to exec command: %s, %s", command, ex.getMessage()));
            }
        }
        return response;
    }

    private Session getSession() throws IOException {

        if (sshClient == null || !sshClient.isConnected()) {
            synchronized (this) {
                if (sshClient == null || !sshClient.isConnected()) {
                    SSHClient newSSHClient = new SSHClient();
                    newSSHClient.setConnectTimeout(sshProperties.getConnectTimeoutSeconds() * 1000);
                    newSSHClient.setTimeout(sshProperties.getSocketTimeoutSeconds() * 1000);
                    newSSHClient.addHostKeyVerifier(new PromiscuousVerifier());
                    newSSHClient.connect(sshProperties.getHost(), sshProperties.getPort());
                    sshClient = newSSHClient;
                }
            }
        }

        if (!sshClient.isAuthenticated()) {
            synchronized (this) {
                if (!sshClient.isAuthenticated()) {
                    AuthService authService = authServiceMap.get(sshProperties.getAuthMethod().getCode());
                    if (authService == null) {
                        throw new IllegalStateException("ssh auth is not initialized");
                    }
                    authService.auth(sshClient);
                }
            }
        }
        return sshClient.startSession();
    }
}
