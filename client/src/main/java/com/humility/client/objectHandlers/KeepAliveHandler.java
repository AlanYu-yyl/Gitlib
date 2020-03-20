package com.humility.client.objectHandlers;

import com.humility.client.Client;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeepAliveHandler implements ObjectHandler {
    @Override
    public void handleObejct(Object obj, Client client) {
        log.info("Keep Alive: Server is still online.");
    }
}
