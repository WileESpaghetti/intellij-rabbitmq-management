package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.Disposable;
import com.intellij.util.EventDispatcher;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ConnectionTypeManagerImpl extends ConnectionTypeManager {
    private final EventDispatcher<ConnectionTypeListener> myDispatcher;
    private final Map<String, ConnectionType> myConnectionTypes;

    public ConnectionTypeManagerImpl() {
        this.myConnectionTypes = ContainerUtil.newLinkedHashMap();
        this.myDispatcher = EventDispatcher.create(ConnectionTypeListener.class);
    }

    @NotNull
    private ConnectionType addConnectionType(@NotNull ConnectionType connectionType) {
        this.myConnectionTypes.put(connectionType.getId(), connectionType);
        this.getDispatcher().getMulticaster().connectionTypeAdded(connectionType);

        return connectionType;
    }

    @Override
    public Collection<ConnectionType> getConnectionTypes() {
        return Collections.unmodifiableCollection(this.myConnectionTypes.values());
    }

    @Override
    public void updateConnectionType(@NotNull ConnectionType connectionType) {
        if (!this.myConnectionTypes.containsKey(connectionType.getId())) {
            this.addConnectionType(connectionType);
        }
    }

    @Override
    public void removeConnectionType(@NotNull ConnectionType connectionType) {
        ConnectionType removedConnectionType = this.myConnectionTypes.remove(connectionType);
        if (removedConnectionType != null) {
            this.getDispatcher().getMulticaster().connectionTypeRemoved(removedConnectionType);
        }

    }

    @Override
    public void addConnectionTypeListener(@NotNull ConnectionTypeListener listener, @NotNull Disposable disposable) {
        this.myDispatcher.addListener(listener, disposable);
    }

    EventDispatcher<ConnectionTypeListener> getDispatcher() {
        return this.myDispatcher;
    }

}
