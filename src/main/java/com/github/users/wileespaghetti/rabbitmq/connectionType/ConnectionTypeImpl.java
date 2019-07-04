package com.github.users.wileespaghetti.rabbitmq.connectionType;

import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ConnectionTypeImpl implements  ConnectionType {
    private final String myId;
    private String myName;

    public ConnectionTypeImpl(@Nullable String id) {
        this.myId = StringUtil.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

    public ConnectionTypeImpl(@Nullable String id, @Nullable String name) {
        this(id);
        this.myName = name;
    }

    public String getId() {
        return this.myId;
    }

    public String getName() {
        return this.myName;
    }

    public boolean equalConfiguration(ConnectionTypeImpl comparison) {
        if (!Comparing.equal(StringUtil.nullize(this.myName), StringUtil.nullize(comparison.myName))) {
            return false;
        } else {
            // TODO Compare actual configuration values
            return false;
        }
    }
}
