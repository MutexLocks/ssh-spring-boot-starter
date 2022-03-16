package com.g.service;

import net.schmizz.sshj.SSHClient;

public interface AuthService {
    void auth(SSHClient sshClient);
}
