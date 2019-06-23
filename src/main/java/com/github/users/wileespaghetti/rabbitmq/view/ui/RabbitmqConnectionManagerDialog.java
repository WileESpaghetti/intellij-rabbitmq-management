package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

// com.intellij.database.view.ui.DataSourceManagerDialog
public class RabbitmqConnectionManagerDialog extends DialogWrapper {
    public RabbitmqConnectionManagerDialog() {
        super(true);
        init();
        setTitle("RabbitMQ");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Hello World");
        label.setPreferredSize(new Dimension(100, 100));
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }

    public static void showDialog() {
        RabbitmqConnectionManagerDialog dialog = new RabbitmqConnectionManagerDialog();
        dialog.show();
    }

}
