package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class ConnectionTypeManager {
    public static ConnectionTypeManager getInstance() {
        return ServiceManager.getService(ConnectionTypeManager.class);
    }

    public abstract Collection<ConnectionType> getConnectionTypes();

    public abstract ConnectionType getConnectionType(String id);

    public abstract void resetToPredefined(@NotNull ConnectionType connectionType);

    public abstract boolean isInPredefinedState(@NotNull ConnectionType connectionType);

    @NotNull
    public abstract ConnectionType createConnectionType(String id, String name);

    public abstract void updateConnectionType(@NotNull ConnectionType connectionType);

    public abstract void removeConnectionType(@NotNull ConnectionType connectionType);

    public abstract void addConnectionTypeListener(@NotNull ConnectionTypeListener listener, @NotNull Disposable disposable);
}
