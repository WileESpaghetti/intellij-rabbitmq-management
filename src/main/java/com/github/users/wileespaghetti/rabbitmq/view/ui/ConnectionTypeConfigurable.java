package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionTypeImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

// com.intellij.database.view.ui;DatabaseDriverConfigurable
public class ConnectionTypeConfigurable extends com.github.users.wileespaghetti.rabbitmq.connectionType.AbstractRabbitmqConfigurable<ConnectionTypeImpl> {
    private final ConnectionTypeImpl myTempConnectionType = new ConnectionTypeImpl("temp", true);

    @NotNull
    public ConnectionTypeImpl getTempConnectionType() {
        return new ConnectionTypeImpl("temp", "My Temp Connection Type");
    }

    @NotNull
    @Override
    public ConnectionTypeImpl getTempTarget() {
        return this.myTempConnectionType;
    }

    @Nullable
    public JComponent createComponent() {
        JPanel content = new JPanel(new BorderLayout(0, 5));
        content.add(new JLabel("ConnectionTypeConfigurable"), BorderLayout.CENTER);
        return content;
    }
}

