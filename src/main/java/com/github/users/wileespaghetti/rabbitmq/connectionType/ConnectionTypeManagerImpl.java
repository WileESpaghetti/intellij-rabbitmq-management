package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.EventDispatcher;
import com.intellij.util.containers.ContainerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.*;

@State(
        name = "LocalConnectionTypeManager",
        storages = {@Storage("connection-types.xml")}
)
public class ConnectionTypeManagerImpl extends ConnectionTypeManager implements PersistentStateComponent<Element> {
    private static final Logger LOG = Logger.getInstance(ConnectionTypeManagerImpl.class);
    public static final String URL_CONNECTION_TYPES_LOCATION = "connection-types.xml";
    private final EventDispatcher<ConnectionTypeListener> myDispatcher;
    private final Map<String, ConnectionTypeImpl> myConnectionTypes;
    private final Map<String, ConnectionTypeImpl> myPredefinedConnectionTypes;

    public ConnectionTypeManagerImpl() {
        this(getPredefinedLocation());
    }

    private ConnectionTypeManagerImpl(@Nullable URL var1) {
        this.myConnectionTypes = ContainerUtil.newLinkedHashMap();
        this.myPredefinedConnectionTypes = ContainerUtil.newLinkedHashMap();
        this.myDispatcher = EventDispatcher.create(ConnectionTypeListener.class);

        try {
            if (var1 != null) {
                this.loadLatest(JDOMUtil.load(var1), false, true);
            }
        } catch (Exception e) {
            LOG.warn(e);
        }

        Iterator<ConnectionTypeImpl> connectionTypes = this.myConnectionTypes.values().iterator();

        while(connectionTypes.hasNext()) {
            ConnectionTypeImpl connectionType = connectionTypes.next();
            if (connectionType.isPredefined()) {
                ConnectionTypeImpl predefinedConnectionType = new ConnectionTypeImpl(connectionType.getId(), true);
                predefinedConnectionType.loadState(connectionType.getState(null), false, true);
                this.myPredefinedConnectionTypes.put(predefinedConnectionType.getId(), predefinedConnectionType);
            }
        }
    }

    @Nullable
    private static URL getPredefinedLocation() {
        return ConnectionTypeManagerImpl.class.getClassLoader().getResource(URL_CONNECTION_TYPES_LOCATION);
    }

    @NotNull
    private ConnectionTypeImpl addConnectionType(@NotNull ConnectionTypeImpl connectionType) {
        this.myConnectionTypes.put(connectionType.getId(), connectionType);
        this.getDispatcher().getMulticaster().connectionTypeAdded(connectionType);

        return connectionType;
    }

    @Override
    public Collection<ConnectionType> getConnectionTypes() {
        return Collections.unmodifiableCollection(this.myConnectionTypes.values());
    }

    @Override
    public ConnectionType getConnectionType(String id) {
        return this.myConnectionTypes.get(StringUtil.notNullize(id));
    }

    @Override
    public void resetToPredefined(@NotNull ConnectionType connectionType) {
        // FIXME ConnectionTypes can not be currently edited so this is a noop
    }

    @Override
    public boolean isInPredefinedState(@NotNull ConnectionType connectionType) {
        ConnectionTypeImpl predefinedConnectionType = this.myPredefinedConnectionTypes.get(connectionType.getId());
        return predefinedConnectionType == null || ((ConnectionTypeImpl) connectionType).equalConfiguration(predefinedConnectionType);
    }

    @NotNull
    @Override
    public ConnectionType createConnectionType(String id, String name) {
        String newId = id;
        if (this.getConnectionType(id) != null) {
            newId = null;
        }

        return new ConnectionTypeImpl(newId, name);
    }


    @Override
    public void updateConnectionType(@NotNull ConnectionType connectionType) {
        if (!this.myConnectionTypes.containsKey(connectionType.getId())) {
            this.addConnectionType((ConnectionTypeImpl) connectionType);
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

    @Nullable
    @Override
    public Element getState() {
        Element localConnectionTypes = new Element("LocalConnectionTypeManager");
        Iterator<ConnectionTypeImpl> connectionTypes = this.myConnectionTypes.values().iterator();

        while(connectionTypes.hasNext()) {
            ConnectionTypeImpl connectionTypeRoot = (ConnectionTypeImpl) connectionTypes.next();
            localConnectionTypes.addContent(connectionTypeRoot.getState(this.myPredefinedConnectionTypes.get(connectionTypeRoot.getId())));
        }

        return localConnectionTypes;
    }

    @Override
    public void loadState(@NotNull Element state) {
        this.loadLatest(state, true, false);
    }

    private void loadLatest(@NotNull Element connectionTypesRoot, boolean isPredefined, boolean isFixed) {
        Iterator<Element> connectionTypes = getConnectionTypes(connectionTypesRoot).iterator();

        while(connectionTypes.hasNext()) {
            Element connectionTypeNode = connectionTypes.next();
            String id = getConnectionTypeId(connectionTypeNode);
            ConnectionTypeImpl connectionType = id == null ? null : this.getOrCreateDriver(id, isFixed, true);
            if (connectionType != null) {
                connectionType.loadState(connectionTypeNode, isPredefined, true);
            }
        }
    }

    @NotNull
    private static List<Element> getConnectionTypes(@NotNull Element connectionTypesRoot) {
        List<Element> connectionTypes = connectionTypesRoot.getChildren("connection-type");
        return connectionTypes;
    }

    @Nullable
    private ConnectionTypeImpl getOrCreateDriver(@NotNull String id, boolean isPredefined, boolean isFixed) {
        ConnectionTypeImpl connectiontype = (ConnectionTypeImpl) this.myConnectionTypes.get(id);
        if (connectiontype == null && isFixed) {
            connectiontype = this.addConnectionType(new ConnectionTypeImpl(id, isPredefined));
        }

        return connectiontype;
    }

    @Nullable
    private static String getConnectionTypeId(@NotNull Element connectionTypeNode) {
        String id = connectionTypeNode.getAttributeValue("id");
        return !StringUtil.isEmpty(id) ? id : null;
    }
}
