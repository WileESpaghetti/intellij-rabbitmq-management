package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.UUID;

// com.intellij.database.dataSource.DatabaseDriverImpl
public class ConnectionTypeImpl implements  ConnectionType {
    private final String myId;
    private String myName;
    private String myComment;
    private final boolean myPredefined;
    private String myConnectionTypeClass;

    public ConnectionTypeImpl(@Nullable String id, boolean isPredefined) {
        this.myId = StringUtil.isEmpty(id) ? UUID.randomUUID().toString() : id;
        this.myPredefined = isPredefined;
    }

    public ConnectionTypeImpl(@Nullable String id, @Nullable String name, @Nullable String connectionTypeClass) {
        this(id, false);
        this.myName = name;
        this.myConnectionTypeClass = connectionTypeClass;
    }

    public String getId() {
        return this.myId;
    }

    public String getName() {
        return this.myName;
    }
    
    public boolean isPredefined() {
        return this.myPredefined;
    }


    public boolean equalConfiguration(ConnectionTypeImpl comparison) {
        if (!Comparing.equal(StringUtil.nullize(this.myName), StringUtil.nullize(comparison.myName))) {
            return false;
        } else {
            // TODO Compare actual configuration values
            return false;
        }
    }

    @Nullable
    public String getComment() {
        return this.myComment;
    }

    public Element getState(@Nullable ConnectionType connectionType) {
        Element connectionTypeRoot = new Element("connectiontype");
        connectionTypeRoot.setAttribute("id", this.myId);
        boolean isNotPredefined = !this.myPredefined || connectionType == null;
        if (isNotPredefined || !Comparing.equal(connectionType.getName(), this.myName)) {
            connectionTypeRoot.setAttribute("name", StringUtil.notNullize(this.myName));
        }

        return connectionTypeRoot;
    }

    public void loadState(@NotNull Element state, boolean isPredefined, boolean isFixed) {
        if (isFixed || this.myName == null) {
            this.myName = getOverride(state.getAttributeValue("name"), this.myName, isPredefined);
        }
    }

    private static String getOverride(String value, String defaultValue, boolean isPredefined) {
        return isPredefined ? getIfNotNull(value, defaultValue) : value;
    }

    private static String getIfNotNull(String value, String defaultValue) {
        return StringUtil.isEmpty(value) ? defaultValue : value;
    }

    @NotNull
    public ConnectionTypeImpl copy(@Nullable String name, boolean var2) {
        ConnectionTypeImpl newConnectionType = new ConnectionTypeImpl(var2 ? this.myId : null, (String) ObjectUtils.chooseNotNull(name, this.myName), (String)null);
        newConnectionType.loadState(this.getState(null), false, false);

        return newConnectionType;
    }

    public LocalDataSource createDataSource(@Nullable String name) {
        LocalDataSource dataSource = LocalDataSource.fromDriver(this, false);

        dataSource.setName(StringUtil.notNullize(name));
        return dataSource;
    }

    @Override
    public Icon getIcon(int flags) {
        return null;
    }
}
