package com.humility.server.objectHandler;

import com.humility.datas.KeepAlive;

public class KeepAliveHandler implements ObjectHandler {
    @Override
    public Object handleObject(Object rev) {
        return new KeepAlive();
    }
}
