package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.github.users.wileespaghetti.rabbitmq.connectionType.AbstractRabbitmqConfigurable;
import com.github.users.wileespaghetti.rabbitmq.connectionType.ConnectionTypeImpl;
import com.github.users.wileespaghetti.rabbitmq.connectionType.RabbitmqNameComponent;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

// com.intellij.database.view.ui;DatabaseDriverConfigurable
public class ConnectionTypeConfigurable extends AbstractRabbitmqConfigurable<ConnectionTypeImpl> {
    private RabbitmqNameComponent myNameComponent;
    private final ConnectionTypeImpl myConnectionType;
    private final ConnectionTypeImpl myTempConnectionType;

    ConnectionTypeConfigurable(@NotNull ConnectionTypeImpl connectionType, @NotNull RabbitmqConfigEditor controller) {
        super(connectionType);
        this.setController(controller);
        this.myConnectionType = connectionType;
        this.myTempConnectionType = connectionType.copy((String)null, true);
    }

//    @NotNull
//    public ConnectionTypeImpl getTempConnectionType() {
//        return new ConnectionTypeImpl("temp", "My Temp Connection Type");
//    }

    @NotNull
    @Override
    public ConnectionTypeImpl getTempTarget() {
        return this.myTempConnectionType;
    }

    @Nullable
    public JComponent createComponent() {
        JPanel content = new JPanel(new BorderLayout(0, 5));
        this.myNameComponent = new RabbitmqNameComponent(this, this.myController, false);
        content.add(this.myNameComponent.getComponent(), BorderLayout.NORTH);
        content.add(new JBLabel("ConnectionTypeConfigurable"), BorderLayout.CENTER);
        return content;
    }

    public void setController(@NotNull RabbitmqConfigEditor editor) {
        this.myController = editor;
    }

    public void resetFromTemp() {
        ((SettingsEditor)this.myController).bulkUpdate(() -> {
            this.reset(this.myTempConnectionType);
        });
    }

    protected void reset(@NotNull ConnectionTypeImpl connectionType) {
        if (this.myNameComponent != null) {
            this.myNameComponent.reset(connectionType, null);
        }
    }
}

