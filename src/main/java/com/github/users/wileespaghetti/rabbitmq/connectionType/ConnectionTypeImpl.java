package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

// com.intellij.database.dataSource.DatabaseDriverImpl
public class ConnectionTypeImpl implements  ConnectionType {
    private final String myId;
    private String myName;
    private final boolean myPredefined;

    public ConnectionTypeImpl(@Nullable String id, boolean isPredefined) {
        this.myId = StringUtil.isEmpty(id) ? UUID.randomUUID().toString() : id;
        this.myPredefined = isPredefined;
    }

    public ConnectionTypeImpl(@Nullable String id, @Nullable String name) {
        this(id, false);
        this.myName = name;
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
}
