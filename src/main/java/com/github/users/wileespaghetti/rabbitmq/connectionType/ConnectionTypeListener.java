package com.github.users.wileespaghetti.rabbitmq.connectionType;

import java.util.EventListener;

public interface ConnectionTypeListener extends EventListener {
    void connectionTypeAdded(ConnectionType connectionType);

    void connectionTypeRemoved(ConnectionType connectionType);

    void connectionTypeUpdated(ConnectionType connectionType);

    public static class Adapter implements ConnectionTypeListener {

        @Override
        public void connectionTypeAdded(ConnectionType connectionType) {
        }

        @Override
        public void connectionTypeRemoved(ConnectionType connectionType) {
        }

        @Override
        public void connectionTypeUpdated(ConnectionType connectionType) {
        }
    }
}
