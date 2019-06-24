package com.github.users.wileespaghetti.rabbitmq.view.ui;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

// com.intellij.database.view.ui.DataSourceSidePanel
public class ConnectionsSidePanel {
    private final JPanel myPanel = new JPanel(new BorderLayout());

    public ConnectionsSidePanel() {
        UIUtil.setBackgroundRecursively(this.myPanel, UIUtil.SIDE_PANEL_BACKGROUND);
    }

    public JComponent getComponent() {
        return this.myPanel;
    }
}
