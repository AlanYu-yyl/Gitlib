package com.humility.server;

import com.humility.datas.KeepAlive;

public class KeepAliveHandler implements ObjectHandler {
    @Override
    public Object handleObject(Object rev) {
        return new KeepAlive();
    }
}
