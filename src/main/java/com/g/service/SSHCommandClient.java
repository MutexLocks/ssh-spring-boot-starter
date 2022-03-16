package com.g.service;

import com.g.model.SSHResponse;

public interface SSHCommandClient {
    SSHResponse exec(String command);
}
