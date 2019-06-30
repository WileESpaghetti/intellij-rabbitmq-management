package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.components.ServiceManager;

public abstract class ConnectionTypeManager {
    public static ConnectionTypeManager getInstance() {
        return ServiceManager.getService(ConnectionTypeManager.class);
    }
}
